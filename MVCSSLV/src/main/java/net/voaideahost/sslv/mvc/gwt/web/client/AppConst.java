package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {

	@DefaultStringValue("v5.0.6_5")
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
	int VAL_ADS_PER_VIEW_MODE_ALL();

	// TODO Razobrat'sja s kol-vom Ads dlja User-Only-Mode - nuzhno 20!
	@DefaultIntValue(20)
	int VAL_ADS_PER_VIEW_MODE_USER();

	@DefaultIntValue(15)
	int VAL_PAGES_IN_LINE();

	@DefaultIntValue(1812)
	int VAL_MAX_IDLE_TIME();

	@DefaultStringValue("Initializing&nbsp;")
	String VAL_INITIALIZING();

	@DefaultStringValue("Loading&nbsp;")
	String VAL_LOADING();

	@DefaultStringValue("Total Ads: ")
	String VAL_TOTAL_ADS();

	@DefaultStringValue("View Mode: All")
	String VAL_VIEW_MODE_ALL();

	@DefaultStringValue("View Mode: User Only")
	String VAL_VIEW_MODE_USER();

	@DefaultStringValue("Not Logged In")
	String STATUS_NOT_LOGGED_IN();

	@DefaultStringValue("Logging In&nbsp;")
	String STATUS_LOGGING_IN();

	@DefaultStringValue("Logged In")
	String STATUS_LOGGED_IN();

	@DefaultStringValue("Logging Out&nbsp;")
	String STATUS_LOGGING_OUT();

	@DefaultStringValue("Logged Out")
	String STATUS_LOGGED_OUT();

	@DefaultStringValue("Logged Out (idle timeout)")
	String STATUS_LOGGED_OUT_IDLE();

	@DefaultStringValue("Advertisment has been removed")
	String ACTION_AD_DELETED();

	@DefaultStringValue("Saving&nbsp;")
	String ACTION_SAVING();

	@DefaultStringValue("Advertisment has been updated")
	String ACTION_AD_UPDATED();

	@DefaultStringValue("Not Authorized!")
	String ACTION_NOT_AUTHORIZED();

	@DefaultStringValue("Not Allowed to open Two or more View/Edit boxes!")
	String ACTION_SECOND_AD_VIEW_EDIT();

	@DefaultStringValue("Failed logging in!")
	String ACTION_LOGGING_IN_FAILED();

	@DefaultStringValue("Ready")
	String ACTION_READY();
}
