package souju.kzw.com.souju.Widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import souju.kzw.com.souju.R;


/**
 * Created by Android on 2018/9/17.
 */

public class CustomToastView {
    private static TextView mTextView;
    public static void showToast(Context context, String message) {
        if(context != null) {
            //加载Toast布局
            View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
            toastRoot.setAlpha(0.7f);
            //初始化布局控件
            mTextView = toastRoot.findViewById(R.id.tv_conent);
            //为控件设置属性
            mTextView.setText(message);
            //Toast的初始化
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastRoot);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();
        }
    }


}
