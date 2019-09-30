package souju.kzw.com.souju.UI;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import souju.kzw.com.souju.Adapter.WebSiteAdapter;
import souju.kzw.com.souju.Bean.WebSiteBean;
import souju.kzw.com.souju.Bean.WebSiteData;
import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.Constant;
import souju.kzw.com.souju.Util.DimenUtil;
import souju.kzw.com.souju.Util.GsonUtil;
import souju.kzw.com.souju.Widget.AddWebSiteDialog;
import souju.kzw.com.souju.Widget.MenuPop;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllWebSiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllWebSiteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    @BindView(R.id.floatActionBar)
    FloatingActionButton floatActionBar;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout swiprefresh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Realm realm;
    private List<WebSiteBean> list = new ArrayList<>();

    // TODO: Rename and change types and number of parameters
    public static AllWebSiteFragment newInstance(String param1, String param2) {
        AllWebSiteFragment fragment = new AllWebSiteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_web_site, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        initData();
        initView(list);
        return view;
    }

    private void initData() {
        WebSiteData data = realm.where(WebSiteData.class).findFirst();
        if (data != null) {
            list = data.getWebSiteBeanRealmList();
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<WebSiteBean> sitelist = GsonUtil.getInstance().fromJson(Constant.webSite, new TypeToken<List<WebSiteBean>>() {
                    }.getType());
                    WebSiteData data = realm.createObject(WebSiteData.class);
                    data.setWebSiteBeanRealmList(sitelist);
                    list = data.getWebSiteBeanRealmList();
                }
            });
        }
    }

    private void initView(final List<WebSiteBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        final WebSiteAdapter adapter = new WebSiteAdapter(list);
        recyclerview.setAdapter(adapter);
        adapter.setOnClickListener(new WebSiteAdapter.OnClickListener() {
            @Override
            public void OnClick(WebSiteBean bean) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BrowserActivity.class);
                intent.putExtra("url", bean.getSiteName());
                startActivity(intent);
            }
        });
        swiprefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swiprefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swiprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiprefresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        adapter.notifyDataSetChanged();
                        swiprefresh.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        adapter.setMenuClickListener(new WebSiteAdapter.menuClickListener() {
            @Override
            public void menuClick(View v, final WebSiteBean bean, int position) {
                MenuPop pop = new MenuPop(getActivity());
                if (position == list.size() - 1) {
                    pop.showAsDropDown(v, DimenUtil.dip2px(getActivity(), -100), DimenUtil.dip2px(getActivity(), -80), Gravity.BOTTOM | Gravity.START);
                } else {
                    pop.showAsDropDown(v, DimenUtil.dip2px(getActivity(), -100), 0, Gravity.BOTTOM | Gravity.START);
                }
                pop.setMenuClickListener(new MenuPop.menuClickListener() {
                    @Override
                    public void delete() {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                list.remove(bean);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void moveTop() {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                list.remove(bean);
                                list.add(0, bean);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.floatActionBar)
    public void onViewClicked() {
        AddWebSiteDialog dialog = new AddWebSiteDialog(getActivity());
        dialog.show();
        dialog.setClickListener(new AddWebSiteDialog.clickListener() {
            @Override
            public void click(final String site, final String url) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        WebSiteBean bean = realm.createObject(WebSiteBean.class);
                        bean.setSiteName(url);
                        bean.setUrl(site);
                        WebSiteData data = realm.where(WebSiteData.class).findFirst();
                        if (data != null) {
                            data.getWebSiteBeanRealmList().add(bean);
                        }
                    }
                });
            }
        });
    }
}
