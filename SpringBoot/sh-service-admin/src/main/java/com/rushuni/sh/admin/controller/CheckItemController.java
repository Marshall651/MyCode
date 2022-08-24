package com.rushuni.sh.admin.controller;

import com.rushuni.service.CheckItemService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.CheckItem;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import com.rushuni.sh.common.util.PageResult;
import com.rushuni.sh.common.util.QueryPageBean;
import com.rushuni.sh.common.util.R;

import java.util.List;

/**
 * @author Marshall
 * @date 2022/7/25
 */
@AllArgsConstructor
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @DubboReference
    private CheckItemService checkItemService;

    @PostMapping("/add")
    public R add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.save(checkItem);
        } catch (Exception e) {
            return new R(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new R(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @DeleteMapping("/delete")
    public R delete(Integer id){
        try{
            checkItemService.handleDelete(id);
        }catch (Exception e){
            e.printStackTrace();
            return new R(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new R(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    @RequestMapping("/findAll")
    public R findAll() {
        List<CheckItem> checkItemList = checkItemService.selectList();
        if (checkItemList != null && checkItemList.size() > 0) {
            R result = new R(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItemList);
            return result;
        }
        return new R(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}
