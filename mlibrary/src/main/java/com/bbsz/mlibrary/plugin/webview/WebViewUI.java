package com.bbsz.mlibrary.plugin.webview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.FrameLayout;

public class WebViewUI extends AppCompatActivity implements WebWidget.OnTitleListener {

    private WebWidget mWebWidget;

    private String testUrl = "http://www.bb-sz.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebWidget = new WebWidget(this);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(mWebWidget, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        testUrl = getIntent().getStringExtra("url");
        if (!URLUtil.isValidUrl(testUrl)) {
            finish();
            return;
        }
        mWebWidget.loadUrl(testUrl);

        mWebWidget.setUpdateTitleListener(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWebWidget.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (mWebWidget.canBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.webview_menu, menu);

        // Retrieve the share menu item
//        MenuItem shareItem = menu.findItem(R.id.menu_share);

        // Now get the ShareActionProvider from the item
//        mMoreActionProvider = (MoreActionProvider) MenuItemCompat.getActionProvider(shareItem);

//        return super.onCreateOptionsMenu(menu);
//        MenuItem moreItem = menu.add(0, 0, 0, "More");
//        android.R.drawable.ic_menu_more;
//        moreItem.setIcon(android.R.drawable.ic_menu_moreoverflow_normal_holo_dark);
//        moreItem.setIcon(android.R.drawable.ic_menu_more);

//        MenuItemCompat.setShowAsAction(moreItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTitle(String title) {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }
}
