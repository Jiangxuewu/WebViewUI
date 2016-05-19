package com.bbsz.mlibrary.plugin.webview;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2016/5/18.
 */
/*public*/ class JsCallJava {

    private final Context context;

    public JsCallJava(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void finish() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}
