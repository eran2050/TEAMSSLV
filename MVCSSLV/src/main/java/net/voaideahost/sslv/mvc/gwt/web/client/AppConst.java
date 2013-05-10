package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {

	@DefaultStringValue("v5.0.4_0")
	String APP_VERSION();

	@DefaultStringValue("/java2/")
	String VAL_CONTEXT_ROOT();

	@DefaultStringValue("SessionID")
	String VAL_COOKIE();

	@DefaultStringValue("")
	String VAL_EMPTY();

	@DefaultIntValue(1000)
	int VAL_SECOND_MS();

	@DefaultIntValue(20)
	int VAL_ADS_PER_MAIN_PAGE();

	@DefaultIntValue(10)
	int VAL_ADS_PER_LOGIN_PAGE();

	@DefaultIntValue(15)
	int VAL_PAGES_IN_LINE();

	@DefaultIntValue(300)
	int VAL_MAX_IDLE_TIME();

	@DefaultStringValue("Initializing ..")
	String VAL_INITIALIZING();

	@DefaultStringValue("Loading ..")
	String VAL_LOADING();

	@DefaultStringValue("Total Ads: ")
	String VAL_TOTAL_ADS();

	@DefaultStringValue("Not Logged In")
	String STATUS_NOT_LOGGED_IN();

	@DefaultStringValue("Logging In ..")
	String STATUS_LOGGING_IN();

	@DefaultStringValue("Logged In")
	String STATUS_LOGGED_IN();

	@DefaultStringValue("Logging Out ..")
	String STATUS_LOGGING_OUT();

	@DefaultStringValue("Logged Out")
	String STATUS_LOGGED_OUT();

	@DefaultStringValue("Advertisment has been removed")
	String ACTION_AD_DELETED();

	@DefaultStringValue("Saving..")
	String ACTION_SAVING();

	@DefaultStringValue("Advertisment has been updated")
	String ACTION_AD_UPDATED();

	@DefaultStringValue("Not Authorized!")
	String ACTION_NOT_AUTHORIZED();

	@DefaultStringValue("Failed logging in")
	String ACTION_LOGGING_IN_FAILED();

	@DefaultStringValue("view_mode_all")
	String VIEW_MODE_ALL();

	@DefaultStringValue("view_mode_user")
	String VIEW_MODE_USER();
}
