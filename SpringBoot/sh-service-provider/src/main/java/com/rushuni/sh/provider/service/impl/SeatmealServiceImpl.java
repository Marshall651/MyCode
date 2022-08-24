package com.rushuni.sh.provider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rushuni.service.SetmealService;
import com.rushuni.sh.common.entity.CheckGroup;
import com.rushuni.sh.common.entity.Setmeal;
import com.rushuni.sh.provider.mapper.SetmealMapper;
import freemarker.template.Configuration;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.rushuni.sh.common.util.PageResult;
import com.rushuni.sh.common.util.RedisUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 * @date 2022/7/28
 */
@DubboService
@Transactional
public class SeatmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 从属性文件中读取要生成的html对应的目录
     */
    @Value("${out-put-path}")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealMapper.add(setmeal);
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
            //将图片名称保存到Redis集合中
            String fileName = setmeal.getImg();
            redisTemplate.opsForValue().set(RedisUtils.SETMEAL_PIC_DB_RESOURCES, fileName);
            generateMobileStaticHtml();
        }
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = setmealMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }


    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealMapper.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public List<Setmeal> findAllSetmeal() {
        return setmealMapper.findAllSetmeal();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealMapper.findById(id);
    }

    /**
     * 生成当前方法所需的静态页面
     */
    public void generateMobileStaticHtml() {
        // 在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealMapper.findAllSetmeal();
        // 需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        // 需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }


    /**
     * 通用的方法，用于生成静态页面
     *
     * @param templateName
     * @param htmlPageName
     * @param map
     */
    public void generteHtml(String templateName, String htmlPageName, Map map) {
        // 获得 FreeMarker 配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            //输出文件
            template.process(map, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成套餐列表静态页面
     *
     * @param list
     */
    private void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map map = new HashMap();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList", list);
        generteHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    /**
     * 生成套餐详情静态页面（可能有多个）
     *
     * @param list
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal", setmealMapper.findById(setmeal.getId()));
            generteHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }
}
