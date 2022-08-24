package com.rushuni.sh.provider.mapper;

import cn.hutool.core.date.DateTime;
import com.rushuni.sh.common.entity.ReserveSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/3
 */
public interface ReserveSettingMapper {
    /**
     * 查找日期是否存在
     * @param orderDate
     * @return
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 编辑
     * @param orderSetting
     */
    void editNumberByOrderDate(ReserveSetting orderSetting);

    /**
     * 添加
     * @param orderSetting
     */
    void add(ReserveSetting orderSetting);

    /**
     * 查找。根据月份
     * @param map
     * @return
     */
    List<ReserveSetting> getOrderSettingByMonth(Map<String, String> map);

    ReserveSetting selectByDate(DateTime reserveDate);

    void update(ReserveSetting reserveSetting);
}
