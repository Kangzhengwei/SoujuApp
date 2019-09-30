package souju.kzw.com.souju.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.StringUtil;

/**
 * Created by Android on 2019/2/27.
 */

public class ConfirmDialog extends Dialog {
    TextView confirm;
    TextView cancle;
    clickListener clickListener;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);
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
                if (clickListener != null) {
                    clickListener.click();
                }
            }
        });
    }

    public interface clickListener {
        void click();
    }

    public void setClickListener(clickListener listener) {
        clickListener = listener;
    }
}
