package com.ddopik.linktask.base;




import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ddopik on 9/17/2017.
 */

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    private final List mFragmentListT = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragmentListT.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentListT.size();
    }

    <T> void addFragment(T fragment, String title) {

        mFragmentListT.add(fragment);
        mFragmentTitleList.add(title);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}


