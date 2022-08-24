package com.rushuni.sh.provider.mapper;

import com.github.pagehelper.Page;
import com.rushuni.sh.common.entity.CheckItem;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/25
 */
public interface CheckItemMapper {
    /**
     * 添加检查项
     * @param checkItem
     */
    void insert(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    void handleDelete(int id);

    void handleUpdate(int id);

    List<CheckItem> selectList();

    CheckItem findCheckItemById(Integer id);
}
