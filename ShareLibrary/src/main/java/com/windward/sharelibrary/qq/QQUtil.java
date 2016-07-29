package com.windward.sharelibrary.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.windward.sharelibrary.ShareValues;

import org.json.JSONObject;

import java.util.ArrayList;

public class QQUtil {
    private Activity act;

    public QQUtil(Activity act) {
        this.act = act;
    }

        private IUiListener listener = new MyBaseUiListener(act) {
        @Override
        protected void doComplete(JSONObject values) {
            super.doComplete(values);
            initOpenidAndToken(values);
        }
    };

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            Log.d("bank", openId + "");
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private Tencent mTencent = null;

    public Tencent getInstance(Context context) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(QQValues.APP_ID, context.getApplicationContext());
        }
        return mTencent;
    }

    public void loginWithQQ(final Activity context) {
        Tencent tencent = Tencent.createInstance(QQValues.APP_ID, context);
        tencent.login(context, "all", listener);
    }

    /**
     * QQ好友图文分享
     */
    public void sendImageTextMessageToQQ(final Activity context,
                                         String title, String summary, String bitmapPath, String target_url) {
        Tencent tencent = Tencent.createInstance(QQValues.APP_ID, context.getApplicationContext());
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, TextUtils
            .isEmpty(target_url) ? QQValues.REDIRECT_URL : target_url);

        if (bitmapPath != null) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                bitmapPath);
        }
/*

        if (bitmapPath != null) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
                bitmapPath);
        }
*/
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ShareValues.QQ_APP_NAME);
        tencent.shareToQQ(context, params, listener);
    }

    /**
     * 好友图片分享
     * */
    public void sendImageMessageToQQ(final Activity context, String bitmapPath, String target_url) {
        Tencent tencent = Tencent.createInstance(QQValues.APP_ID, context);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
            QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "智慧出行");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);
        if (bitmapPath != null) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
                bitmapPath);
        }
        tencent.shareToQQ(context, params, listener);
    }

    /**
     * 好友文字分享
     *
     * @param context
     * @param title
     * @param summary
     */

    public void sendMessageToQQ(final Activity context, String title,
                                String summary, String targetUrl) {
        Tencent mTencent = getInstance(context.getApplicationContext());
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        mTencent.shareToQQ(context, params, listener);
    }

    /**
     * 空间文字分享
     * */
    public void sendTextMessageToQzone(final Activity context,
                                       String title, String summary, String target_url, ArrayList imageUrlList) {
        mTencent = getInstance(context.getApplicationContext());
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, TextUtils
            .isEmpty(target_url) ? QQValues.REDIRECT_URL : target_url);// 必填;
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrlList);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        mTencent.shareToQzone(context, params, listener);
    }


}
