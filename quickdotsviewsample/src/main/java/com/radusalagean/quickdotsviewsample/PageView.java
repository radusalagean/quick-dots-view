package com.radusalagean.quickdotsviewsample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PageView extends FrameLayout {

    private TextView pageNumberTextView;
    private int pageNumber;

    public static PageView createPage(Context context, int pageNumber) {
        PageView pageView = new PageView(context);
        pageView.setPageNumber(pageNumber);
        return pageView;
    }

    public PageView(@NonNull Context context) {
        super(context);
        init();
    }

    public PageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_page, this);
        pageNumberTextView = findViewById(R.id.text_view_page_number);
        refreshUi();
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        refreshUi();
    }

    private void refreshUi() {
        if (pageNumberTextView != null) {
            pageNumberTextView.setText(getResources().getString(R.string.page_number, pageNumber));
        }
    }
}
