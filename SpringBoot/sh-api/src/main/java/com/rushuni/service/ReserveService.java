package com.rushuni.service;

import com.rushuni.sh.common.util.R;

import java.util.Map;

/**
 * @author Marshall
 * @date 2022/8/9
 */
public interface ReserveService {
    R reserveProcess(Map map);

    Map getById(Integer id);
}
