package com.rushuni.sh.provider.mapper;

import com.rushuni.sh.common.entity.Reserve;

import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/9
 */
public interface ReserveMapper {
    List<Reserve> selectList(Reserve reserve);

    void insert(Reserve reserve);

    Map selectOne(Integer id);
}
