package souju.kzw.com.souju.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.StringUtil;

/**
 * Created by Android on 2019/2/27.
 */

public class CollectDialog extends Dialog {
    TextInputEditText editText;
    TextView confirm;
    TextView cancle;
    clickListener clickListener;
    Context mContext;

    public CollectDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_dialog_layout);
        editText = findViewById(R.id.edit_text);
        confirm = findViewById(R.id.confirm);
        cancle = findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (StringUtil.isNotEmpty(editText.getText().toString())) {
                    if (clickListener != null) {
                        clickListener.click(editText.getText().toString());
                    }
                } else {
                    CustomToastView.showToast(mContext, "编辑不能为空");
                }
            }
        });
    }

    public interface clickListener {
        void click(String remark);
    }

    public void setClickListener(clickListener listener) {
        clickListener = listener;
    }
}
