package com.windward.sharelibrary.qq;


import com.windward.sharelibrary.ShareValues;

/**
 * QQ分享的参数值，此处不设定，在ShareValues里面统一设定
 *
 * Created by ww on 2015/12/14.
 */
public interface QQValues {
    String APP_ID = ShareValues.QQ_APP_ID; //应用申请的appId
    String REDIRECT_URL = ShareValues.QQ_REDIRECT_URL;//分享重定向URL
}
