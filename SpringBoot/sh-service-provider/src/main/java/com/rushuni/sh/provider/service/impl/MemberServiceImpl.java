package com.rushuni.sh.provider.service.impl;

import com.rushuni.service.MemberService;
import com.rushuni.sh.common.entity.Member;
import com.rushuni.sh.provider.mapper.MemberMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marshall
 * @date 2022/8/10
 */
@DubboService
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member getByTelephone(String telephone) {
        return memberMapper.findByPhone(telephone);
    }

    @Override
    public void save(Member member) {
        memberMapper.insert(member);
    }
}
