package com.rushuni.sh.admin.controller;


import com.rushuni.service.ReserveSettingService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.ReserveSetting;
import com.rushuni.sh.common.util.PoiUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rushuni.sh.common.util.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/3
 */

@RestController
@RequestMapping("/ordersetting")
public class ReserveSettingController {
    @DubboReference
    private ReserveSettingService reserveSettingService;

    @RequestMapping("/upload")
    public R upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            // 使用POI解析表格数据
            List<String[]> list = PoiUtils.readExcel(excelFile);
            List<ReserveSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                System.out.println(strings);
                String orderDate = strings[0];
                String number = strings[1];
                ReserveSetting orderSetting = new ReserveSetting(new Date(orderDate), Integer.parseInt(number));
                data.add(orderSetting);
            }
            // 通过 dubbo 远程调用服务实现数据批量导入到数据库(data);
            return new R(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new R(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public R getOrderSettingByMonth(String date) {
        try {
            List<Map> list = reserveSettingService.getOrderSettingByMonth(date);
            return new R(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public R editNumberByDate(@RequestBody ReserveSetting orderSetting) {
        try {
            reserveSettingService.editNumberByDate(orderSetting);
            return new R(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return new R(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
