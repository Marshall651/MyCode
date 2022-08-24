package com.rushuni.sh.provider.mapper;

import com.github.pagehelper.Page;
import com.rushuni.sh.common.entity.CheckGroup;
import com.rushuni.sh.common.entity.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/7/28
 */
public interface SetmealMapper {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<CheckGroup> selectByCondition(String queryString);

    List<Setmeal> findAllSetmeal();

    Setmeal findById(int id);
}
