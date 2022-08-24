package com.rushuni.service;

import com.rushuni.sh.common.entity.Member;

/**
 * @author Marshall
 * @date 2022/8/10
 */
public interface MemberService {
    Member getByTelephone(String telephone);

    void save(Member member);
}
