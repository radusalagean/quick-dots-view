package com.radusalagean.quickdotsviewsample;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    private int pageCount;

    public PagerAdapter(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PageView pageView = PageView.createPage(container.getContext(), position + 1);
        container.addView(pageView);
        return pageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((PageView) object);
    }
}
