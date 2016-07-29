package com.windward.sharelibrary.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.windward.sharelibrary.BitmapUtils;

import java.io.ByteArrayOutputStream;

public class WXUtils {

    private static IWXAPI iwxapi;

    public WXUtils(Context context) {
        this.iwxapi = WXAPIFactory.createWXAPI(context, WXValues.WX_APP_ID);
    }

    public void loginWithWeiXin(Context context) {
//        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, WXValues.WX_APP_ID);
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context, "您还未安装微信客户端,请先下载安装最新的微信手机客户端。",
                Toast.LENGTH_LONG).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "feno_weixin_login";
        iwxapi.sendReq(req);
    }

    public void sendWebMessageToWeiXin(Context context, int type,
                                       String title, String description, String webpageUrl,
                                       Bitmap thumpBitmap) {
//        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, WXValues.WX_APP_ID);
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context, "您还未安装微信客户端,请先下载安装最新的微信手机客户端。",
                Toast.LENGTH_LONG).show();
            return;
        }
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpageObject;
        if (!TextUtils.isEmpty(title)) {
            msg.title = title;
        }
        if (!TextUtils.isEmpty(description)) {
            msg.description = description;
        }
        if (thumpBitmap != null) {
            msg.thumbData = BitmapUtils.bmpToByteArray(
                Bitmap.createScaledBitmap(thumpBitmap, 150, 150, true),
                true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (type == 0) {// 微信好友
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 1) {// 微信朋友圈
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        iwxapi.sendReq(req);
    }

    public void sendImageMessageToWeiXin(Context context, int type,
                                         Bitmap bitmap, String title, String description) {

//        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, WXValues.WX_APP_ID);
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context, "您还未安装微信客户端,请先下载安装最新的微信手机客户端。",
                Toast.LENGTH_LONG).show();
            return;
        }
        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = BitmapUtils.saveTempBitmap(context, bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        if (!TextUtils.isEmpty(title)) {
            msg.title = title;
        }
        if (!TextUtils.isEmpty(description)) {
            msg.description = description;
        }
        if (bitmap != null) {
            msg.thumbData = BitmapUtils.bmpToByteArray(
                    Bitmap.createScaledBitmap(bitmap, 175, 175, true), true);
        }
//        if (bitmap != null) {
//            msg.thumbData = bmpToByteArray(bitmap);
//        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (type == 0) {// 微信好友
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 1) {// 微信朋友圈
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        iwxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
            : (type + System.currentTimeMillis());
    }

    private byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
