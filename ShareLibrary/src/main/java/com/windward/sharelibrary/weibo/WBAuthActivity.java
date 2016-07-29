package com.windward.sharelibrary.weibo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class WBAuthActivity extends Activity {

    private Context activity;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        mContext = this;
        authWithWeibo();
    }

    // 新浪微博认证
    private void authWithWeibo() {
        mAuthInfo = new AuthInfo(this, WBValues.APP_KEY,
                WBValues.REDIRECT_URL, WBValues.REDIRECT_URL);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSsoHandler.authorize(new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                finish();
            }

            @SuppressLint("NewApi")
            @Override
            public void onComplete(Bundle arg0) {
                System.out.println("arg  " + arg0);

                // Bundle[{access_token=2.00bxlRcFxc2jVBa39cb7c481x5WgHB,
                // refresh_token=2.00bxlRcFxc2jVBbbbac0b33e0tPvIz,
                // expires_in=132546, uid=5146400867,
                // scope=follow_app_official_microblog, remind_in=132546}]

                Oauth2AccessToken mAccessToken = Oauth2AccessToken
                        .parseAccessToken(arg0);
                if (mAccessToken.isSessionValid()) {
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
                    String uid = arg0.getString("uid", null);
                    if (!TextUtils.isEmpty(uid)) {
                        onAuthResult(uid);
                    } else {
                        finish();
                        // showFenoToastDialog("新浪微博登录失败，请稍后再试或换其他方式登录。", null,
                        // new FeNOListener() {
                        //
                        // @Override
                        // public void onMenuClicked(int menuIndex) {
                        // finish();
                        // }
                        // });
                    }
                } else {
                    // 以下几种情况，您会收到 Code：
                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
                    // 2. 当您注册的应用程序包名与签名不正确时；
                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = arg0.getString("code");
                    String message = "error";
                    if (!TextUtils.isEmpty(code)) {
                        message = message + "\nObtained the code: " + code;
                    }
                    finish();
                    // showFenoToastDialog(message, null, new FeNOListener() {
                    //
                    // @Override
                    // public void onMenuClicked(int menuIndex) {
                    // finish();
                    // }
                    // });
                }
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }

    private void onAuthResult(String uid) {
        Intent intent = new Intent();
        intent.putExtra("uid", uid);
        setResult(9009, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
