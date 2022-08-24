package com.rushuni.sh.admin.controller;

import com.rushuni.service.SetmealService;
import com.rushuni.sh.common.constant.MessageConstant;
import com.rushuni.sh.common.entity.Setmeal;
import com.rushuni.sh.common.util.*;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Marshall
 * @date 2022/7/28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/setmeal")
public class SetmealController {
    @DubboReference
    private SetmealService setmealService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/upload")
    public R upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功
            R result = new R(true, MessageConstant.PIC_UPLOAD_SUCCESS);
            redisTemplate.opsForSet().add(RedisUtils.SETMEAL_PIC_RESOURCES,fileName);
            result.setData(fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public R add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            return new R(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new R(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.pageQuery(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }
}
