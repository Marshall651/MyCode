package com.rushuni.sh.provider.service.impl;


import com.rushuni.service.ReserveSettingService;
import com.rushuni.sh.common.entity.ReserveSetting;
import com.rushuni.sh.provider.mapper.ReserveSettingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/3
 */
@DubboService
public class ReserveSettingServiceImpl implements ReserveSettingService {
    @Autowired
    private ReserveSettingMapper reserveSettingMapper;

    @Override
    public void add(List<ReserveSetting> ordersettingList) {
        if (ordersettingList != null && ordersettingList.size() > 0) {
            for (ReserveSetting orderSetting : ordersettingList) {
                // 判断当前日期是否已经进行了预约设置
                long countByOrderDate = reserveSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    // 已经进行了预约设置，执行更新操作
                    reserveSettingMapper.editNumberByOrderDate(orderSetting);
                } else {
                    // 没有进行预约设置，执行插入操作
                    reserveSettingMapper.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        // 调用DAO，根据日期范围查询预约设置数据
        List<ReserveSetting> list = reserveSettingMapper.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (ReserveSetting orderSetting : list) {
                Map<String, Object> m = new HashMap<>();
                // 获取日期数字（几号）
                m.put("date", orderSetting.getOrderDate().getDate());
                m.put("number", orderSetting.getNumber());
                m.put("reservations", orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public void editNumberByDate(ReserveSetting orderSetting) {
        long count = reserveSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            reserveSettingMapper.editNumberByOrderDate(orderSetting);
        } else {
            reserveSettingMapper.add(orderSetting);
        }
    }
}
