package krot.sample.com.meshchat.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import krot.sample.com.meshchat.fragment.ImageFragment;
import krot.sample.com.meshchat.fragment.PlainTextFragment;
import krot.sample.com.meshchat.fragment.VideoFragment;

/**
 * Created by Krot on 5/13/18.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {


    private String[] tabTitles = new String[]{"PLAIN TEXT", "IMAGE", "VIDEO"};
    private List<Fragment> fragmentList;

    public CustomPagerAdapter(List<Fragment> fragmentList, FragmentManager fm) {
        super(fm);
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
            default:
                return null;
        }
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
