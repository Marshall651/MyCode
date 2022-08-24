package com.rushuni.sh.provider.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.Member;
import com.rushuni.sh.common.entity.Reserve;
import com.rushuni.sh.common.entity.ReserveSetting;
import com.rushuni.sh.common.util.R;
import com.rushuni.sh.provider.mapper.MemberMapper;
import com.rushuni.sh.provider.mapper.ReserveMapper;
import com.rushuni.sh.provider.mapper.ReserveSettingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import com.rushuni.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/9
 */
@DubboService(interfaceClass = ReserveService.class)
@Transactional
public class ReserveServiceImpl implements ReserveService {
    @Autowired
    private ReserveSettingMapper reserveSettingMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ReserveMapper reserveMapper;

    @Override
    public R reserveProcess(Map map) {
        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String reserveDateStr = (String) map.get("orderDate");
        // Hutool转换为日期类型
        DateTime reserveDate = DateUtil.parse(reserveDateStr, "yyyy-MM-dd");
        ReserveSetting reserveSetting = reserveSettingMapper.selectByDate(reserveDate);
        if (ObjectUtil.isNull(reserveSetting)) {
            // 指定日期没有进行预约设置，无法完成体检预约
            return new R(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        // 可预约人数
        int number = reserveSetting.getNumber();
        // 已预约人数
        int reservations = reserveSetting.getReservations();
        if (reservations >= number) {
            // 已经约满，无法预约
            return new R(false, MessageConstant.ORDER_FULL);
        }

        // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        // 获取用户页面输入的手机号
        String telephone = (String) map.get("telephone");
        // 通过手机号码查找会员信息
        Member member = memberMapper.findByPhone(telephone);
        System.out.println(map);
        // 判断是否在重复预约逻辑
        if (ObjectUtil.isNotNull(member)) {
            Reserve reserve = new Reserve();
            reserve.setMemberId(member.getId());
            reserve.setOrderDate(reserveDate);
            reserve.setSetmealId(Integer.valueOf(ObjectUtil.toString(map.get("setmealId"))));
            // 根据条件进行查询
            List<Reserve> list = reserveMapper.selectList(reserve);
            if (list != null && list.size() > 0) {
                // 说明用户在重复预约，无法完成再次预约
                return new R(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            // 4、当前用户如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberMapper.insert(member);
        }
        // 5、预约成功，更新当日的已预约人数
        Reserve reserve = new Reserve();
        reserve.setMemberId(member.getId());
        reserve.setOrderDate(reserveDate);
        reserve.setOrderType((String) map.get("reserveType"));
        reserve.setOrderStatus(Reserve.ORDERSTATUS_NO);
        reserve.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        reserveMapper.insert(reserve);
        // 设置已预约人数+1
        reserveSetting.setReservations(reserveSetting.getReservations() + 1);
        reserveSettingMapper.update(reserveSetting);
        return new R(true, MessageConstant.ORDER_SUCCESS, reserve.getId());
    }

    //根据预约ID查询预约相关信息（体检人姓名、预约日期、套餐名称、预约类型）
    @Override
    public Map getById(Integer id) {
        Map map = reserveMapper.selectOne(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtil.format(orderDate, "yyyy-MM-dd"));
        }
        return map;

    }
}
