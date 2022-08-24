package com.rushuni.sh.job.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.rushuni.sh.common.util.QiniuUtils;
import com.rushuni.sh.common.util.RedisUtils;

import java.util.Set;

/**
 * @author Marshall
 * @date 2022/8/2
 */
@Slf4j
public class ClearSetamealPicJob extends QuartzJobBean {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }

    public void clearSetmealImg() {
        log.info("[clearSetmealImg][开始执行清理套餐垃圾图片任务]-[{}]");
        // 根据 Redis 中保存的两个 set 集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = redisTemplate.opsForSet().difference(RedisUtils.SETMEAL_PIC_RESOURCES, RedisUtils.SETMEAL_PIC_DB_RESOURCES);
        if (set!=null) {
            for (String picName:set) {
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //从Redis集合中删除图片名称
                redisTemplate.opsForSet().remove(RedisUtils.SETMEAL_PIC_RESOURCES,picName);
                log.info("[clearSetmealImg][执行清理套餐垃圾图片任务]-[{}]", picName);
            }
        }
        log.info("[clearSetmealImg][结束执行清理套餐垃圾图片任务]-[{}]");
    }
}
