package com.rushuni.service;

import com.rushuni.sh.common.entity.CheckItem;
import com.rushuni.sh.common.util.PageResult;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/25
 */
public interface CheckItemService {
    void save(CheckItem checkItem);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void handleDelete(int id);

    void handleUpdate(int id);

    List<CheckItem> selectList();
}
