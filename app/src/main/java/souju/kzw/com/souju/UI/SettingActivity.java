package souju.kzw.com.souju.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.CacheUtils;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.clear_cache)
    RelativeLayout clearCache;
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        try {
            String dataSize = CacheUtils.getTotalCacheSize(getApplicationContext());
            tvCache.setText(dataSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.clear_cache)
    public void onViewClicked() {
        CacheUtils.clearAllCache(getApplicationContext());
        String dataSize = null;
        try {
            dataSize = CacheUtils.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataSize != null) {
            tvCache.setText(dataSize);
        }
    }

}
