package com.windward.sharelibrary;

/**
 * 配置分享信息
 * <p/>
 * 所有平台分享时的参数值设定
 * <p/>
 * Created by ww on 2016/1/20.
 */
public interface ShareValues {
    /**
     * QQ分享时的分享信息
     */
    String QQ_APP_ID = "1105114037";
    String QQ_APP_NAME = "cn.msh.mobilebank.android.user.u.BaseApplication";
    String QQ_REDIRECT_URL = "http://114.215.208.188:8082/share/";

    /**
     * 微博分享时的分享信息
     */
    String WB_APP_KEY = "2834490125";
    String WB_APP_SECRET = "55e7ce8b3148c05b0153e66d8c57f885";
    String WB_REDIRECT_URL = "http://www.sina.com";

    /**
     * 微信分享时的信息
     */
    String WX_APP_ID = "wxb6f7262d8d16b9ca";
    String WX_APP_SECRET = "7d8a320a8a5c921ff35970e2a4635162";
    String WX_REDIRECT_URL = "http://114.215.208.188:8082/share/";

}
