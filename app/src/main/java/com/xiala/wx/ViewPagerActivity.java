package com.xiala.wx;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.gyf.immersionbar.ImmersionBar;

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twolevel);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        final LinearLayout bottomBar = findViewById(R.id.bottomBar);
        final ExpendPointView expandView = findViewById(R.id.headerView);
        final ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        final TwoLevelTransformer transformer = new TwoLevelTransformer(viewPager2, expandView);
        final Animation anim_hide = AnimationUtils.loadAnimation(this, R.anim.anim_bottombar_hide);
        final Animation anim_show = AnimationUtils.loadAnimation(this, R.anim.anim_bottombar_show);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TwoLevelAdapter adapter = new TwoLevelAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new FloorFragment());
        adapter.addFragment(new CeilFragment());
        viewPager2.setAdapter(adapter);

        viewPager2.setRotation(180);
        viewPager2.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (viewPager2.getCurrentItem() == 1 && bottomBar.getVisibility() == View.VISIBLE) {
                        bottomBar.startAnimation(anim_hide);
                        bottomBar.setVisibility(View.GONE);
                    } else if (viewPager2.getCurrentItem() == 0 && bottomBar.getVisibility() == View.GONE) {
                        bottomBar.startAnimation(anim_show);
                        bottomBar.setVisibility(View.VISIBLE);
                        transformer.setFromFloorPage(true);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (position == 1) {
                    transformer.setFromFloorPage(false);
                    WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    wic.setAppearanceLightStatusBars(false);
                } else {
                    WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    wic.setAppearanceLightStatusBars(true);
                }

                lp.height = positionOffsetPixels + (int) DimensionUtil.dip2px(ViewPagerActivity.this, 0);
                expandView.setLayoutParams(lp);
                expandView.setPercent(positionOffset);
            }
        });
    }
}
