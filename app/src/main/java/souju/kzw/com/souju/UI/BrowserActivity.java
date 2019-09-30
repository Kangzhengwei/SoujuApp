package souju.kzw.com.souju.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import souju.kzw.com.souju.Bean.AdsScript;
import souju.kzw.com.souju.Bean.CollectDataBean;
import souju.kzw.com.souju.R;
import souju.kzw.com.souju.Util.Constant;
import souju.kzw.com.souju.Util.GsonUtil;
import souju.kzw.com.souju.Util.PermissionUtil;
import souju.kzw.com.souju.Util.WebClient;
import souju.kzw.com.souju.Util.WebViewJavaScriptFunction;
import souju.kzw.com.souju.Widget.CollectDialog;
import souju.kzw.com.souju.Widget.CustomToastView;
import souju.kzw.com.souju.Widget.X5WebView;

public class BrowserActivity extends AppCompatActivity implements WebClient.requestListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.video_webView)
    X5WebView webView;
    @BindView(R.id.collect)
    ImageView collect;


    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private View customView;
    private FrameLayout fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    private AudioManager mAudioManager;
    private WebClient webClient;
    private Realm realm;
    private String urlTitle;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        ButterKnife.bind(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        realm = Realm.getDefaultInstance();
        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
        List<AdsScript> list = GsonUtil.getInstance().fromJson(Constant.adScript, new TypeToken<List<AdsScript>>() {
        }.getType());
        initWebView(list);
    }

    public void initWebView(List<AdsScript> list) {
        webClient = new WebClient(list);
        webClient.setRequestListener(this);
        webView.setWebViewClient(webClient);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                showCustomView(view, customViewCallback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });

        //全屏播放的相关方法
        webView.addJavascriptInterface(new WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub

            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                BrowserActivity.this.enableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
                BrowserActivity.this.disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                BrowserActivity.this.enableLiteWndFunc();
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                BrowserActivity.this.enablePageVideoFunc();
            }
        }, "Android");
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    @OnClick({R.id.collect, R.id.setting})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.collect:
                CollectDialog dialog = new CollectDialog(this);
                dialog.show();
                dialog.setClickListener(new CollectDialog.clickListener() {
                    @Override
                    public void click(final String remark) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                boolean ishas = false;
                                List<CollectDataBean> list = realm.where(CollectDataBean.class).findAll();
                                if (list != null && list.size() > 0) {
                                    for (CollectDataBean bean : list) {
                                        if (bean.getUrl().equals(url)) {
                                            ishas = true;
                                            CustomToastView.showToast(BrowserActivity.this, "该链接已收藏");
                                            break;
                                        }
                                    }
                                }
                                if (!ishas) {
                                    CollectDataBean data = realm.createObject(CollectDataBean.class);
                                    data.setRemark(remark);
                                    data.setUrl(url);
                                    data.setUrlTitle(urlTitle);
                                    Intent intent = new Intent();
                                    intent.putExtra("type", 1);
                                    setResult(20, intent);
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.setting:
                if (PermissionUtil.checkReadPermission(this)) {
                    sendIntent();
                }
                break;
        }
    }

    public void sendIntent() {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendIntent();
        }
    }

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        setFullScreen();
        BrowserActivity.this.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(BrowserActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attr);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestResult(String title, String url) {
        urlTitle = title;
        this.url = url;
    }


    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableX5FullscreenFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {//解决Receiver not registered: android.widget.ZoomButtonsController
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setVisibility(View.GONE);// 把destroy()延后
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        mAudioManager.abandonAudioFocus(audioFocusChangeListener);
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAudioFocus();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    //音频控件获取焦点

    private boolean requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };
}
