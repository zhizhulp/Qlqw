package com.ascba.rebate.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.bean.Result;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * Created by 李平 on 2017/8/11.
 * NoHttp 请求实体类
 */

public abstract class AbstractRequest<T> extends RestRequest<Result<T>> {

    public AbstractRequest(String url) {
        super(url);
    }

    public AbstractRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }


    // 这个方法由继承的子类去实现，解析成我们真正想要的数据类型。
    protected abstract T getResult(String data);

    @Override
    public Result<T> parseResponse(Headers headers, byte[] body) throws Exception {
        int responseCode = headers.getResponseCode(); // 响应码。
        // 响应码等于200，Http层成功。
        if (responseCode == 200) {
            if (body == null || body.length == 0) {
                // 服务器包体为空。
                return new Result<>(-2017, null, "body为空");
            } else {
                // 这里可以统一打印所有请求的数据哦：
                String bodyString = StringRequest.parseResponseString(headers, body);
                String TAG = "response";
                Log.d(TAG, "parseResponse: " + bodyString);
                JSONObject bodyObject;
                try {
                    bodyObject = JSON.parseObject(bodyString);
                } catch (Exception e) {
                    return new Result<>(-4515, null, bodyString);
                }
                String msg = bodyObject.getString("msg");
                int code = bodyObject.getIntValue("code");
                String data = bodyObject.getString("data");
                Result.VersionUpgrade version = bodyObject.getObject("version_upgrade", Result.VersionUpgrade.class);
                if (code == 200 || code == 404) {
                    JSONObject loginAccess = bodyObject.getJSONObject("login_access");
                    updateStatus(loginAccess);
                }
                if (code == 200 || code == 404) {
                    try {
                        T result = getResult(data);
                        return new Result<>(code, result, msg, version);
                    } catch (Exception e) {
                        return new Result<>(-1545, null, e.getMessage());
                    }
                } else if (code == 10 || code == 11 || code == 20 || code == 21) {
                    return new Result<>(code, null, msg, version);
                } else {
                    return new Result<>(code, null, "没有和服务端约定的状态");
                }

            }
        } else {
            return new Result<>(-7102, null, "服务器异常");
        }
    }

    private void updateStatus(JSONObject infoObj) {
        if (infoObj != null) {
            String sessionId = infoObj.getString("sessionId");
            String storeId = AppConfig.getInstance().getString("session_id", null);
            if (sessionId != null) {
                if (!sessionId.equals(storeId)) {
                    AppConfig.getInstance().putString("session_id", sessionId);
                }
            }
        }
    }

}
