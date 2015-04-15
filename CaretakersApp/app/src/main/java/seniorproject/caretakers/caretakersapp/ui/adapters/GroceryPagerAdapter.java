package seniorproject.caretakers.caretakersapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.UserPresenter;
import seniorproject.caretakers.caretakersapp.ui.fragments.GroceryFragment;

public class GroceryPagerAdapter extends FragmentPagerAdapter {

    int[] mPatientFragmentTitles = {R.string.grocery_tab_all, R.string.grocery_tab_not_taken, R.string.grocery_tab_taken};
    int[] mCaretakerFragmentTitles = {R.string.grocery_tab_not_taken, R.string.grocery_tab_your_items};

    Context mContext;

    GroceryFragment.GroceryEditListener mEditListener;

    @Inject
    UserPresenter presenter;

    List<GroceryFragment> mGroceryFragments;

    public GroceryPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        mContext = activity;
        mGroceryFragments = new ArrayList<>();
        CaretakersApplication app = (CaretakersApplication) activity.getApplication();
        app.inject(this);
    }

    @Override
    public String getPageTitle(int position) {
        if(isPatient()) {
            return mContext.getResources().getString(mPatientFragmentTitles[position]).toUpperCase();
        } else {
            return mContext.getResources().getString(mCaretakerFragmentTitles[position]).toUpperCase();
        }
    }

    @Override
    public Fragment getItem(int position) {
        GroceryFragment fragment;
        if (isPatient()) {
            switch (position) {
                case 0:
                    fragment = GroceryFragment.newInstance(0, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
                case 1:
                    fragment = GroceryFragment.newInstance(1, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
                case 2:
                    fragment = GroceryFragment.newInstance(2, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
                default:
                    fragment = GroceryFragment.newInstance(0, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
            }
        } else {
            switch(position) {
                case 0:
                    fragment = GroceryFragment.newInstance(1, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
                case 1:
                    fragment = GroceryFragment.newInstance(3, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
                default:
                    fragment = GroceryFragment.newInstance(1, mGroceryFragments);
                    fragment.setGroceryEditListener(mEditListener);
                    return fragment;
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    @Override
    public int getCount() {
        if(presenter.isUserPatient()) {
            return mPatientFragmentTitles.length;
        } else {
            return mCaretakerFragmentTitles.length;
        }

    }

    public void refreshLists() {
        for(GroceryFragment fragment : mGroceryFragments) {
            fragment.refresh();
        }
    }

    private boolean isPatient() {
        return presenter.isUserPatient();
    }
}
