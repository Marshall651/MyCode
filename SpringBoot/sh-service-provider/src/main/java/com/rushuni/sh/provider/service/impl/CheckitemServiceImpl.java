package com.rushuni.sh.provider.service.impl;

/**
 * @author Marshall
 * @date 2022/7/25
 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rushuni.service.CheckItemService;
import com.rushuni.sh.common.entity.CheckItem;
import com.rushuni.sh.provider.mapper.CheckItemMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.rushuni.sh.common.util.PageResult;

import java.util.List;

@DubboService
@Transactional
public class CheckitemServiceImpl implements CheckItemService {
    /**
     * 注入dao层对象
     */
    @Autowired
    private CheckItemMapper checkItemMapper;

    @Override
    public void save(CheckItem checkItem) {
        checkItemMapper.insert(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void handleDelete(int id) {
        checkItemMapper.handleDelete(id);
    }

    @Override
    public void handleUpdate(int id) {
        checkItemMapper.handleUpdate(id);
    }

    @Override
    public List<CheckItem> selectList() {
        return checkItemMapper.selectList();
    }


}
