package souju.kzw.com.souju.Util;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import souju.kzw.com.souju.Bean.AdsScript;

/**
 * Created by Android on 2019/2/26.
 */

public class WebClient extends WebViewClient {
    private List<AdsScript> list = new ArrayList<>();
    private requestListener listener;

    public WebClient(List<AdsScript> list) {
        this.list = list;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        for (AdsScript bean : list) {
            if (url.contains(bean.getWebsite())) {
                String js = bean.getScript();
                view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                    }
                });
            }
        }
        if (listener != null) {
            listener.requestResult(view.getTitle(), url);
        }
    }

    public interface requestListener {
        void requestResult(String title, String url);
    }

    public void setRequestListener(requestListener listener) {
        this.listener = listener;
    }
}
