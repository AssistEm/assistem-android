package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.presenters.GroceryPresenter;
import seniorproject.caretakers.caretakersapp.views.GroceryView;

public class GroceryFragment extends Fragment implements GroceryView {

    @Inject
    GroceryPresenter presenter;

    Spinner mSortBySpinner;
    Spinner mSortOrderSpinner;

    ListView mGroceryList;

    ArrayAdapter<GroceryItem> mGroceryAdapter;

    Comparator<GroceryItem> mCurrentSort;

    GroceryItem.FilterGroceryItem mFilter;

    GroceryEditListener mEditListener;

    ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(mEditListener != null) {
                mEditListener.onGroceryItemSelected(mGroceryAdapter.getItem(i));
            }
        }
    };

    Spinner.OnItemSelectedListener mOnSortItemSelected = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sortItems();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            adapterView.setSelection(0);
        }
    };

    public static GroceryFragment newInstance(int filterItem, List<GroceryFragment> addList) {
        GroceryFragment fragment = new GroceryFragment();
        Bundle args = new Bundle();
        args.putInt("filter", filterItem);
        fragment.setArguments(args);
        addList.add(fragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        int filterItem = args.getInt("filter");
        mFilter = GroceryItem.mFilters[filterItem];
        View rootView = inflater.inflate(R.layout.fragment_grocery, viewGroup, false);
        mGroceryList = (ListView) rootView.findViewById(R.id.grocery_list);
        mGroceryAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_grocery_list,
                R.id.grocery_item_title);
        mGroceryList.setAdapter(mGroceryAdapter);
        mGroceryList.setOnItemClickListener(mItemClickListener);
        mSortBySpinner = (Spinner) rootView.findViewById(R.id.sort_by_type);
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grocery_sort_types, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(sortByAdapter);
        mSortBySpinner.setOnItemSelectedListener(mOnSortItemSelected);
        mSortOrderSpinner = (Spinner) rootView.findViewById(R.id.sort_by_order);
        ArrayAdapter<CharSequence> sortOrderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grocery_sort_order, android.R.layout.simple_spinner_item);
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortOrderSpinner.setAdapter(sortOrderAdapter);
        mSortOrderSpinner.setOnItemSelectedListener(mOnSortItemSelected);
        mCurrentSort = GroceryItem.ASC_NAME_COMPARE;
        mGroceryAdapter.sort(mCurrentSort);
        presenter.retrieveItems();
        return rootView;
    }

    private void sortItems() {
        int type = mSortBySpinner.getSelectedItemPosition();
        int order = mSortOrderSpinner.getSelectedItemPosition();
        if(type == 0 && order == 0) {
            mCurrentSort = GroceryItem.ASC_NAME_COMPARE;
        } else if(type == 0 && order == 1) {
            mCurrentSort = GroceryItem.DESC_NAME_COMPARE;
        } else if(type == 1 && order == 0) {
            mCurrentSort = GroceryItem.ASC_URGENCY_COMPARE;
        } else if(type == 1 && order == 1) {
            mCurrentSort = GroceryItem.DESC_URGENCY_COMPARE;
        }
        mGroceryAdapter.sort(mCurrentSort);
    }

    public void refresh() {
        presenter.retrieveItems();
    }

    public void setGroceryEditListener(GroceryEditListener listener) {
        mEditListener = listener;
    }

    @Override
    public void onGroceryItemsFetched(List<GroceryItem> items) {
        mGroceryAdapter.setNotifyOnChange(false);
        mGroceryAdapter.clear();
        for(GroceryItem item : items) {
            if(mFilter.isInFilter(item, presenter.retrieveUser())) {
                mGroceryAdapter.add(item);
            }
        }
        mGroceryAdapter.sort(mCurrentSort);
        mGroceryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        Crouton.makeText(this.getActivity(), error, Style.ALERT).show();
    }

    public interface GroceryEditListener {
        public void onGroceryItemSelected(GroceryItem item);
    }
}
