package com.rushuni.sh.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import com.rushuni.service.ReserveService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.constant.RedisConstant;
import com.rushuni.sh.common.entity.Reserve;
import com.rushuni.sh.common.util.R;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rushuni.sh.common.util.SmsUtils;

import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/reserve")
@AllArgsConstructor
public class ReserveController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @DubboReference
    private ReserveService reserveService;

    @PostMapping("/submit")
    public R submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // 从Redis中获取保存的验证码
        String smsValidKey = SmsUtils.getSmsValidKey(RedisConstant.RESERVE_SMS_KEY, telephone);
        String validateCodeInRedis = redisTemplate.opsForValue().get(smsValidKey);

        // 将用户输入的验证码和Redis中保存的验证码进行比对
        if (ObjectUtil.equal(validateCode, validateCodeInRedis)) {
            map.put("reserveType", Reserve.ORDERTYPE_WEIXIN);
            R result = null;
            try {
                // 通过Dubbo远程调用服务处理在线预约业务
                result = reserveService.reserveProcess(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                // 预约成功，可以为用户发送预约成功的短信
                SmsUtils.sendShortMessage(SmsUtils.TEMPLATE_CODE_2,telephone,(String) map.get("orderDate"));
            }
            return result;
        }else {
            return new R(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @PostMapping("/findById")
    public R reserve(Integer id) {
        try {
            Map map = reserveService.getById(id);
            return new R(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
            return new R(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
