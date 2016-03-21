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
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

/**
 * Created by sarah on 3/18/16.
 */
public class PrimaryCaretakerAdapter extends BaseAdapter {

    List<User> mCaretakerList;
    User mPrimaryCaretaker;

    Context mContext;

    private View.OnClickListener mSetPrimaryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            mPrimaryCaretaker = mCaretakerList.get(position); //change list to dictionary with id's and make this
            notifyDataSetChanged();
        }
    };

    public PrimaryCaretakerAdapter(Context context) {
        mContext = context;
        mCaretakerList = new ArrayList<>();
        mPrimaryCaretaker = null;
    }

    public void addCaretaker(User caretaker) {
        mCaretakerList.add(caretaker);
        notifyDataSetChanged();
    }

    public List<User> getCaretakers() {
        return mCaretakerList;
    }

    public User getPrimaryCaretaker() {
        return mPrimaryCaretaker;
    }

    public int getPosition(User caretaker) {
        return mCaretakerList.indexOf(caretaker);
    }

    @Override
    public int getCount() {
        return mCaretakerList.size();
    }

    @Override
    public User getItem(int i) {
        if(i >= 0 && i < mCaretakerList.size()) {
            return mCaretakerList.get(i);
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
            view = inflater.inflate(R.layout.item_caretaker_list_modify_primary, viewGroup, false);
            holder = new ViewHolder();
            holder.mName = (TextView) view.findViewById(R.id.caretaker_name);
            holder.mDeleteImage = (ImageView) view.findViewById(R.id.remove);
            holder.mDeleteImage.setOnClickListener(mSetPrimaryClickListener);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        User caretaker = getItem(i);
        holder.mName.setText(caretaker.getDisplayName());
        holder.mDeleteImage.setTag(i);
        return view;
    }

    private class ViewHolder {
        TextView mName;
        ImageView mDeleteImage;
    }
}