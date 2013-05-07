package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {
	@DefaultStringValue("v5.0.1_10")
	String APP_VERSION();

	@DefaultStringValue("")
	String VAL_EMPTY();

	@DefaultIntValue(15)
	int VAL_ADS_PER_MAIN_PAGE();

	@DefaultStringValue("Status: Loading..")
	String VAL_LOADING();
}
