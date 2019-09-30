package souju.kzw.com.souju.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2019/2/27.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addfragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
