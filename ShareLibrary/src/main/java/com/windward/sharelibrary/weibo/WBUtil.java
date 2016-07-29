package com.windward.sharelibrary.weibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.windward.sharelibrary.R;


public class WBUtil extends Activity implements IWeiboHandler.Response {

    private Activity mActivity;

    private IWeiboShareAPI mIWeiboShareAPI;

    private TextView tvTitle, tvMsg, sure,tvUrl;
    private ImageView img;
    private String title;
    private String msg;
    private String pageUrl;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbauth);

        initWeiboSdk();
        initShareData();
    }

    private void initShareData() {
        tvTitle = (TextView) findViewById(R.id.tv_sina_title);
        tvMsg = (TextView) findViewById(R.id.tv_sina_msg);
        sure = (TextView) findViewById(R.id.tv_sina_sure);
        img = (ImageView) findViewById(R.id.iv_sina_img);
        tvUrl = (TextView) findViewById(R.id.tv_sina_url);

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            msg = intent.getStringExtra("msg");
            bmp = intent.getParcelableExtra("bmp");
            pageUrl = getIntent().getStringExtra("pageUrl");
        }

        tvTitle.setText(title);
        tvMsg.setText(msg);
        img.setImageBitmap(bmp);
        tvUrl.setText(pageUrl);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMultiMessage(title, msg, bmp, pageUrl);
            }
        });
    }

    private void initWeiboSdk() {
        mActivity = this;
        mIWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, WBValues.APP_KEY);
        mIWeiboShareAPI.registerApp();//注册
    }



    private void sendMultiMessage(String title, String text, Bitmap bitmap,
                                  String pageUrl) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        if (!TextUtils.isEmpty(text)) {
            weiboMessage.textObject = getTextObj(text);
        }

        if (bitmap != null) {
            weiboMessage.imageObject = getImageObj(bitmap);
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (!TextUtils.isEmpty(pageUrl)) {
            weiboMessage.mediaObject = getWebpageObj(title, text, bitmap,
                pageUrl, text);
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(mActivity, WBValues.APP_KEY, WBValues.REDIRECT_URL,
            WBValues.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper
            .readAccessToken(mActivity.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mIWeiboShareAPI.sendRequest(this, request, authInfo, token,
            new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                    showToast(arg0.getMessage().toString());
                    finish();
                }

                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken newToken = Oauth2AccessToken
                        .parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(mActivity, newToken);
                    sendResultToService();
                    showToast("分享成功");
                }

                @Override
                public void onCancel() {
                    showToast("取消分享");
                    finish();
                }
            });
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    public ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String title, String description,
                                        Bitmap thumpBitmap, String actionUrl, String webpageDefaultText) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = "";
        if (thumpBitmap != null) {
            mediaObject.setThumbImage(Bitmap.createScaledBitmap(thumpBitmap,
                200, 200, true));
        }
        mediaObject.actionUrl = actionUrl;
        return mediaObject;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mIWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    private void sendResultToService() {

    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                sendResultToService();
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                finish();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败");
                finish();
                break;
        }
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}
