package com.ascba.rebate.net;

import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/8/17.
 * NoHttp string请求
 */

public class StringRequest extends AbstractRequest<String> {
    public StringRequest(String url) {
        super(url);
    }

    public StringRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    protected String getResult(String data) {
        return data;
    }
}
