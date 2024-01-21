package com.justice.insight.guide.pageviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.justice.insight.R;
import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

public class PageIndicator extends FrameLayout {
  private List<ImageView> strokeDots;
  private ImageView dotIndicatorView;
  private View dotIndicatorLayout;

  // Attributes
  private int dotsSize;
  private int dotsSpacing;
  private int dotsStrokeWidth;
  private int dotsCornerRadius;
  private int dotIndicatorColor;
  private int dotsStrokeColor;

  private int horizontalMargin;
  private SpringAnimation dotIndicatorXSpring;
  private SpringAnimation dotIndicatorWidthSpring;
  private LinearLayout strokeDotsLinearLayout;

  private boolean dotsClickable;
  private int count;

  public PageIndicator(Context context) {
    this(context, null);
  }

  public PageIndicator(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    strokeDots = new ArrayList<>();
    strokeDotsLinearLayout = new LinearLayout(context);
    LayoutParams linearParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    horizontalMargin = dpToPx(24);
    linearParams.setMargins(horizontalMargin, 0, horizontalMargin, 0);
    strokeDotsLinearLayout.setLayoutParams(linearParams);
    strokeDotsLinearLayout.setOrientation(HORIZONTAL);
    addView(strokeDotsLinearLayout);

    dotsSize = dpToPx(16); // 16dp
    dotsSpacing = dpToPx(4); // 4dp
    dotsStrokeWidth = dpToPx(2); // 2dp
    dotsCornerRadius = dotsSize / 2;
    dotIndicatorColor = Color.parseColor("#FFFFFF");
    dotsStrokeColor = dotIndicatorColor;
    dotsClickable = true;

    if (attrs != null) {
      TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicator);

      // Dots attributes
      dotIndicatorColor = a.getColor(R.styleable.PageIndicator_dotsColor, dotIndicatorColor);
      dotsStrokeColor = a.getColor(R.styleable.PageIndicator_dotsStrokeColor, dotIndicatorColor);
      dotsSize = (int) a.getDimension(R.styleable.PageIndicator_dotsSize, dotsSize);
      dotsSpacing = (int) a.getDimension(R.styleable.PageIndicator_dotsSpacing, dotsSpacing);
      dotsCornerRadius = (int) a.getDimension(R.styleable.PageIndicator_dotsCornerRadius, dotsSize / 2);

      // Spring dots attributes
      dotsStrokeWidth = (int) a.getDimension(R.styleable.PageIndicator_dotsStrokeWidth, dotsStrokeWidth);

      a.recycle();
    }

    if (isInEditMode()) {
      addStrokeDots(5);
      addView(buildDot(false));
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    refreshDots();
  }

  private void refreshDots() {
    if (dotIndicatorLayout == null) {
      setUpDotIndicator();
    }
    // Check if we need to refresh the strokeDots count
    if (strokeDots.size() < this.count) {
      addStrokeDots(this.count - strokeDots.size());
    } else if (strokeDots.size() > this.count) {
      removeDots(strokeDots.size() - count);
    }
  }

  private void setUpDotIndicator() {
    dotIndicatorLayout = buildDot(false);
    dotIndicatorView = dotIndicatorLayout.findViewById(R.id.worm_dot);
    addView(dotIndicatorLayout);
    dotIndicatorXSpring = new SpringAnimation(dotIndicatorLayout, SpringAnimation.TRANSLATION_X);
    SpringForce springForceX = new SpringForce(0);
    springForceX.setDampingRatio(1f);
    springForceX.setStiffness(300);
    dotIndicatorXSpring.setSpring(springForceX);

    FloatPropertyCompat floatPropertyCompat = new FloatPropertyCompat("DotsWidth") {
      @Override
      public float getValue(Object object) {
        return dotIndicatorView.getLayoutParams().width;
      }

      @Override
      public void setValue(Object object, float value) {
        ViewGroup.LayoutParams params = dotIndicatorView.getLayoutParams();
        params.width = (int) value;
        dotIndicatorView.requestLayout();
      }
    };
    dotIndicatorWidthSpring = new SpringAnimation(dotIndicatorLayout, floatPropertyCompat);
    SpringForce springForceWidth = new SpringForce(0);
    springForceWidth.setDampingRatio(1f);
    springForceWidth.setStiffness(300);
    dotIndicatorWidthSpring.setSpring(springForceWidth);
  }

  private void addStrokeDots(int count) {
    for (int i = 0; i < count; i++) {
      ViewGroup dot = buildDot(true);
      strokeDots.add((ImageView) dot.findViewById(R.id.worm_dot));
      strokeDotsLinearLayout.addView(dot);
    }
  }

  private ViewGroup buildDot(boolean stroke) {
    ViewGroup dot = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.worm_dot_layout, this, false);
    View dotImageView = dot.findViewById(R.id.worm_dot);
    dotImageView.setBackground(
      ContextCompat.getDrawable(getContext(), stroke ? R.drawable.worm_dot_stroke_background : R.drawable.worm_dot_background));
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dotImageView.getLayoutParams();
    params.width = params.height = dotsSize;
    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

    params.setMargins(dotsSpacing, 0, dotsSpacing, 0);

    setUpDotBackground(stroke, dotImageView);
    return dot;
  }

  private void setUpDotBackground(boolean stroke, View dotImageView) {
    GradientDrawable dotBackground = (GradientDrawable) dotImageView.getBackground();
    if (stroke) {
      dotBackground.setStroke(dotsStrokeWidth, dotsStrokeColor);
    } else {
      dotBackground.setColor(dotIndicatorColor);
    }
    dotBackground.setCornerRadius(dotsCornerRadius);
  }

  private void removeDots(int count) {
    for (int i = 0; i < count; i++) {
      strokeDotsLinearLayout.removeViewAt(strokeDotsLinearLayout.getChildCount() - 1);
      strokeDots.remove(strokeDots.size() - 1);
    }
  }

  private int dpToPx(int dp) {
    return (int) (getContext().getResources().getDisplayMetrics().density * dp);
  }

  //*********************************************************
  // Users Methods
  //*********************************************************

  /**
   * Set the indicator dot color.
   *
   * @param color the color fo the indicator dot.
   */
  public void setDotIndicatorColor(int color) {
    if (dotIndicatorView != null) {
      dotIndicatorColor = color;
      setUpDotBackground(false, dotIndicatorView);
    }
  }

  /**
   * Set the stroke indicator dots color.
   *
   * @param color the color fo the stroke indicator dots.
   */
  public void setStrokeDotsIndicatorColor(int color) {
    if (strokeDots != null && !strokeDots.isEmpty()) {
      dotsStrokeColor = color;
      for (ImageView v : strokeDots) {
        setUpDotBackground(true, v);
      }
    }
  }

  /**
   * Determine if the stroke dots are clickable to go the a page directly.
   *
   * @param dotsClickable true if dots are clickables.
   */
  public void setDotsClickable(boolean dotsClickable) {
    this.dotsClickable = dotsClickable;
  }

  public void setCount(int count) {
    this.count = count;
    refreshDots();
  }

  public void setCurrentPosition(int position, float positionOffset) {
    int stepX = dotsSize + dotsSpacing * 2;
    float xFinalPosition;
    float widthFinalPosition;

    if (positionOffset >= 0 && positionOffset < 0.1f) {
      xFinalPosition = horizontalMargin + position * stepX;
      widthFinalPosition = dotsSize;
    } else if (positionOffset >= 0.1f && positionOffset <= 0.9f) {
      xFinalPosition = horizontalMargin + position * stepX;
      widthFinalPosition = dotsSize + stepX;
    } else {
      xFinalPosition = horizontalMargin + (position + 1) * stepX;
      widthFinalPosition = dotsSize;
    }

    if (dotIndicatorXSpring.getSpring().getFinalPosition() != xFinalPosition) {
      dotIndicatorXSpring.getSpring().setFinalPosition(xFinalPosition);
    }

    if (dotIndicatorWidthSpring.getSpring().getFinalPosition() != widthFinalPosition) {
      dotIndicatorWidthSpring.getSpring().setFinalPosition(widthFinalPosition);
    }

    if (!dotIndicatorXSpring.isRunning()) {
      dotIndicatorXSpring.start();
    }

    if (!dotIndicatorWidthSpring.isRunning()) {
      dotIndicatorWidthSpring.start();
    }
  }
}
