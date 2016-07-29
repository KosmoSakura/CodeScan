package com.windward.sharelibrary;

import java.io.Serializable;

public class ResponseMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 366351246002077975L;

	private int statue;
	private String message;
	private String response;
	private int errorCode;
	private String code;
	private int new_sys_msg_count;
	private int new_user_msg_count;
	private int verify_status;
	private int need_login;

	public int getVerify_status() {
		return verify_status;
	}

	public void setVerify_status(int verify_status) {
		this.verify_status = verify_status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getResult() {
		return statue;
	}

	public void setResult(int result) {
		this.statue = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ResponseMsg [result=" + statue + ", message=" + message
				+ ", response=" + response + ", code=" + code + "]";
	}

	public int getNew_sys_msg_count() {
		return new_sys_msg_count;
	}

	public void setNew_sys_msg_count(int new_sys_msg_count) {
		this.new_sys_msg_count = new_sys_msg_count;
	}

	public int getNew_user_msg_count() {
		return new_user_msg_count;
	}

	public void setNew_user_msg_count(int new_user_msg_count) {
		this.new_user_msg_count = new_user_msg_count;
	}

	public int getNeed_login() {
		return need_login;
	}

	public void setNeed_login(int need_login) {
		this.need_login = need_login;
	}

}
