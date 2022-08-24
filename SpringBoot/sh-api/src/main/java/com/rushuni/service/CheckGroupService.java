package com.rushuni.service;

import com.rushuni.sh.common.entity.CheckGroup;
import com.rushuni.sh.common.util.PageResult;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/26
 */
public interface CheckGroupService {
    CheckGroup findById(Integer id);

    void save(CheckGroup checkGroup, Integer[] checkitemIds);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<CheckGroup> selectList();

    void delete(Integer id);
}
