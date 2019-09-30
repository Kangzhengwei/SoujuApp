package souju.kzw.com.souju.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.StringUtil;

/**
 * Created by Android on 2019/2/28.
 */

public class AddWebSiteDialog extends Dialog {
    TextInputEditText siteEdit;
    TextInputEditText urlEdit;
    TextView confirm;
    TextView cancle;
    Context mContext;
    clickListener clickListener;

    public AddWebSiteDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_web_site_dialog);
        siteEdit = findViewById(R.id.site_edit);
        urlEdit = findViewById(R.id.url_edit);
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
                if (StringUtil.isNotEmpty(siteEdit.getText().toString()) && StringUtil.isNotEmpty(urlEdit.getText().toString())) {
                    if (clickListener != null) {
                        clickListener.click(siteEdit.getText().toString(), urlEdit.getText().toString());
                    }
                } else {
                    CustomToastView.showToast(mContext, "编辑不能为空");
                }
            }
        });
    }

    public interface clickListener {
        void click(String site, String url);
    }

    public void setClickListener(clickListener listener) {
        clickListener = listener;
    }
}
