package com.windward.sharelibrary;

public interface HttpRequestCompleteListener {
	public void onRequestStart();

	public void onRequestSuccess(ResponseMsg responseMsg);

	public void onRequestFail(int errorCode);
}