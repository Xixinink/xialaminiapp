package com.xiala.wx;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ExpendPointView extends View {
    private Paint mPaint;
    private Paint mBgPaint;
    private float percent;
    private boolean isExpanded;
    private int BgAlpha = 255;
    /** 以下为可调参数，请根据实际情况进行调整 **/
    private static final float firstPercent = 0.08f;  //开始出现三个圆的页面下拉偏移量
    private static final float secondPercent = 0.21f;  //开始渐变的页面下拉偏移量
    private static final float maxRadius = 170;  //最大的圆半径
    private static final float minRadius = 8;  //最小的圆半径
    private static final float maxDistance = 55;  //两圆之间的最大间距
    private static final float radiusRate = 0.03f;  //中心圆的缩放速度，值越大缩放越快
    private static final float pointAlphaRate = 5f; //圆点渐变速度，值越大渐变越快
    private static final float bgAlphaRate = 1f; //背景色渐变速度，值越大渐变越快

    public ExpendPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //绘制圆点的画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#686868"));
        //绘制背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
//        mBgPaint.setColor(Color.parseColor("#ededed"));
        mBgPaint.setColor(Color.parseColor("#ffffff"));
    }

    /**
     * 设置百分比
     * @param percent 百分比
     */
    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mBgPaint);
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        if (percent > 0 && percent < firstPercent) {
            //绘制中心点，不断变大
            mPaint.setAlpha(255);
            mBgPaint.setAlpha(255);
            float curRadius = percent * maxRadius;
            canvas.drawCircle(centerX, centerY, curRadius, mPaint);
            setExpandStatus(false);
        } else if (percent >= firstPercent && percent < secondPercent) {
            //中心点缩小，并出现三个点，逐渐分离
            mPaint.setAlpha(255);
            mBgPaint.setAlpha(255);
            float rate = (percent - firstPercent) / (secondPercent - firstPercent);
            float curRadius = (firstPercent * maxRadius) - (maxRadius * rate * radiusRate);
            curRadius = Math.max(curRadius, minRadius);  //计算中心圆的半径
            float offsetX = maxDistance * rate;  //计算每两个圆之间的间距
            canvas.drawCircle(centerX, centerY, curRadius, mPaint);
            canvas.drawCircle(centerX - offsetX, centerY, minRadius, mPaint);
            canvas.drawCircle(centerX + offsetX, centerY, minRadius, mPaint);
            setExpandStatus(false);
        } else if (percent >= secondPercent && percent < 1f) {
            //三个点等距，并逐渐透明至消失
            float rate = (percent - secondPercent) / (1f - secondPercent);
            float pointRate = pointAlphaRate * rate;
            float bgRate = bgAlphaRate * (float) Math.pow(rate, 3);
            int pointAlpha = (int) (255 * (1f - percent + secondPercent) * (1f - pointRate));
            BgAlpha = (int) (255 * (1f - percent + secondPercent) * (1f - bgRate));
            pointAlpha = Math.max(pointAlpha, 0);
            BgAlpha = Math.max(BgAlpha, 0);
            mPaint.setAlpha(pointAlpha);
            mBgPaint.setAlpha(BgAlpha);
            canvas.drawCircle(centerX, centerY, minRadius, mPaint);
            canvas.drawCircle(centerX - 54.9f, centerY, minRadius, mPaint);
            canvas.drawCircle(centerX + 54.9f, centerY, minRadius, mPaint);
            setExpandStatus(true);
        }
    }

    /**
     * 获取圆点动画执行的状态
     * @return 状态值
     */
    public boolean getExpandStatus() {
        return this.isExpanded;
    }

    /**
     * 设置圆点动画完全执行的状态
     * @param isExpanded 状态值，true为执行完动画，进入渐变
     */
    public void setExpandStatus(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    /**
     * 获取背景的透明度
     * @return 透明度
     */
    public int getBackgroundAlpha() {
        return this.BgAlpha;
    }
}