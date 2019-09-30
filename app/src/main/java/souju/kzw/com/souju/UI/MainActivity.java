package souju.kzw.com.souju.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import souju.kzw.com.souju.Adapter.ViewPagerAdapter;
import souju.kzw.com.souju.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.stl_tabs)
    SlidingTabLayout stlTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<>();
    String[] titles = new String[]{"网站", "收藏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments.add(AllWebSiteFragment.newInstance("", ""));
        fragments.add(CollectFragment.newInstance("", ""));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(viewPagerAdapter);
        stlTabs.setViewPager(viewpager, titles);
    }


}
