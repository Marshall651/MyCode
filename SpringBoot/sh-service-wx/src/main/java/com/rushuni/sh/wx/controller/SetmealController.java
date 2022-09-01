package com.rushuni.sh.wx.controller;

import com.rushuni.service.SetmealService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.Setmeal;
import com.rushuni.sh.common.util.R;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/8/5
 */
@RestController
@AllArgsConstructor
@RequestMapping("/setmeal")
public class SetmealController {
    @DubboReference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     *
     * @return
     */
    @RequestMapping("/getAllSetmeal.do")
    public R getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAllSetmeal();
            return new R(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public R findById(int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new R(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
