package seniorproject.caretakers.caretakersapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Availability;

public class AvailabilityAdapter extends BaseAdapter {

    List<Availability> mAvailabilityList;

    Context mContext;

    private View.OnClickListener mRemoveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            mAvailabilityList.remove(position);
            notifyDataSetChanged();
        }
    };

    public AvailabilityAdapter(Context context) {
        mContext = context;
        mAvailabilityList = new ArrayList<>();
    }

    public void addAvailability(Availability avail) {
        mAvailabilityList.add(avail);
        notifyDataSetChanged();
    }

    public void addAvailability(List<Availability> availabilities) {
        mAvailabilityList.addAll(availabilities);
        notifyDataSetChanged();
    }

    public List<Availability> getAvailability() {
        return mAvailabilityList;
    }

    @Override
    public int getCount() {
        return mAvailabilityList.size();
    }

    @Override
    public Availability getItem(int i) {
        if(i >= 0 && i < mAvailabilityList.size()) {
            return mAvailabilityList.get(i);
        }
        return null;
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
            view = inflater.inflate(R.layout.item_availability, viewGroup, false);
            holder = new ViewHolder();
            holder.mDayText = (TextView) view.findViewById(R.id.days_of_week);
            holder.mTimeText = (TextView) view.findViewById(R.id.times);
            holder.mDeleteImage = (ImageView) view.findViewById(R.id.remove);
            holder.mDeleteImage.setOnClickListener(mRemoveClickListener);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Availability avail = getItem(i);
        holder.mDayText.setText(avail.getDisplayDay());
        holder.mTimeText.setText(avail.getDisplayTime(mContext));
        holder.mDeleteImage.setTag(i);
        return view;
    }

    private class ViewHolder {
        TextView mDayText;
        TextView mTimeText;
        ImageView mDeleteImage;
    }
}
