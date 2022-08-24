package com.rushuni.sh.common.constant;

/**
 * @author Marshall
 * @date 2022/8/9
 */
public interface RedisConstant {
    /**
     * 套餐图片 所有图片名称（保存在redis）
     */
    String SETMEAL_PIC_KEY = "sh:setmeal:Pic:set";
    /**
     * 套餐图片 需要的图片名称（代表保存到数据库）
     */
    String SETMEAL_PIC_DB_KEY = "sh:setmeal:Pic.Db:set";
    /**
     * 手机号及其对应的验证码 存入redis（5分钟）
     */
    String RESERVE_SMS_KEY = "sh:reserve:sms.code:set";

    String LOGIN_SMS_KEY = "sh:login:sms.code:str";

    String LOGIN_MEMBER_KEY = "sh:member:login.code:json";
}
