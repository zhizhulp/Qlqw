package com.ascba.rebate.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AddressAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AddressEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EasyBRVAHEmpty;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jero on 2017/9/19 0019.
 */

public class AddressActivity extends BaseDefaultNetActivity {

    private static final int GET = 85;
    private static final int SET_DEF = 226;
    private static final int DEL = 238;

    private List<AddressEntity> addressList;
    private AddressAdapter addressAdapter;
    private int mPosition;
    private int addressID;
    private int type;
    private AddressEntity backAE;//要带回去的实体类

    @Override
    protected int bindLayout() {
        return R.layout.activity_address;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        addressAdapter = new AddressAdapter();
        mRecyclerView.setAdapter(addressAdapter);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                backAndResults();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (type == 1) {
            addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = getIntent();
                    intent.putExtra("address", addressList.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                mPosition = position;
                switch (view.getId()) {
                    case R.id.item_address_del:
                        dm.showAlertDialog2("确定要删除吗？", null, null, new DialogManager.Callback() {
                            @Override
                            public void handleLeft() {
                                requestAddressDelNetwork(DEL, (AddressEntity) adapter.getItem(position));
                            }
                        });
                        break;
                    case R.id.item_address_edit:
                        Bundle bundle = new Bundle();
                        bundle.putString("address", JSON.toJSONString(adapter.getItem(position)));
                        bundle.putInt("type", AddressAddActivity.EDIT_TYPE);
                        startActivityForResult(AddressAddActivity.class, bundle, AddressAddActivity.EDIT_TYPE);
                        break;
                    case R.id.item_address_check:
                        requestAddressDefaultNetwork(SET_DEF, (AddressEntity) adapter.getItem(position));
                        break;
                }
            }
        });
        View footView = LayoutInflater.from(this).inflate(R.layout.address_foot_item, null);
        addressAdapter.addFooterView(footView);
//        View emptyView = LayoutInflater.from(this).inflate(R.layout.activity_address_empty, null);
//        addressAdapter.setEmptyView(emptyView);
        new EasyBRVAHEmpty(this).bind(addressAdapter, R.layout.activity_address_empty);
//        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                requestAddressNetwork(GET);
            }
        });
        requestAddressNetwork(GET);
    }

    private void backAndResults() {
        Intent intent = getIntent();
        if (addressList == null || addressList.size() == 0) {//没有地址
            Log.d(TAG, "backAndResults: 没有地址");
        } else if (addressList != null && addressList.size() == 1) {//只有一条地址
            Log.d(TAG, "backAndResults: 只有一条地址");
            intent.putExtra("address", addressList.get(0));
        } else {//多条地址
            if (hasDeleteOrg()) {//用户把原来的删掉了
                if (hasDefaultAddress()) {//有默认地址
                    Log.d(TAG, "backAndResults: 用户把原来的删掉了，有默认地址");
                    intent.putExtra("address", backAE);
                } else {
                    Log.d(TAG, "backAndResults: 用户把原来的删掉了，无默认地址");
                    intent.putExtra("address", addressList.get(0));
                }
            } else {
                Log.d(TAG, "backAndResults: 原来的地址没有删掉");
                intent.putExtra("address", backAE);
            }
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean hasDefaultAddress() {
        boolean has = false;
        for (int i = 0; i < addressList.size(); i++) {
            AddressEntity addressEntity = addressList.get(i);
            if (addressEntity.getType() == 1) {
                backAE = addressEntity;
                return true;
            }
        }
        return has;
    }

    private boolean hasDeleteOrg() {
        boolean has = true;
        for (int i = 0; i < addressList.size(); i++) {
            AddressEntity addressEntity = addressList.get(i);
            if (addressEntity.getAddress_id() == addressID) {
                backAE = addressEntity;
                return false;
            }
        }
        return has;
    }

    @Override
    public void onBackPressed() {
        backAndResults();
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            addressID = extras.getInt("address_id");
            type = extras.getInt("type");
        }
    }

    public void requestAddressNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.addressGetList, RequestMethod.GET, null);
        executeNetwork(what, "请稍后", request);
    }

    public void requestAddressDefaultNetwork(int what, AddressEntity addressEntity) {
        AbstractRequest request = buildRequest(UrlUtils.addressSetDefault, RequestMethod.POST, null);
        request.add("member_address_id", addressEntity.getAddress_id());
        executeNetwork(what, "请稍后", request);
    }

    public void requestAddressDelNetwork(int what, AddressEntity addressEntity) {
        AbstractRequest request = buildRequest(UrlUtils.addressDel, RequestMethod.POST, null);
        request.add("member_address_id", addressEntity.getAddress_id());
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case GET:
                try {
                    JSONObject jsonObject = new JSONObject(result.getData().toString());
                    addressList = JSON.parseArray(jsonObject.getString("info"), AddressEntity.class);
                    addressAdapter.setNewData(addressList);
                    setBackAE();
//                    updateTitle();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case DEL:
                addressAdapter.remove(mPosition);
//                updateTitle();
                addressAdapter.notifyDataSetChanged();
                break;
            case SET_DEF:
                int num = 0;
                for (AddressEntity addressEntity : addressAdapter.getData()) {
                    if (num++ == mPosition)
                        addressEntity.setType(1);
                    else
                        addressEntity.setType(0);
                }
                addressAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void setBackAE() {
        if (addressList != null && addressList.size() > 0) {
            for (int i = 0; i < addressList.size(); i++) {
                AddressEntity addressEntity = addressList.get(i);
                if (addressEntity.getAddress_id() == addressID) {
                    backAE = addressEntity;
                }
            }
        }
    }

    private void updateTitle() {
        if (addressAdapter.getData().size() <= 0)
            mMoneyBar.setTextTitle("收货地址");
        else
            mMoneyBar.setTextTitle("管理收货地址");
    }

    public void add(View v) {
        startActivityForResult(AddressAddActivity.class, null, AddressAddActivity.ADD_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AddressAddActivity.ADD_TYPE:
//                    requestAddressNetwork(GET);
                    AddressEntity entity = data.getParcelableExtra("address");
                    if (entity.getType() == 1)
                        for (AddressEntity addressEntity : addressAdapter.getData())
                            addressEntity.setType(0);
                    addressAdapter.addData(entity);
                    addressAdapter.notifyDataSetChanged();
                    break;
                case AddressAddActivity.EDIT_TYPE:
                    requestAddressNetwork(GET);
                    break;
            }
        }
    }
}
