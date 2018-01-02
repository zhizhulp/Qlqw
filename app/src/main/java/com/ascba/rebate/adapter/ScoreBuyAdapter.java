package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.ScoreBuyBanner;
import com.ascba.rebate.bean.ScoreBuyBase;
import com.ascba.rebate.bean.ScoreBuyHead;
import com.ascba.rebate.bean.ScoreBuyMsgP;
import com.ascba.rebate.bean.ScoreBuyType;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.manager.BannerImageLoader;
import com.ascba.rebate.view.MyGridView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;


public class ScoreBuyAdapter extends BaseMultiItemQuickAdapter<ScoreBuyBase, BaseViewHolder> {

    public ScoreBuyAdapter(List<ScoreBuyBase> data) {
        super(data);
        addItemType(0, R.layout.score_buy_banner);
        addItemType(1, R.layout.score_buy_grid);
        addItemType(2, R.layout.score_buy_type);
        addItemType(3, R.layout.gift_goods_item);
        addItemType(4, R.layout.score_buy_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreBuyBase item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case 0://banner
                ScoreBuyBanner itemBanner = (ScoreBuyBanner) item;
                final Banner banner = helper.getView(R.id.banner);
                banner.setImageLoader(new BannerImageLoader());
                final List<ScoreBuyBanner.ScoreBuyImg> imgs = itemBanner.getImgs();
                banner.setImages(imgs).start();
                //click event
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                        ScoreBuyBanner.ScoreBuyImg scoreBuyImg = imgs.get(position);
                        int status = scoreBuyImg.getBanner_status();
                        Log.d(TAG, "OnBannerClick: "+status);
                        if (status == 1) {
                            WebViewBaseActivity.start(mContext, null, scoreBuyImg.getBanner_url());
                        } else if (status == 2) {//跳转原生界面

                        }
                    }
                });
                break;
            case 1://head
                ScoreBuyHead item1 = (ScoreBuyHead) item;
                MyGridView gridView = helper.getView(R.id.gridView);
                gridView.setNumColumns(item1.getGrids().size());
                GridAdapter adapter = new GridAdapter(item1.getGrids(), mContext);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                break;
            case 2://type
                ScoreBuyType item3 = (ScoreBuyType) item;
                GridView gridView1 = helper.getView(R.id.gridView);
                gridView1.setNumColumns(item3.getTypes().size());
                TypeAdapter adapter1 = new TypeAdapter(item3.getTypes(), mContext);
                gridView1.setAdapter(adapter1);
                break;
            case 3://goods
                ScoreHome.GiftGoods itemC = (ScoreHome.GiftGoods) item;
                Picasso.with(mContext).load(itemC.getImg()).placeholder(R.mipmap.gift_goods_loading)
                        .resizeDimen(R.dimen.goods_dimen, R.dimen.goods_dimen)
                        .into((ImageView) helper.getView(R.id.im_icon));
                helper.setText(R.id.tv_title, itemC.getTitle());
                helper.setText(R.id.tv_score, itemC.getPrice());
                TextView view = helper.getView(R.id.tv_money);
                String toMoney = itemC.getTo_money();
                String money = itemC.getMoney();
                if (!TextUtils.isEmpty(toMoney)) {
                    setMoney(view, toMoney);
                }
                if (!TextUtils.isEmpty(money) && !money.equals("0.00")) {
                    view.setText(String.format("+%s元", money));
                    view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                break;
            case 4://msg
                ScoreBuyMsgP item2 = (ScoreBuyMsgP) item;
                ViewFlipper viewFlipper = helper.getView(R.id.viewFlipper);
                viewFlipper.setAutoStart(true);
                List<String> msgs = item2.getMsgs();
                for (int i = 0; i < msgs.size(); i++) {
                    TextView textView = new TextView(mContext);
                    textView.setTextSize(13);
                    textView.setTextColor(Color.parseColor("#999999"));
                    textView.setText(msgs.get(i));
                    viewFlipper.addView(textView);
                }
                break;
        }
    }

    private void setMoney(TextView view, String toMoney) {
        SpannableString ss = new SpannableString("原价 " + toMoney + "元");
        ss.setSpan(new StrikethroughSpan(), 3, ss.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(ss);
    }

    public static class GridAdapter extends BaseAdapter {
        private List<ScoreBuyHead.ScoreBuyGrid> data;
        private Context context;

        public GridAdapter(List<ScoreBuyHead.ScoreBuyGrid> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.score_buy_grid_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ScoreBuyHead.ScoreBuyGrid scoreBuyGrid = data.get(position);
            Picasso.with(context).load(scoreBuyGrid.getUrl()).placeholder(R.mipmap.module_loading).into(viewHolder.im);
            viewHolder.tv.setText(scoreBuyGrid.getTitle());
            return convertView;
        }

        class ViewHolder {
            ViewHolder(View root) {
                im = root.findViewById(R.id.head_im);
                tv = root.findViewById(R.id.head_text);
            }

            ImageView im;
            TextView tv;
        }
    }

    public static class TypeAdapter extends BaseAdapter {
        private List<ScoreBuyType.ScoreBuyTypeC> data;
        private Context context;

        public TypeAdapter(List<ScoreBuyType.ScoreBuyTypeC> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.score_buy_type_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ScoreBuyType.ScoreBuyTypeC scoreBuytype = data.get(position);
            Picasso.with(context).load(scoreBuytype.getCate_img()).placeholder(R.mipmap.module_loading).into(viewHolder.im);
            viewHolder.tv.setText(scoreBuytype.getCate_title());
            return convertView;
        }

        class ViewHolder {
            ViewHolder(View root) {
                im = root.findViewById(R.id.type_im);
                tv = root.findViewById(R.id.type_tv);
            }

            ImageView im;
            TextView tv;
        }
    }
}
