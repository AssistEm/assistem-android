package seniorproject.caretakers.caretakersapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.UserPresenter;
import seniorproject.caretakers.caretakersapp.ui.views.DrawerRowTextView;

public class DrawerAdapter extends BaseAdapter {

    private static final int[] DRAWER_SECTIONS = {
            R.string.drawer_calendar, R.string.drawer_grocery_list, R.string.drawer_ping, R.string.drawer_settings
    };

    @Inject
    UserPresenter presenter;

    private Context mContext;

    public DrawerAdapter(Activity activity) {
        mContext = activity;
        CaretakersApplication app = (CaretakersApplication) activity.getApplication();
        app.inject(this);
    }

    @Override
    public int getCount() {
        if(presenter.isUserPatient()) {
            return DRAWER_SECTIONS.length;
        } else {
            return DRAWER_SECTIONS.length - 1;
        }
    }

    @Override
    public String getItem(int i) {
        if(presenter.isUserPatient()) {
            return mContext.getResources().getString(DRAWER_SECTIONS[i]);
        } else {
            if(i == getCount() - 1) {
                i++;
            }
            return mContext.getResources().getString(DRAWER_SECTIONS[i]);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_drawer, viewGroup, false);
            holder = new ViewHolder();
            holder.drawerText = (DrawerRowTextView) view.findViewById(R.id.drawer_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.drawerText.setText(getItem(i));
        return view;
    }

    private class ViewHolder {
        DrawerRowTextView drawerText;
    }
}
