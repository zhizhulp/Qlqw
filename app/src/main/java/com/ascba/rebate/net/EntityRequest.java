package com.ascba.rebate.net;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/8/11.
 * NoHttp 实体类请求
 */

public class EntityRequest<T> extends AbstractRequest<T> {
    private Class<T> clazz;

    public EntityRequest(String url, Class<T> clazz) {
        super(url);
        this.clazz =clazz;
    }

    public EntityRequest(String url, RequestMethod requestMethod,Class<T> clazz) {
        super(url, requestMethod);
        this.clazz=clazz;
    }

    @Override
    protected T getResult(String data) {
        return JSON.parseObject(data,clazz);
    }
}
