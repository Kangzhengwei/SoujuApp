package souju.kzw.com.souju.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import souju.kzw.com.souju.Bean.CollectDataBean;
import souju.kzw.com.souju.R;

/**
 * Created by Android on 2019/2/27.
 */

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CollectDataBean> list = new ArrayList<>();
    private OnClickListener onClickListener;
    private onLongClickListener longClickListener;

    public CollectAdapter(List<CollectDataBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_view_item, parent, false));
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {
        TextView tvRemark;
        TextView tvTitle;
        View view;

        public CollectViewHolder(View itemView) {
            super(itemView);
            tvRemark = itemView.findViewById(R.id.remark);
            tvTitle = itemView.findViewById(R.id.urlTitle);
            view = itemView;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CollectDataBean bean = list.get(position);
        ((CollectViewHolder) holder).tvRemark.setText(bean.getRemark());
        ((CollectViewHolder) holder).tvTitle.setText(bean.getUrlTitle());
        ((CollectViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.OnClick(bean);
                }
            }
        });
        ((CollectViewHolder) holder).view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.longClick(bean,position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClickListener {
        void OnClick(CollectDataBean bean);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        onClickListener = clickListener;
    }

    public interface onLongClickListener {
        void longClick(CollectDataBean bean,int position);
    }

    public void setOnLongClickListener(onLongClickListener listener) {
        longClickListener = listener;
    }
}
