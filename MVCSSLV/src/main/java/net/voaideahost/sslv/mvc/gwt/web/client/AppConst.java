package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.i18n.client.Constants;

public interface AppConst extends Constants {
	@DefaultStringValue ("v5.0.0_9")
	String APP_VERSION();

	@DefaultIntValue (20)
	int VAL_ADS_PER_MAIN_PAGE();
}
