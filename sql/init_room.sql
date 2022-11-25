DELIMITER //

-- 初始化1~5号楼，每层楼10个房间，每个房间的大小为2~7
drop procedure if exists func_init_building;
CREATE procedure func_init_building()
BEGIN
    DECLARE val_build_id, var_build_order_num, var_build_is_valid, var_build_is_del INT; -- 申明楼号, 楼层. 房间号
    DECLARE val_build_na,var_build_describe, var_build_image_url varchar(100);
    SET val_build_id = 1; -- 最小楼号
    WHILE val_build_id <= 5
        DO
            SET val_build_na = CONCAT(val_build_id, ' building');
            SET var_build_order_num = val_build_id;
            SET var_build_is_valid = 1; -- 楼全部有效
            SET var_build_describe = CONCAT('this is ', val_build_id, ' building');
            SET var_build_image_url = CONCAT('url of ', val_build_id, ' building');
            SET var_build_is_del = 0; -- 楼层全部未被删除
            INSERT INTO buildings(id, name, order_num, is_valid, buildings.describe, image_url, is_del)
            VALUES (val_build_id, val_build_na, var_build_order_num, var_build_is_valid, var_build_describe,
                    var_build_image_url, var_build_is_del);
            SET val_build_id = val_build_id + 1;
        END WHILE;

END;

-- 初始化房间
drop procedure if exists func_init_room;
CREATE procedure func_init_room()
BEGIN
    DECLARE val_building_id, var_gender, var_order_num, var_is_valid, var_is_del, var_floor_index, var_room_index,var_bed_cnt_all INT;
    DECLARE val_name, var_describe, var_image_url varchar(100);
    SET val_building_id = 1; -- 最小楼号
    WHILE val_building_id <= 5
        DO
            SET var_floor_index = 1; -- 最小层数
            WHILE var_floor_index <= 5
                DO
                    SET var_room_index = 1; -- 每层的最小房间号
                    WHILE var_room_index <= 10
                        DO
                            SET val_name = val_building_id * 1000 + var_floor_index * 100 + var_room_index;
#                             SET var_bed_cnt_all = (RAND() * (10 - 5) + 2); -- 宿舍的床位数量
                            SET var_gender = var_floor_index mod 2; -- 楼层性别按照奇数偶数划分
                            SET var_order_num = CONVERT(val_name, SIGNED); -- 排序和房间名称一致
                            SET var_is_valid = IF(rand() < 0.01, 0, 1); -- 房间有1/100的概率为无效
                            SET var_describe = CONCAT('this is ', val_name, ' room');
                            SET var_image_url = CONCAT('url of ', val_name, ' room');
                            SET var_is_del = IF(rand() < 0.01, 1, 0); -- 房间有1/100的概率被删除
                            INSERT INTO rooms (building_id, rooms.name, gender, order_num, is_valid, rooms.describe,
                                               image_url, is_del)
                            VALUES (val_building_id, val_name, var_gender, var_order_num, var_is_valid, var_describe,
                                    var_image_url, var_is_del);
                            SET var_room_index = var_room_index + 1; -- 循环一次,i加1
                        END WHILE;
                    SET var_floor_index = var_floor_index + 1;
                END WHILE; -- 结束while循环
            SET val_building_id = val_building_id + 1;
        END WHILE;

END;

DROP procedure IF EXISTS func_init_beds;
-- 初始化床位
CREATE PROCEDURE func_init_beds()
BEGIN
    -- 该变量用于标识是否还有数据需遍历
    DECLARE flag INT DEFAULT 0;
    -- 创建一个变量用来存储遍历过程中的值
    DECLARE var_room_id,var_bed_num,var_bed_index,var_order_num,var_is_valid,var_is_del INT DEFAULT 0;
    DECLARE var_name varchar(100);
    -- 查询出需要遍历的数据集合,这里指定数据库下面所有表明
    DECLARE var_room_id_bed_num_list CURSOR FOR select id from rooms;
    -- 查询是否有下一个数据，没有将标识设为1，相当于hasNext
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET flag = 1;
    -- 打开游标
    OPEN var_room_id_bed_num_list;
    -- 取值设置到临时变量中，我这里就一个字段
    -- 注意：多个字段取值，变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
    FETCH var_room_id_bed_num_list INTO var_room_id;
    -- 遍历未结束就一直执行
    WHILE flag != 1
        DO
            -- targetSQL 你想要执行的目标功能，这里可以写多个SQL
            SET var_bed_num = (RAND() * (10 - 5) + 2);
            SET var_bed_index = 0;
            WHILE var_bed_index < var_bed_num
                DO
                    SET var_name = CONCAT(var_bed_index, ' bed');
                    SET var_order_num = var_order_num + 1;
                    SET var_is_valid = IF(rand() < 0.01, 0, 1); -- 床位有1/100的概率为无效
                    SET var_is_del = IF(rand() < 0.01, 1, 0); -- 房间有1/100的概率被删除
                    INSERT INTO beds
                        (room_id, name, order_num, is_valid, is_del)
                    VALUES (var_room_id, var_name, var_order_num, var_is_valid, var_is_del);
                    SET var_bed_index = var_bed_index + 1;
                end while;
            -- 一定要记得把游标向后移一位，这个坑我替各位踩过了，不需要再踩了
            FETCH var_room_id_bed_num_list INTO var_room_id;
            -- 当s等于1时表明遍历以完成，退出循环
        END WHILE;
    -- 关闭游标
    CLOSE var_room_id_bed_num_list;
END;
# CALL func_init_building();
# CALL func_init_room();
call func_init_beds(); --
// -- 结束定义语句
DELIMITER ; -- 重新将分隔符设置为;