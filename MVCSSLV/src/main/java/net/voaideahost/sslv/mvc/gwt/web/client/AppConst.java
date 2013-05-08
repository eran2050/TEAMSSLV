package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {
	@DefaultStringValue("v5.0.2_2")
	String APP_VERSION();

	@DefaultStringValue("")
	String VAL_EMPTY();

	@DefaultIntValue(20)
	int VAL_ADS_PER_MAIN_PAGE();

	@DefaultIntValue(10)
	int VAL_ADS_PER_LOGIN_PAGE();

	@DefaultIntValue(15)
	int VAL_PAGES_IN_LINE();

	@DefaultStringValue("Loading..")
	String VAL_LOADING();

	@DefaultStringValue("Not Logged In")
	String STATUS_NOT_LOGGED_IN();

	@DefaultStringValue("Logged In")
	String STATUS_LOGGED_IN();

	@DefaultStringValue("No Such User")
	String STATUS_NO_SUCH_USER();

	@DefaultStringValue("Logged Out")
	String STATUS_LOGGED_OUT();

	@DefaultStringValue("Advertisment has been removed")
	String STATUS_AD_DELETED();

	@DefaultStringValue("Advertisment has been updated")
	String AD_UPDATED();

	@DefaultStringValue("login")
	String ACTION_LOGIN();

	@DefaultStringValue("logout")
	String ACTION_LOGOUT();

	@DefaultStringValue("delete")
	String ACTION_DELETE();

	@DefaultStringValue("update")
	String ACTION_UPDATE();

	@DefaultStringValue("view")
	String ACTION_VIEW();

	@DefaultStringValue("edit")
	String ACTION_EDIT();

	@DefaultStringValue("Not Authorized!")
	String ACTION_NOT_AUTHORIZED();
}
