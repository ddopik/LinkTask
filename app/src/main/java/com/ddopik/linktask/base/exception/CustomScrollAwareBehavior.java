package com.ddopik.linktask.base.exception;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

public class CustomScrollAwareBehavior extends FloatingActionButton.Behavior{

    private Handler handler = new Handler();
    private FloatingActionButton fab;

    public CustomScrollAwareBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, @NotNull View directTargetChild, View target, int nestedScrollAxes) {
        fab = child;

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }



    Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            fab.show();
        }
    };


    @Override
    public void onNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               @NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            handler.removeCallbacks(showRunnable);
            handler.postDelayed(showRunnable,1000);
            child.hide();
        }
    }
}