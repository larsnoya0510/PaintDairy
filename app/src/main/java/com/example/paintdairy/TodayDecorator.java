package com.example.paintdairy;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class TodayDecorator implements DayViewDecorator {
    private CalendarDay date;
    private Context mContext;
    public TodayDecorator(CalendarDay today, Context context) {
        this.date = today;
        this.mContext = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new DotSpan(5, color));
        Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.ic_now_24dp);
        assert drawable != null;
        view.setBackgroundDrawable(drawable);
    }
}