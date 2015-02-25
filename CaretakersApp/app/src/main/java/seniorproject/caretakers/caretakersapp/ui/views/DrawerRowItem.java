package seniorproject.caretakers.caretakersapp.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;

public class DrawerRowItem extends LinearLayout implements Checkable {

    private boolean mIsChecked;
    private List<Checkable> mCheckableViews = new ArrayList<Checkable>();

    public DrawerRowItem(Context context) {
        super(context);
    }

    public DrawerRowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean b) {
        mIsChecked = b;
        for(Checkable checkable : mCheckableViews) {
            checkable.setChecked(b);
        }
        if(b) {
            setBackgroundColor(getResources().getColor(R.color.drawer_background_selected));
        } else {
            setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        mIsChecked = !mIsChecked;
        for(Checkable checkable : mCheckableViews) {
            checkable.toggle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++) {
            findCheckableChildren(getChildAt(i));
        }
    }

    private void findCheckableChildren(View v) {
        if (v instanceof Checkable) {
            this.mCheckableViews.add((Checkable) v);
        }
        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            final int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                findCheckableChildren(vg.getChildAt(i));
            }
        }
    }
}
