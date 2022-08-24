package com.rushuni.service;

import com.rushuni.sh.common.entity.ReserveSetting;

import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/3
 */
public interface ReserveSettingService {
    void add(List<ReserveSetting> data);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(ReserveSetting orderSetting);
}
