package com.bbsz.mlibrary.plugin.webview;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/5/18.
 */
/*public*/ class MWebView extends VideoEnabledWebView {
    private static final String TAG = MWebView.class.getSimpleName();
    private JsCallJava jsJavaInterface;

    public MWebView(Context context) {
        super(context);
        init();
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        WebSettings settings = getSettings();
        //自定适配字体大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            //设置字体缩放比例，默认100
            settings.setTextZoom(100);//TODO need calculation
        }

        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(WebSettings.RenderPriority.LOW);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        settings.setNavDump(true);//Deprecated
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);

        settings.setUseWideViewPort(true);

        //if set to true, {@link WebChromeClient#onCreateWindow} must be implemented by the host application. The default is false.
        settings.setSupportMultipleWindows(false);

        settings.setStandardFontFamily("sans-serif");//sans-serif  schrappen
        settings.setFixedFontFamily("monospace");//monospace
        settings.setMinimumFontSize(8);
        settings.setDefaultFixedFontSize(8);

        //是否允许加载图片
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);


        //是否允许执行JavaScript语句
        settings.setJavaScriptEnabled(true);

        //启用数据库
        settings.setDatabaseEnabled(true);

        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + "/webViewCache ";
        if (!TextUtils.isEmpty(cacheDirPath)) {
            settings.setAppCachePath(cacheDirPath);
            settings.setAppCacheEnabled(true);
//            settings.setAppCacheMaxSize(1024 * 1024 * 10);//设置最大缓存
        }


        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");//default is "UTF-8"

        settings.setUserAgentString("");
        Log.d(TAG, "WebView's user-agent：" + settings.getUserAgentString());


        settings.setAllowFileAccess(true);

//		settings.setPluginsEnabled(true);
        settings.setLoadWithOverviewMode(true);

        String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);
        //设置DOM存储，开启js定位功能
        settings.setDomStorageEnabled(true);

        setBackgroundColor(0); // 设置背景色


        //JsInterface Method one
        jsJavaInterface = new JsCallJava(getContext());
        addJavascriptInterface(jsJavaInterface, control);
        //JsBridge  Method two
//        setWebChromeClient(new WebChromeClient());
        //JsRouter  Method three
//        setWebViewClient(new WebViewClient());
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    class MWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    class MWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
