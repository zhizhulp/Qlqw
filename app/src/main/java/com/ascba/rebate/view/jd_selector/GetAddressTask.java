package com.ascba.rebate.view.jd_selector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class GetAddressTask extends AsyncTask<Void, Void, Address> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private OnResults onResults;

    public GetAddressTask(Context context, OnResults onResults) {
        this.context = context;
        this.onResults = onResults;
    }

    public interface OnResults {
        void onSuccess(Address address);
        void onFailed();
    }

    @Override
    protected Address doInBackground(Void... voids) {
        AssetManager assets = context.getAssets();
        try {
            InputStream is = assets.open("region.json");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer,0,len);
            }
            byte[] bytes = baos.toByteArray();
            String strRe = new String(bytes);
            Gson gson = new Gson();
            is.close();
            baos.close();
            return gson.fromJson(strRe, Address.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);
        if(address!=null){
            onResults.onSuccess(address);
        }else {
            onResults.onFailed();
        }
    }

}
