package com.rushuni.sh.provider.mapper;

import com.rushuni.sh.common.entity.Member;

/**
 * @author Marshall
 * @date 2022/8/9
 */
public interface MemberMapper {
    Member findByPhone(String telephone);

    void insert(Member member);
}
