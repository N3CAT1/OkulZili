package com.necatitufan.okulzili;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentList.get(position).getArguments().getString("GUNADI");
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }
}