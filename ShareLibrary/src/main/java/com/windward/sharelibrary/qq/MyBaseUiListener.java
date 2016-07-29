package com.windward.sharelibrary.qq;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Zero on 2016/2/18.
 */
public class MyBaseUiListener implements IUiListener {
    private Activity act;

    public MyBaseUiListener(Activity act) {
        this.act = act;
    }

    @Override
    public void onComplete(Object response) {
        if (null == response) {
            Util.showResultDialog(act, "返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Util.showResultDialog(act, "返回为空", "登录失败");
            return;
        }
        Util.showResultDialog(act, response.toString(),
            "登录成功");
        // 有奖分享处理
        doComplete((JSONObject) response);
    }

    protected void doComplete(JSONObject values) {

    }

    @Override
    public void onError(UiError e) {
        Util.toastMessage(act, "onError: " + e.errorDetail);
        Util.dismissDialog();
    }

    @Override
    public void onCancel() {
        Util.toastMessage(act, "onCancel: ");
        Util.dismissDialog();
    }
}
