package seniorproject.caretakers.caretakersapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.views.DrawerRowTextView;

public class DrawerAdapter extends BaseAdapter {

    private static final int[] DRAWER_SECTIONS_PATIENT = {
            R.string.drawer_calendar, R.string.drawer_grocery_list, R.string.drawer_community, R.string.drawer_location, R.string.drawer_ping, R.string.drawer_settings

    };

    private static final int[] DRAWER_SECTIONS_CARETAKER = {
            R.string.drawer_calendar, R.string.drawer_grocery_list, R.string.drawer_community, R.string.drawer_location, R.string.drawer_settings

    };

    private Context mContext;

    public DrawerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if(AccountHandler.getInstance(mContext).getCurrentUser() instanceof Patient) {
            return DRAWER_SECTIONS_PATIENT.length;
        } else {
            return DRAWER_SECTIONS_CARETAKER.length;
        }
    }

    @Override
    public String getItem(int i) {
        if(AccountHandler.getInstance(mContext).getCurrentUser() instanceof Patient) {
            return mContext.getResources().getString(DRAWER_SECTIONS_PATIENT[i]);
        } else {
            return mContext.getResources().getString(DRAWER_SECTIONS_CARETAKER[i]);
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
