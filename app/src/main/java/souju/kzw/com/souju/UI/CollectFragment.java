package souju.kzw.com.souju.UI;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import souju.kzw.com.souju.Adapter.CollectAdapter;
import souju.kzw.com.souju.Bean.CollectDataBean;
import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Widget.ConfirmDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout swiprefresh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Realm realm;
    private List<CollectDataBean> list = new ArrayList<>();
    private CollectAdapter adapter;

    // TODO: Rename and change types and number of parameters
    public static CollectFragment newInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
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
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        initData();
        initView();
        return view;
    }

    private void initData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list = realm.where(CollectDataBean.class).findAll();
            }
        });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new CollectAdapter(list);
        recyclerview.setAdapter(adapter);
        adapter.setOnClickListener(new CollectAdapter.OnClickListener() {
            @Override
            public void OnClick(CollectDataBean bean) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BrowserActivity.class);
                intent.putExtra("url", bean.getUrl());
                startActivityForResult(intent, 10);
            }
        });
        adapter.setOnLongClickListener(new CollectAdapter.onLongClickListener() {
            @Override
            public void longClick(final CollectDataBean bean, final int position) {
                ConfirmDialog dialog = new ConfirmDialog(getActivity());
                dialog.show();
                dialog.setClickListener(new ConfirmDialog.clickListener() {
                    @Override
                    public void click() {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                bean.deleteFromRealm();
                            }
                        });
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, list.size() - position);
                    }
                });
            }
        });
        swiprefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swiprefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 20) {
            list = realm.where(CollectDataBean.class).findAll();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
    }
}
