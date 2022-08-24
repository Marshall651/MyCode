package com.rushuni.sh.provider.mapper;

import com.github.pagehelper.Page;
import com.rushuni.sh.common.entity.CheckGroup;
import com.rushuni.sh.common.entity.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/7/26
 */
public interface CheckGroupMapper {
    void insert(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void update(CheckGroup checkGroup);

    Page<CheckGroup> selectByCondition(String queryString);

    List<CheckGroup> selectList();

    void delete(Integer id);

    CheckGroup findCheckGroupById(Integer id);
}
