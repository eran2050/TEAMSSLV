package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {
	@DefaultStringValue("v5.0.0_12")
	String APP_VERSION();

	@DefaultStringValue("")
	String VAL_EMPTY();

	@DefaultIntValue(15)
	int VAL_ADS_PER_MAIN_PAGE();
}
