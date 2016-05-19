package com.bbsz.mlibrary.plugin.webview;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbsz.mlibrary.R;
import com.bbsz.mlibrary.permissions.PermissionUtil;

/**
 * Created by Administrator on 2016/5/18.
 * <p>加载WebView</p>
 */
/*public*/ class WebWidget extends FrameLayout {

    private static final String TAG = WebWidget.class.getSimpleName();
    private MWebView webView;
    private WebViewProgress progress;
    private View errorView;

    private boolean isLoadFailed = false;
    private PermissionUtil mPermissionUtil;


    private WebViewClient client = new WebViewClient() {

        //JsRouter  Method three
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading, url = " + url);
            if (!URLUtil.isValidUrl(url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted, url = " + url);
            isLoadFailed = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished, url = " + url);
            updateTitle(webView.getTitle());
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.w(TAG, "onReceivedError");
            mayByLoadFailed(0.5f);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.w(TAG, "onReceivedHttpError, errorResponse = " + errorResponse.toString());
            mayByLoadFailed(0.2f);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.w(TAG, "onReceivedSslError, error = " + error.toString());
            mayByLoadFailed(0.2f);
        }
    };


    private WebChromeClient chrome = new VideoEnabledWebChromeClient() {
        //JsBridge  Method two
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.d(TAG, "onJsPrompt, url = " + url);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "onProgressChanged(), newProgress = " + newProgress);
            progress.updateProgress(newProgress / 100.f);
            if (newProgress >= 40 && !isLoadFailed && webView.getVisibility() != View.VISIBLE) {
                webView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.d(TAG, "onReceivedTitle(), title = " + title);
            updateTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            Log.d(TAG, "onReceivedIcon()");
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean recomposed) {
            super.onReceivedTouchIconUrl(view, url, recomposed);
            Log.d(TAG, "onReceivedTouchIconUrl(), url = " + url + ", recomposed = " + recomposed);

        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
            Log.d(TAG, "onGeolocationPermissionsHidePrompt");
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin,
                                                       final GeolocationPermissions.Callback callback) {
            Log.d(TAG, "onGeolocationPermissionsShowPrompt(), origin = " + origin);

            if (mContext instanceof Activity) {
                mPermissionUtil.requestPermission((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionUtil.OnCheckPermissionCallback() {
                    @Override
                    public void requestPermissionSuccess() {
                        Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per  success");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Allow to access location information?");
                        DialogInterface.OnClickListener dialogButtonOnClickListener = new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int clickedButton) {
                                if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
                                    Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per dialog allow");
                                    callback.invoke(origin, true, true);
                                } else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
                                    callback.invoke(origin, false, false);
                                    Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per dialog deny");
                                }
                            }
                        };
                        builder.setPositiveButton("Allow", dialogButtonOnClickListener);
                        builder.setNegativeButton("Deny", dialogButtonOnClickListener);
                        builder.show();
                    }

                    @Override
                    public void requestPermissionFailed() {
                        Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per  failed");
                        callback.invoke(origin, false, false);
                    }
                });
            }

            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        //支持多窗口时，需要实现
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    };
    private DownloadListener downListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.d(TAG, "onDownloadStart(), url:" + url + "\n userAgent:" + userAgent + "\ncontentDisposition:" + contentDisposition + "\nmimetype:" + mimetype + "\ncontentLength:" + contentLength);
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Toast.makeText(getContext(), "Not SDCard!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //TODO
                //下载地址跳转到系统浏览器打开
                openUrl(url);
            }
        }
    };
    private RelativeLayout videoLayout;
    private RelativeLayout videoFullLayout;
    private View loadingView;

    public void openUrl(String url) {
        if (URLUtil.isNetworkUrl(url)) {
            // 防止有大写
            url = url.replace(url.substring(0, 7), url.substring(0, 7)
                    .toLowerCase());
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(it);
        }
    }

    private void updateTitle(String title) {
        if (null != listener) {
            if (isLoadFailed) {
                title = "";
            }
            listener.updateTitle(title);
        }
    }

    private OnClickListener errorViewListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != webView)
                webView.reload();
            v.setVisibility(View.GONE);
        }
    };
    private Context mContext;
    private OnTitleListener listener;

    public WebWidget(Context context) {
        super(context);
        init(context);
    }

    public WebWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        mPermissionUtil = new PermissionUtil();

        webView = new MWebView(context);
        progress = new WebViewProgress(context);
        errorView = new TextView(context);
        ((TextView) errorView).setText("轻触屏幕重新加载");
        ((TextView) errorView).setGravity(Gravity.CENTER);
        errorView.setBackgroundColor(Color.WHITE);
        errorView.setVisibility(View.GONE);
        loadingView = View.inflate(getContext(), R.layout.view_loading_video, null);
        videoLayout = new RelativeLayout(context);
        videoFullLayout = new RelativeLayout(context);
        videoLayout.addView(webView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(videoLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(videoFullLayout, new LayoutParams(-1, -1));
        addView(progress, new LayoutParams(-1, dp2px(context, 2)));
        addView(errorView, new LayoutParams(-1, -1));
        if (chrome instanceof VideoEnabledWebChromeClient) {
            ((VideoEnabledWebChromeClient) chrome).init(videoLayout, videoFullLayout, loadingView, webView);
            if (mContext instanceof Activity) {

                ((VideoEnabledWebChromeClient) chrome).setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
                    @Override
                    public void toggledFullscreen(boolean fullscreen) {
                        // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                        if (fullscreen) {
                            WindowManager.LayoutParams attrs = ((Activity) mContext).getWindow().getAttributes();
                            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            ((Activity) mContext).getWindow().setAttributes(attrs);
                            if (mContext instanceof AppCompatActivity) {
                                ActionBar bar = ((AppCompatActivity) mContext).getSupportActionBar();
                                if (null != bar)
                                    bar.hide();
                            }

                            if (android.os.Build.VERSION.SDK_INT >= 14) {
                                //noinspection all
                                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                            }
                        } else {
                            WindowManager.LayoutParams attrs = ((Activity) mContext).getWindow().getAttributes();
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            ((Activity) mContext).getWindow().setAttributes(attrs);
                            if (mContext instanceof AppCompatActivity) {
                                ActionBar bar = ((AppCompatActivity) mContext).getSupportActionBar();
                                if (null != bar)
                                    bar.show();
                            }
                            if (android.os.Build.VERSION.SDK_INT >= 14) {
                                //noinspection all
                                ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                            }
                        }

                    }
                });
            }

        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void mayByLoadFailed(float pro) {
        Log.w(TAG, "mayByLoadFailed, pro = " + pro + ", curPro = " + progress.curProgress());
        if (progress.curProgress() >= pro) {
            return;
        }
        if (null != errorView) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setOnClickListener(errorViewListener);
        }
        webView.setVisibility(View.GONE);
        isLoadFailed = true;
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
        webView.setWebViewClient(client);
        webView.setWebChromeClient(chrome);
        webView.setDownloadListener(downListener);
        webView.setVisibility(View.GONE);
    }

    public boolean canBack() {
        if (chrome instanceof VideoEnabledWebChromeClient) {
            if (!((VideoEnabledWebChromeClient) chrome).onBackPressed()) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }

        if (webView.canGoBack()) {
            webView.goBack();
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setUpdateTitleListener(OnTitleListener listener) {
        this.listener = listener;
    }

    public interface OnTitleListener {

        void updateTitle(String title);

    }
}
