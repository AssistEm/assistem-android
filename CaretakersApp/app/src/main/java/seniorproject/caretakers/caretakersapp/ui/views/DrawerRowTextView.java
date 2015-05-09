package seniorproject.caretakers.caretakersapp.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;

import seniorproject.caretakers.caretakersapp.R;

/**
 * Custom Checkable TextView class that also changes its own text color when checked
 */
public class DrawerRowTextView extends CheckedTextView {

    public DrawerRowTextView(Context context) {
        super(context);
    }

    public DrawerRowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerRowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean b) {
        super.setChecked(b);
        if(b) {
            setTextColor(getResources().getColor(R.color.drawer_text_selected_color));
        } else {
            setTextColor(getResources().getColor(R.color.drawer_text_color));
        }
    }
}