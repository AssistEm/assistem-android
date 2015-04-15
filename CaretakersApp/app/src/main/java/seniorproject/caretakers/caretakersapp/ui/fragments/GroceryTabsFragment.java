package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.data.handlers.GroceryHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Caretaker;
import seniorproject.caretakers.caretakersapp.tempdata.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.actvities.AddGroceryItemActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.ViewGroceryItemActivity;
import seniorproject.caretakers.caretakersapp.ui.adapters.GroceryPagerAdapter;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class GroceryTabsFragment extends Fragment {

    public static final int ADD_GROCERY_ITEM_REQUEST = 1;
    public final static int VIEW_GROCERY_ITEM_REQUEST = 2;

    ViewPager mPager;
    GroceryPagerAdapter mAdapter;
    AddFloatingActionButton mAddItemButton;
    PagerSlidingTabStrip mTabStrip;

    View.OnClickListener mOnAddItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), AddGroceryItemActivity.class);
            startActivityForResult(intent, ADD_GROCERY_ITEM_REQUEST);
        }
    };

    GroceryFragment.GroceryEditListener mEditListener = new GroceryFragment.GroceryEditListener() {

        @Override
        public void onGroceryItemSelected(GroceryItem item) {
            Intent intent = new Intent(getActivity(), ViewGroceryItemActivity.class);
            intent.putExtra("item", item);
            startActivityForResult(intent, VIEW_GROCERY_ITEM_REQUEST);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_grocery_tabs, viewGroup, false);
        mPager = (ViewPager) rootView.findViewById(R.id.fragment_pager);
        mPager.setOffscreenPageLimit(3);
        mAdapter = new GroceryPagerAdapter(getChildFragmentManager(), getActivity());
        mAdapter.setGroceryEditListener(mEditListener);
        mPager.setAdapter(mAdapter);
        mTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        mTabStrip.setViewPager(mPager);
        //tabStrip.setIndicatorColorResource(R.color.questions_pager_tab_indicator);
        //tabStrip.setTextColorResource(R.color.questions_pager_tab_text);
        mAddItemButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_grocery_button);
        if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Caretaker) {
            mAddItemButton.setVisibility(View.GONE);
        } else if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Patient) {
            mAddItemButton.setVisibility(View.VISIBLE);
            mAddItemButton.setOnClickListener(mOnAddItemClickListener);
        }
        return rootView;
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        Log.i("DONE FRAGMENT", "" + request + ", " + result);
        if(request == ADD_GROCERY_ITEM_REQUEST) {
            if(result == AddGroceryItemActivity.GROCERY_ITEM_ADDED_RESULT) {
                refreshLists();
            }
        }
        switch(request) {
            case ADD_GROCERY_ITEM_REQUEST:
                if(result == AddGroceryItemActivity.GROCERY_ITEM_ADDED_RESULT) {
                    refreshLists();
                }
                break;
            case VIEW_GROCERY_ITEM_REQUEST:
                switch(result) {
                    case ViewGroceryItemActivity.GROCERY_ITEM_EDITED_RESULT:
                        refreshLists();
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLists();
    }

    private void refreshLists() {
        mAdapter.refreshLists();
    }
}
