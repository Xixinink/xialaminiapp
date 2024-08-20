package com.xiala.wx;

import android.content.Context;

public class DimensionUtil {

    /**
     * 将dip或dp单位转换为像素单位
     *
     * @param context 上下文
     * @param dpValue 密度独立像素值
     * @return 转换后的像素值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素单位转换为密度独立像素单位
     *
     * @param context 上下文
     * @param pxValue 像素值
     * @return 转换后的密度独立像素值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp单位转换为像素单位
     *
     * @param context 上下文
     * @param spValue 缩放独立像素值
     * @return 转换后的像素值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将像素单位转换为缩放独立像素单位
     *
     * @param context 上下文
     * @param pxValue 像素值
     * @return 转换后的缩放独立像素值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}