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

/**
 * DrawerRow layout that passes checked status to children to allow them to be notified of checked
 * status. Also modifies its own background color to match the checked state
 */
public class DrawerRowItem extends LinearLayout implements Checkable {

    private boolean mIsChecked;
    private List<Checkable> mCheckableViews = new ArrayList<Checkable>();

    public DrawerRowItem(Context context) {
        super(context);
    }

    public DrawerRowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Method called when the view is checked. Sets the state, notifies children, and changes its
     * own background color
     * @param b Boolean value representing if the view is to be checked or not
     */
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

    /**
     * Method to check if the view is checked.
     * @return if the view is checked
     */
    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    /**
     * Toggle the checked status of the view and toggle the children views as well
     */
    @Override
    public void toggle() {
        mIsChecked = !mIsChecked;
        for(Checkable checkable : mCheckableViews) {
            checkable.toggle();
        }
    }

    /**
     * Callback for when the view is finished inflating. Finds all the children views that can
     * be checked and stores references to them
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++) {
            findCheckableChildren(getChildAt(i));
        }
    }

    /**
     * Private method that finds all checkable children recursively.
     * @param v View in which to find the checkable children.
     */
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
