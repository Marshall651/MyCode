package com.rushuni.service;

import com.rushuni.sh.common.entity.Setmeal;
import com.rushuni.sh.common.util.PageResult;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/28
 */
public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAllSetmeal();

    Setmeal findById(int id);
}
