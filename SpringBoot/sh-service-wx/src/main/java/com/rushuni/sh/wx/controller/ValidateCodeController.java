package com.rushuni.sh.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.constant.RedisConstant;
import com.rushuni.sh.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rushuni.sh.common.util.SmsUtils;

import java.util.concurrent.TimeUnit;


/**
 * @author Marshall
 * @date 2022/8/9
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/orderCode")
    public R getOrderCode(String telephone) {
        //随机验证码：4位
        Integer validateCode = RandomUtil.randomInt(1000,10000);
        System.out.println(validateCode);
        // 给用户发验证码
        String sendStatus = SmsUtils.sendShortMessage(SmsUtils.TEMPLATE_CODE_1,telephone,validateCode.toString());
        if (ObjectUtil.equal(sendStatus,"OK")) {
            String smsValidkey = SmsUtils.getSmsValidKey(RedisConstant.RESERVE_SMS_KEY,telephone);
            redisTemplate.opsForValue().set(smsValidkey,String.valueOf(validateCode));
            redisTemplate.expire(smsValidkey,5, TimeUnit.MINUTES);
            return new R(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }else {
            return new R(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @PostMapping("/sendLogin")
    public R sendLogin(String telephone) {
        //随机验证码：4位
        Integer validateCode = RandomUtil.randomInt(1000,10000);
        //给用户发验证码
        String sendStatus = SmsUtils.sendShortMessage(SmsUtils.TEMPLATE_CODE_1,telephone,validateCode.toString());
        if (ObjectUtil.equals(sendStatus,"OK")) {
            // 在redis中，将验证码保存5分钟
            String smsValidkey = SmsUtils.getSmsValidKey(RedisConstant.RESERVE_SMS_KEY,telephone);
            redisTemplate.opsForValue().set(smsValidkey,String.valueOf(validateCode));
            redisTemplate.expire(smsValidkey,5, TimeUnit.MINUTES);
            return new R(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }else {
            return new R(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
