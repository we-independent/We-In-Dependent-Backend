package com.weindependent.commons;


/**
 * @author zetor
 */
public interface AuthVariable {

	//=====================================LOG start=====================================

	Integer TOKEN_MAX_LIVE_SECONDS = 28800;

	//=====================================LOG  end=====================================
	String HEADER_TOKEN_KEY = "x-token";

	String SSO_SESSIONID = "sso_sessionid";

}
