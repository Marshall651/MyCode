package com.rushuni.sh.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回结果
 */
@Data
public class R implements Serializable {
    /**
     * 执行结果，true为执行成功 false为执行失败
     */
    private boolean flag;
    /**
     * 返回结果信息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

    public R(boolean flag, String message) {
        super();
        this.flag = flag;
        this.message = message;
    }

    public R(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

}
