# WebViewUI
WebViewUI 提供一个WebView的Activity，支持视频播放、位置定位等，模仿WeChat的WebViewUI的Activity。


#使用方法：
在任何地方调用:
        Intent i = new Intent(this, WebViewUI.class);
        i.putExtra("url", "http://www.bb-sz.com");
        startActivity(i);
 在AndroidManifest.xml中添加：
      <activity
            android:name="com.bbsz.mlibrary.plugin.webview.WebViewUI"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:process=":tools"
            android:hardwareAccelerated="true"
            android:screenOrientation="sensor">
        </activity>
        
      和权限：  
         <uses-permission android:name="android.permission.INTERNET" />
         <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
         <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
         <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
         
  在build.gradle中：
  
