package com.xiala.wx;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

public class TwoLevelTransformer implements ViewPager2.PageTransformer {
    private Toolbar mToolbar;
    private ImageView mListView;
    private ViewPager2 mViewPager;
    private boolean fromFloorPage = true;
    private ExpendPointView mExpendPointView;
    /**
     * 以下为可调参数，请根据实际情况进行调整
     **/
    //顶页可见部分百分比（其余是底页可见部分）
    private static final float floorPageVisibleOffset = 0.90f;

    TwoLevelTransformer(ViewPager2 viewPager, ExpendPointView expendPointView) {
        this.mViewPager = viewPager;
        this.mExpendPointView = expendPointView;
    }


    @Override
    public void transformPage(@NonNull View page, float position) {
        switch (page.getId()) {
            case 2:
                doInFloorPage(page, position);
                break;
            case 3:
                doInCeilPage(page, position);
                break;
        }
        ViewCompat.setElevation(page, mViewPager.getOffscreenPageLimit() - position);
    }

    /**
     * 实现对底部页面的操作
     *
     * @param page     页面对象
     * @param position 位移量
     */
    private void doInFloorPage(@NonNull View page, float position) {
        int drawableID;
        mToolbar = page.findViewById(R.id.toolbar);
        mListView = page.findViewById(R.id.mainListView);

        float offset;  //设置底部偏移（在大于某值时不移动，使底页部分露出）
        if (position > -floorPageVisibleOffset) {
            offset = 0;
        } else {
            offset = page.getHeight() * (Math.abs(position) - floorPageVisibleOffset);
        }
        page.setTranslationY(offset);

        float alpha;  //设置底页的透明度（三个点完全展开后，开始渐变）
        if (Math.abs(position) < 0.20f) {
            alpha = 1f;
        } else {
            alpha = Math.max(mExpendPointView.getBackgroundAlpha() / 255f, 0.5f);
        }
        mToolbar.setAlpha(alpha);
        mListView.setAlpha(alpha);

        //底页到达底端后变成圆角
        if (Math.abs(position) > 0.9f) {
            drawableID = R.drawable.toolbar_bg_round_corner;
        } else {
            drawableID = R.drawable.toolbar_bg_rect_corner;
        }

        mToolbar.setBackground(ContextCompat.getDrawable(mViewPager.getContext(), drawableID));
    }


    /**
     * 实现对顶部页面的操作
     *
     * @param page     页面对象
     * @param position 位移量
     */
    private void doInCeilPage(@NonNull View page, float position) {
        if (getFromFloorPage()) {
            //设置顶页缩放动画，以及透明度渐变
            float translationY = (page.getHeight() - 300) * position;
            page.setTranslationY(-translationY);
            float scaleFactor = Math.min(1f - position * 0.25f, 1f);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }
        page.setAlpha(1f - Math.abs(position));
    }

    /**
     * 设置起始滑动页面的标志
     *
     * @param fromFloorPage 起始页面标志
     *                      true：从底页开始滑动
     *                      false：从顶页开始滑动
     */
    public void setFromFloorPage(boolean fromFloorPage) {
        this.fromFloorPage = fromFloorPage;
    }

    /**
     * 设置起始滑动页面的标志
     *
     * @return 起始页面标志
     */
    public boolean getFromFloorPage() {
        return this.fromFloorPage;
    }
}