package com.ascba.rebate.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jero on 2018/1/24 0024.
 */

public class DrawableChangeUtils {

    public static final int Drawable_LEFT = 0;
    public static final int Drawable_TOP = 1;
    public static final int Drawable_RIGHT = 2;
    public static final int Drawable_BOTTOM = 3;

    @IntDef({Drawable_LEFT, Drawable_TOP, Drawable_RIGHT, Drawable_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawableType {
    }

    public static void setShapeDrawableColor(View view, @ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(color);
    }

    public static void setShapeDrawableColor(TextView view, @DrawableType int type, @ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) view.getCompoundDrawables()[0];
        drawable.setColor(color);
    }

    public static void setChangeCompoundDrawable(TextView view, @DrawableType int type, @Nullable Drawable drawable) {
        Drawable[] drawables = view.getCompoundDrawables();
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //view.setCompoundDrawablesWithIntrinsicBounds (drawable, drawables[1], drawables[2], drawables[3]);
        switch (type) {
            case Drawable_LEFT:
                view.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                break;
            case Drawable_TOP:
                view.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                break;
            case Drawable_RIGHT:
                view.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
                break;
            case Drawable_BOTTOM:
                view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                break;
        }
    }

    public static void setChangeCompoundDrawable(TextView view, @DrawableType int type, @DrawableRes int drawable) {
        setChangeCompoundDrawable(view, type, drawable != 0 ? view.getContext().getResources().getDrawable(drawable) : null);
    }

}
