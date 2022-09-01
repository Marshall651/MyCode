package com.rushuni.sh.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import com.rushuni.service.MemberService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.constant.RedisConstant;
import com.rushuni.sh.common.entity.Member;
import com.rushuni.sh.common.util.R;
import com.rushuni.sh.common.util.SmsUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Marshall
 * @date 2022/8/10
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> objectTemplate;

    @DubboReference
    private MemberService memberService;

    /**
     * 手机号快速登录
     *
     * @param response
     * @param map
     * @return
     */
    @RequestMapping("/login")
    public R login(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // 从Redis中获取保存的验证码
        String smsValidKey = SmsUtils.getSmsValidKey(RedisConstant.LOGIN_SMS_KEY, telephone);
        String validateCodeInRedis = redisTemplate.opsForValue().get(smsValidKey);

        if (ObjectUtil.equals(validateCode, validateCodeInRedis)) {
            // 验证码输入正确
            // 判断当前用户是否为会员（查询会员表来确定）
            Member member = memberService.getByTelephone(telephone);
            if (ObjectUtil.isNull(member)) {
                member = new Member();
                // 不是会员，自动完成注册（自动将当前用户信息保存到会员表）
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                // 设置初始密码 123
                member.setPassword("123");
                memberService.save(member);
            }
            // 向客户端浏览器写入Cookie，内容为手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            // 将会员信息保存到Redis
            objectTemplate.opsForValue().set(RedisConstant.LOGIN_MEMBER_KEY+telephone,member);
            objectTemplate.expire(RedisConstant.LOGIN_MEMBER_KEY+telephone,7, TimeUnit.DAYS);
            return new R(true, MessageConstant.LOGIN_SUCCESS);
        }else {
            // 验证码输入错误
            return new R(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
