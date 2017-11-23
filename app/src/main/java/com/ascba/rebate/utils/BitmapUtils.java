package com.ascba.rebate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ascba.rebate.application.MyApplication;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {
    /**
     * 添加logo到二维码图片上
     *
     * @param src  原图
     * @param logo logo
     * @return 新的二维码图片
     */
    public static Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
        if (src == null || logo == null) {
            return src;
        }
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 获取圆角bitmap
     *
     * @param bitmap      原来的bitmap
     * @param roundCorner 倒角
     * @param lineWidth   stroke线宽
     * @return 圆角bitmap
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int roundCorner, int lineWidth) {
        //创建一个和原始图片一样大小位图
        Bitmap roundImage = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //创建带有位图roundImage的画布
        Canvas canvas = new Canvas(roundImage);
        //创建画笔
        Paint paint = new Paint();
        //创建一个和原始图片一样大小的矩形
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        // 去锯齿
        paint.setAntiAlias(true);
        //画一个和原始图片一样大小的圆角矩形
        float dpCorner = ScreenDpiUtils.dp2px(MyApplication.getInstance(), roundCorner);
        canvas.drawRoundRect(rectF, dpCorner, dpCorner, paint);
        //设置相交模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //把图片画到矩形去
        canvas.drawBitmap(bitmap, null, rect, paint);
        //描边
        int dpLW = (int) ScreenDpiUtils.dp2px(MyApplication.getInstance(), lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//空心样式
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dpLW);
        Rect roundRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF roundRectF = new RectF(roundRect);
        canvas.drawRoundRect(roundRectF, dpCorner, dpCorner, paint);
        return roundImage;
    }

    /**
     * 对图片进行压缩的处理
     *
     * @param setWidth  指定图片的宽度
     * @param setHeight 指定图片的高度
     * @return 压缩处理的图片
     * 碰到最多的OOM(Out of Memory) 20M,系统报错,通过该方法的处理可以有效的防止OOM的产生
     */
    public static Bitmap scaleBitmap(byte[] data, int setWidth, int setHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true; //不加载实际内容,只有图片的边框信息
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        int orginWidth = opts.outWidth; //原始的宽度
        int orginHeight = opts.outHeight; //原始的高度
        int scaleWidth = orginWidth / setWidth; // >1才有效果    5
        int scaleHeight = orginHeight / setHeight; // >1才有效果  4
        opts.inSampleSize = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inJustDecodeBounds = false; //要获取图片内容
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap 原始图片
     * @param maxkb  最大容量
     * @return Bitmap
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        byte[] bytes = output.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
