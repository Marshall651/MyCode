package com.rushuni.sh.provider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rushuni.service.CheckGroupService;
import com.rushuni.sh.common.entity.CheckGroup;
import com.rushuni.sh.provider.mapper.CheckGroupMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.rushuni.sh.common.util.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/7/26
 */
@DubboService
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;

    /**
     * 添加检查组合，同时需要设置检查组合和检查项的关联关系
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void save(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.insert(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }


    /**
     * 设置检查组合和检查项的关联关系
     *
     * @param checkGroupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        checkGroupMapper.update(checkGroup);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<CheckGroup> selectList() {
        return checkGroupMapper.selectList();
    }

    @Override
    public void delete(Integer id) {
        checkGroupMapper.delete(id);
    }
}
