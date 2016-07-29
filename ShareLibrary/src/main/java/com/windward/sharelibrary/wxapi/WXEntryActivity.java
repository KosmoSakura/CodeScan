package com.windward.sharelibrary.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WXValues.WX_APP_ID,false);
        api.registerApp(WXValues.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        if (SendAuth.Resp.class.isInstance(resp)) {
            SendAuth.Resp result = (SendAuth.Resp) resp;
            if (result.errCode == BaseResp.ErrCode.ERR_OK) {
                Intent intent = new Intent("feno_action_wx_auth_success");
//                intent.putExtra("token", result.token);
                sendBroadcast(intent);
            }
            finish();
        } else {
            String result = "";
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK://成功
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://取消
                    result = "发送取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://被拒绝
                    result = "认证失败";
                    break;
                default:
                    result = "发送返回";//返回
                    break;
            }
            Toast.makeText(this, result + "", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}