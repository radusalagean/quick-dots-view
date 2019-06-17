package com.radusalagean.quickdotsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A configurable view that displays a number of dots (represented by {@link #count}) and a selected
 * dot in a different color (represented by {@link #currentDot}).<br>
 * <br>
 * A common use case is displaying the current page in a {@link ViewPager}. This can be easily done
 * by hooking up the {@link QuickDotsView} with the {@link ViewPager} through the
 * {@link #linkViewPager(ViewPager)} method.<br>
 * <br>
 * The view has a number of properties that can be configured through XML attributes or through
 * calling the appropriate setter method on the property. Every property has a default value that
 * will be used if that property is not defined by the user.<br>
 * <br>
 * Properties:<br>
 * - {@link #count} - The total number of dots to be displayed (value must be equal or higher than
 * {@link #MINIMUM_DOT_COUNT}) <br>
 * - {@link #selectedDotColor} - An integer representation of the color to be used for the selected
 * dot <br>
 * - {@link #unselectedDotColor} - An integer representation of the color to be used for the
 * unselected dots <br>
 * - {@link #dotRadius} - The radius size in pixels of the dot <br>
 * - {@link #dotSeparation} - The distance in pixels between dots <br>
 * - {@link #currentDot} - The 0-based position of the currently selected dot <br>
 * <br>
 * Note for usage with {@link ViewPager}:<br>
 * The {@link #count} and {@link #currentDot} are handled automatically if the
 * {@link #linkViewPager(ViewPager)} method is used.
 */
public class QuickDotsView extends View {

    public static final int MINIMUM_DOT_COUNT = 1;
    private static final int DEFAULT_COUNT = MINIMUM_DOT_COUNT;
    private static final int DEFAULT_SELECTED_DOT_COLOR_RES = R.color.quick_dots_view_default_selected_dot_color;
    private static final int DEFAULT_UNSELECTED_DOT_COLOR_RES = R.color.quick_dots_view_default_unselected_dot_color;
    private static final int DEFAULT_DOT_RADIUS_RES = R.dimen.quick_dots_view_default_dot_radius;
    private static final int DEFAULT_DOT_SEPARATION_RES = R.dimen.quick_dots_view_default_dot_separation;
    private static final int DEFAULT_CURRENT_DOT = 0;

    private int count;
    private @ColorInt int selectedDotColor;
    private @ColorInt int unselectedDotColor;
    private int dotRadius;
    private int dotSeparation;
    private int currentDot;
    private Paint selectedDotPaint;
    private Paint unselectedDotPaint;

    private ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            setCurrentDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    private ViewPager.OnAdapterChangeListener onAdapterChangeListener =
            new ViewPager.OnAdapterChangeListener() {
        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager,
                                     @Nullable PagerAdapter oldAdapter,
                                     @Nullable PagerAdapter newAdapter) {
            setUpPagerAdapter(newAdapter);
        }
    };

    public QuickDotsView(Context context) {
        super(context);
        init(null);
    }

    public QuickDotsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public QuickDotsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(@Nullable AttributeSet attrs) {
        count = DEFAULT_COUNT;
        selectedDotColor = ResourcesCompat
                .getColor(getResources(), DEFAULT_SELECTED_DOT_COLOR_RES, null);
        unselectedDotColor = ResourcesCompat
                .getColor(getResources(), DEFAULT_UNSELECTED_DOT_COLOR_RES, null);
        dotRadius = getResources().getDimensionPixelSize(DEFAULT_DOT_RADIUS_RES);
        dotSeparation = getResources().getDimensionPixelSize(DEFAULT_DOT_SEPARATION_RES);
        currentDot = DEFAULT_CURRENT_DOT;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.QuickDotsView, 0, 0
            );
            count = a.getInt(R.styleable.QuickDotsView_count, DEFAULT_COUNT);
            selectedDotColor = a.getColor(R.styleable.QuickDotsView_selectedDotColor,
                    ResourcesCompat.getColor(getResources(), DEFAULT_SELECTED_DOT_COLOR_RES, null));
            unselectedDotColor = a.getColor(R.styleable.QuickDotsView_unselectedDotColor,
                    ResourcesCompat.getColor(getResources(), DEFAULT_UNSELECTED_DOT_COLOR_RES, null));
            dotRadius = a.getDimensionPixelSize(R.styleable.QuickDotsView_dotRadius,
                    getResources().getDimensionPixelSize(DEFAULT_DOT_RADIUS_RES));
            dotSeparation = a.getDimensionPixelSize(R.styleable.QuickDotsView_dotSeparation,
                    getResources().getDimensionPixelSize(DEFAULT_DOT_SEPARATION_RES));
            currentDot = a.getInt(R.styleable.QuickDotsView_currentDot, DEFAULT_CURRENT_DOT);
            a.recycle();
        }
        selectedDotPaint = buildPaint(selectedDotColor);
        unselectedDotPaint = buildPaint(unselectedDotColor);
        assertSpecs();
    }

    private Paint buildPaint(@ColorInt int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        return paint;
    }

    private float[] getCircleCoordinates(int index) {
        float x = (float) dotRadius + index * (dotSeparation + 2 * dotRadius);
        float y = (float) dotRadius;
        return new float[] { x, y };
    }

    /**
     * Link a {@link ViewPager} in order to automatically handle the {@link #count} and
     * {@link #currentDot} properties
     *
     * @param viewPager The view pager that should be linked
     */
    public void linkViewPager(@NonNull ViewPager viewPager) {
        setUpPagerAdapter(viewPager.getAdapter());
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.addOnAdapterChangeListener(onAdapterChangeListener);
    }

    /**
     * Unlink a {@link ViewPager}
     *
     * @param viewPager That ViewPager that should be linked
     */
    public void unlinkViewPager(@NonNull ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener);
        viewPager.removeOnAdapterChangeListener(onAdapterChangeListener);
    }

    private void setUpPagerAdapter(@NonNull PagerAdapter pagerAdapter) {
        setCount(pagerAdapter.getCount());
        setCurrentDot(0);
    }

    private void assertSpecs() {
        if (count < MINIMUM_DOT_COUNT) {
            throw new IllegalArgumentException("Passed dot count (" + count + ") is lower than " +
                    "the minimum count (" + MINIMUM_DOT_COUNT + ")");
        }
        if (dotRadius < 0) {
            throw new IllegalArgumentException("Passed dot radius (" + dotRadius + ") is " +
                    "negative");
        }
        if (dotSeparation < 0) {
            throw new IllegalArgumentException("Passed dot separation (" + dotSeparation + ") is " +
                    "negative");
        }
    }

    /**
     * Set the total number of dots (value must be equal or higher than
     *  * {@link #MINIMUM_DOT_COUNT})
     *
     * @param count total number of dots
     */
    public void setCount(int count) {
        this.count = count;
        assertSpecs();
        requestLayout();
        invalidate();
    }

    /**
     * Set the integer representation of the color to be used for the selected dot
     *
     * @param selectedDotColor selected dot color
     */
    public void setSelectedDotColor(@ColorInt int selectedDotColor) {
        this.selectedDotColor = selectedDotColor;
        selectedDotPaint = buildPaint(selectedDotColor);
        invalidate();
    }

    /**
     * Set the integer representation of the color to be used for the unselected dot
     *
     * @param unselectedDotColor unselected dot color
     */
    public void setUnselectedDotColor(@ColorInt int unselectedDotColor) {
        this.unselectedDotColor = unselectedDotColor;
        unselectedDotPaint = buildPaint(unselectedDotColor);
        invalidate();
    }

    /**
     * Set the radius size in pixels of the dot
     *
     * @param dotRadius dot radius in pixels
     */
    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
        assertSpecs();
        requestLayout();
        invalidate();
    }

    /**
     * Set the distance in pixels between dots
     *
     * @param dotSeparation dot separation in pixels
     */
    public void setDotSeparation(int dotSeparation) {
        this.dotSeparation = dotSeparation;
        assertSpecs();
        requestLayout();
        invalidate();
    }

    /**
     * Set the 0-based position of the currently selected dot
     *
     * @param index the index for the current dot
     */
    public void setCurrentDot(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("You selected a position that is out " +
                    "of the current bounds (selected: " + index + ", bounds: [0, " + count + ")");
        }
        currentDot = index;
        invalidate();
    }

    /**
     * Get the total number of dots
     *
     * @return total number of dots
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the selected dot color
     *
     * @return selected dot color
     */
    public int getSelectedDotColor() {
        return selectedDotColor;
    }

    /**
     * Get the unselected dot color
     *
     * @return unselected dot color
     */
    public int getUnselectedDotColor() {
        return unselectedDotColor;
    }

    /**
     * Get the dot radius in pixels
     *
     * @return dot radius in pixels
     */
    public int getDotRadius() {
        return dotRadius;
    }

    /**
     * Get the distance between dots in pixels
     *
     * @return distance between dots in pixels
     */
    public int getDotSeparation() {
        return dotSeparation;
    }

    /**
     * Get the 0-based position of the currently selected dot
     *
     * @return the index for the current dot
     */
    public int getCurrentDot() {
        return currentDot;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            float[] coordinates = getCircleCoordinates(i);
            canvas.drawCircle(
                    coordinates[0],
                    coordinates[1],
                    dotRadius,
                    currentDot == i ? selectedDotPaint : unselectedDotPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight();
        for (int i = 0; i < count; i++) {
            width += 2 * dotRadius;
            if (i < count - 1) {
                width += dotSeparation;
            }
        }
        int height = getPaddingTop() + 2 * dotRadius + getPaddingBottom();
        setMeasuredDimension(
                resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec)
        );
    }
}
