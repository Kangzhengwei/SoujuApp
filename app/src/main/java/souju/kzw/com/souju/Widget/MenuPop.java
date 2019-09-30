package souju.kzw.com.souju.Widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import souju.kzw.com.souju.R;

/**
 * Created by Android on 2019/2/28.
 */

public class MenuPop extends PopupWindow {
    private menuClickListener clickListener;

    public MenuPop(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_pop, null);
        final LinearLayout delete = view.findViewById(R.id.delete);
        LinearLayout moveTop = view.findViewById(R.id.move_top);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickListener != null) {
                    clickListener.delete();
                }
            }
        });
        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickListener != null) {
                    clickListener.moveTop();
                }
            }
        });
        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(view);
        // 设置弹出窗体的宽和高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setClippingEnabled(false);
        // 实例化一个ColorDrawable颜色为半透明
    }

    public interface menuClickListener {
        void delete();

        void moveTop();
    }

    public void setMenuClickListener(menuClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
