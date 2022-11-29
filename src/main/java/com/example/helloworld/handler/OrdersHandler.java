package com.example.helloworld.handler;

import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RabbitConsts;
import com.example.helloworld.entity.*;
import com.example.helloworld.service.*;
import com.example.helloworld.utils.CommonUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 订单处理器
 */
@Slf4j
@RabbitListener(queues = RabbitConsts.SIMPLE_MODE_QUEUE_SUBMISSION)
@Component
public class OrdersHandler {
    private final CommonUtil commonUtil;
    @Autowired
    private UsersService usersService;
    @Autowired
    private GroupsUserService groupsUserService;
    @Autowired
    private BedsService bedsService;
    @Autowired
    private SysService sysService;
    @Autowired
    private RoomsService roomsService;
    @Autowired
    private OrdersService ordersService;

    public OrdersHandler(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    @RabbitHandler
    public void directHandlerManualAck(OrdersEntity orderEntity, Message message, Channel channel) {
        //  如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("直接队列1，手动ACK，接收消息：{}", orderEntity);
            //// 处理订单
            // 初始化订单处理结果为失败
            orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
            boolean continue_check = true;
            // 获取订单成员id
            List<Integer> uidList = new ArrayList<>();
            if (orderEntity.getGroupId() == 0) {
                uidList.add(orderEntity.getUid());
            } else {
                // 根据groupId获取那些没有被删除的成员id
                List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findAllByGroupIdAndIsDel(orderEntity.getGroupId(), Consts.IS_NOT_DEL);
                for (GroupsUserEntity groupsUserEntity : groupsUserEntityList) {
                    uidList.add(groupsUserEntity.getUid());
                }
            }
            // 判断订单总人数是否超限
            if (uidList.size() > Integer.parseInt(sysService.findByKeyName("group_num").get().getKeyValue())) {
                orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                orderEntity.setResultContent("订单总人数超限" + sysService.findByKeyName("group_num").get().getKeyValue());
                continue_check = false;
            }
            // 判断是否存在重复的成员
            if (continue_check) {
                Set<Integer> uidSet = new HashSet<>(uidList);
                if (uidSet.size() < uidList.size()) {
                    orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                    orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                    orderEntity.setResultContent("Group中存在重复的成员");
                    continue_check = false;
                }
            }
            // 获取userEntityList
            if (continue_check) {
                // 根据uid直接获取，不用筛出valid和del
                List<UsersEntity> usersEntityList = usersService.findById(uidList);
                // 判断是否存在成员被删除
                for (UsersEntity usersEntity : usersEntityList) {
                    if (usersEntity.getIsDel() == Consts.IS_DEL) {
                        orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                        orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                        orderEntity.setResultContent(usersEntity.getName() + "成员被删除");
                        continue_check = false;
                        break;
                    }
                }
                // 判断成员性别是否一致
                if (continue_check) {
                    for (UsersEntity usersEntity : usersEntityList) {
                        if (!Objects.equals(usersEntity.getGender(), usersEntityList.get(0).getGender())) {
                            orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                            orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                            orderEntity.setResultContent(usersEntity.getName() + "性别不一致");
                            continue_check = false;
                            break;
                        }
                    }
                }
                // 判断成员是否已经住宿
                if (continue_check) {
                    for (UsersEntity usersEntity : usersEntityList) {
                        Optional<BedsEntity> bedsEntity = bedsService.findByUidAndStatusAndIsValidAndIsDel(usersEntity.getUid(), Consts.BED_STATUS_USED, Consts.IS_VALID, Consts.IS_NOT_DEL);
                        if (bedsEntity.isPresent()) {
                            orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                            orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                            orderEntity.setResultContent(usersEntity.getName() + "已经住宿");
                            continue_check = false;
                            break;
                        }
                    }
                }
                if (continue_check) {
                    // 查询楼号符合、性别符合、剩余床位数量符合的房间
                    // 查询符合楼号、性别要求的房间
                    List<RoomsEntity> roomsEntityList = roomsService.findAllByBuildingIdAndGenderAndIsValidAndIsDel(orderEntity.getBuildingId(), usersEntityList.get(0).getGender(), Consts.IS_VALID, Consts.IS_NOT_DEL);
                    // 寻找剩余床位数量大于等于要求的房间
                    Optional<RoomsEntity> targetRoom = Optional.empty();
                    for (RoomsEntity roomsEntity : roomsEntityList) {
                        Integer cntFreeBed = bedsService.countByRoomIdAndStatusAndIsValidAndIsDel(roomsEntity.getId(), Consts.BED_STATUS_EMPTY, Consts.IS_VALID, Consts.IS_NOT_DEL);
                        if (cntFreeBed >= usersEntityList.size()) {
                            targetRoom = Optional.of(roomsEntity);
                            break;
                        }
                    }
                    // 未找到可用的房间
                    if (!targetRoom.isPresent()) {
                        orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                        orderEntity.setStatus(Consts.ORDER_STATUS_FAILED);
                        orderEntity.setResultContent("无可用房间");
                        continue_check = false;
                    } else {
                        // 成功啦！
                        RoomsEntity roomsEntity = targetRoom.get();
                        List<BedsEntity> bedsEntityList = bedsService.findAllByRoomId(roomsEntity.getId(), Consts.BED_STATUS_EMPTY, Consts.IS_VALID, Consts.IS_NOT_DEL);
                        Integer curUserIndex = 0;
                        for (BedsEntity bedsEntity : bedsEntityList) {
                            bedsEntity.setUid(usersEntityList.get(curUserIndex).getUid());
                            bedsEntity.setStatus(Consts.BED_STATUS_USED);
                            bedsService.save(bedsEntity);
                            curUserIndex += 1;
                            if (curUserIndex >= usersEntityList.size()) {
                                break;
                            }
                        }
                        // 设置状态
                        orderEntity.setFinishTime(commonUtil.getUnixTimestamp());
                        orderEntity.setRoomId(roomsEntity.getId());
                        orderEntity.setResultContent("处理成功, roomId:" + roomsEntity.getId());
                        orderEntity.setStatus(Consts.ORDER_STATUS_OK);
                    }
                }
            }
            // 写回订单
            ordersService.save(orderEntity);
            // 通知处理完成
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // 处理失败,重新压入MQ
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
