package com.windward.sharelibrary.weibo;

import com.windward.sharelibrary.ShareValues;

/**
 * 微博分享的参数值，此处不设定，在ShareValues里面统一设定
 *
 * Created by ww on 2015/12/11.
 */
public interface WBValues {
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    String APP_KEY = ShareValues.WB_APP_KEY;
    String APP_SECRET = ShareValues.WB_APP_SECRET;

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    String REDIRECT_URL = ShareValues.WB_REDIRECT_URL;

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
}
