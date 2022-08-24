package com.rushuni.sh.admin.controller;

import com.rushuni.service.CheckGroupService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.CheckGroup;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rushuni.sh.common.util.PageResult;
import com.rushuni.sh.common.util.QueryPageBean;
import com.rushuni.sh.common.util.R;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/26
 */
@RestController
@RequestMapping("/checkgroup")
@AllArgsConstructor
public class CheckGroupController {
    @DubboReference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public R add(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        try {
            checkGroupService.save(checkGroup, checkitemIds);
            return new R(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public R findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if(checkGroup != null){
            R result = new R(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }
        return new R(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public R findCheckItemIdsByCheckGroupId(Integer id) {
        try {
            List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new R(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e) {
            e.printStackTrace();
            return new R(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/edit")
    public R edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            checkGroupService.update(checkGroup,checkitemIds);
        }catch (Exception e) {
            return new R(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new R(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/findAll")
    public R findAll() {
        List<CheckGroup> checkGroupList = checkGroupService.selectList();
        if (checkGroupList != null && checkGroupList.size() > 0) {
            R result = new R(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroupList);
            return result;
        }
        return new R(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/delete")
    public R delete(Integer id) {
        try {
            checkGroupService.delete(id);
        }catch (Exception e){
            return new R(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new R(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
