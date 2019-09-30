package souju.kzw.com.souju.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import souju.kzw.com.souju.Bean.WebSiteBean;
import souju.kzw.com.souju.R;

/**
 * Created by Android on 2019/2/26.
 */

public class WebSiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WebSiteBean> list;
    private OnClickListener onClickListener;
    private menuClickListener menuClickListener;

    public WebSiteAdapter(List<WebSiteBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SiteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.website_item, parent, false));
    }

    class SiteViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView sign;
        ImageView menu;
        View item;

        public SiteViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            tvName = itemView.findViewById(R.id.site_name);
            sign = itemView.findViewById(R.id.sign);
            menu = itemView.findViewById(R.id.menu);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final WebSiteBean bean = list.get(position);
        ((SiteViewHolder) holder).tvName.setText(bean.getUrl());
        String title = bean.getUrl().substring(0, 1);
        ((SiteViewHolder) holder).sign.setText(title);
        ((SiteViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.OnClick(bean);
                }
            }
        });
        ((SiteViewHolder) holder).menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuClickListener != null) {
                    menuClickListener.menuClick(v, bean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClickListener {
        void OnClick(WebSiteBean bean);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        onClickListener = clickListener;
    }

    public interface menuClickListener {
        void menuClick(View v, WebSiteBean bean, int position);
    }

    public void setMenuClickListener(menuClickListener listener) {
        menuClickListener = listener;
    }
}
