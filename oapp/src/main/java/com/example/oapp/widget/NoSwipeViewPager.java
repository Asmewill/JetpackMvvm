package com.example.oapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Owen on 2019/11/19
 */
public class NoSwipeViewPager extends ViewPager {
    private boolean isScroll=false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public NoSwipeViewPager(@NonNull Context context) {
        super(context);
    }

    public NoSwipeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
          if(isScroll){
              return super.onInterceptTouchEvent(ev);
          }else{
              return false;
          }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
         if(isScroll){
             return  super.onTouchEvent(ev);
         }else{
             return true;
         }
    }
}
