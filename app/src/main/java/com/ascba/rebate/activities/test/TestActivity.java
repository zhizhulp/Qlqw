package com.ascba.rebate.activities.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.utils.EncodeUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by 李平 on 2017/9/12 16:01
 * Describe:仅供测试
 */

public class TestActivity extends AppCompatActivity {
    private String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private String TAG="TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void goPay(View view) {
        final StringRequest request = new StringRequest(url, RequestMethod.POST);
        String xml = createXml(getSign("qlqwshopqlqw1234qlqwshopqlqw1234"));
        Log.d(TAG, "goPay: "+xml);
        request.setDefineRequestBodyForXML(xml);
        CallServer.getInstance().addStringRequest(0,request,new OnResponseListener<String>(){

            @Override
            public void onStart(int what) {
                Log.d(TAG, "onStart: ");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.d(TAG, "onSucceed: "+response.get());
                parseXML(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.d(TAG, "onFailed: "+response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                Log.d(TAG, "onFinish: ");
            }
        });
    }

    private static TreeMap<String, String> createMap() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("appid", "wxbc93c33327923c2c");
        map.put("body", "商品描述");
        map.put("mch_id", "1489331592");
        map.put("nonce_str", "ibuaiVcKdpRxkhJA");
        map.put("notify_url", "http://www.baidu.com");
        map.put("out_trade_no", "2018010816280102");
        map.put("spbill_create_ip", "192.168.50.176");
        map.put("total_fee", "1");
        map.put("trade_type", "APP");
        return map;
    }

    private static TreeMap<String, String> getSign(String key) {
        TreeMap<String, String> map = createMap();
        StringBuilder sb = new StringBuilder();
        for (String keys : map.keySet()) {
            sb.append(keys);
            sb.append("=");
            sb.append(map.get(keys));
            sb.append("&");
        }
        sb.append("key=").append(key);
        String sign = EncodeUtils.MD5(sb.toString()).toUpperCase();
        map.put("sign",sign);
        return map;
    }


    public String createXml(TreeMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("\n");
        for (String key : map.keySet()) {
            sb.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">").append("\n");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public void parseXML(final String xml) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                XmlPullParser parser = Xml.newPullParser();
                ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
                final TreeMap<String, String> map = new TreeMap<>();
                try {
                    parser.setInput(stream, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                                break;
                            case XmlPullParser.START_TAG://开始读取某个标签
                                //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值
                                String name = parser.getName();
                                if (!"xml".equals(name)) {
                                    map.put(name, parser.nextText());
                                }
                                break;
                            case XmlPullParser.END_TAG:// 结束元素事件
                                //读完一个Person，可以将其添加到集合类中
                                break;
                        }
                        eventType = parser.next();
                    }
                    stream.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayReq req = new PayReq();
                            req.appId = map.get("appid");
                            req.nonceStr = map.get("nonce_str");
                            req.packageValue = "Sign=WXPay";
                            req.partnerId = map.get("mch_id");
                            req.prepayId = map.get("prepay_id");
                            req.timeStamp = System.currentTimeMillis()/1000+"";
                            HashMap<String,String> map1=new HashMap<>();
                            map1.put("appid", map.get("appid"));
                            map1.put("noncestr", map.get("nonce_str"));
                            map1.put("package", "Sign=WXPay");
                            map1.put("partnerid", map.get("mch_id"));
                            map1.put("prepayid", map.get("prepay_id"));
                            map1.put("timestamp", System.currentTimeMillis()/1000+"");
                            req.sign = getSign1(map1,"qlqwshopqlqw1234qlqwshopqlqw1234)");
                            IWXAPI iwxapi = WXAPIFactory.createWXAPI(TestActivity.this, null);
                            iwxapi.registerApp(map.get("appid"));
                            iwxapi.sendReq(req);
                            AppConfig.getInstance().putString("app_id",map.get("appid"));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String getSign1(HashMap<String,String> map ,String key) {
        StringBuilder sb = new StringBuilder();
        for (String keys : map.keySet()) {
            sb.append(keys);
            sb.append("=");
            sb.append(map.get(keys));
            sb.append("&");
        }
        //sb.append("key=").append(key);
        String ori = sb.toString();
        Log.d("sign", "getSign1: "+ori);
        String subString= ori.substring(0,ori.length()-1);
        Log.d("sign", "getSign1: "+subString);
        return subString.toUpperCase();
    }
}
