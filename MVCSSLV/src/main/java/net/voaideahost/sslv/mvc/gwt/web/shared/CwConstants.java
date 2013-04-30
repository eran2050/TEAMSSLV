package net.voaideahost.sslv.mvc.gwt.web.shared;

import static net.voaideahost.sslv.common.Config.VAL_APP_VERSION;

import com.google.gwt.i18n.client.Constants;

public interface CwConstants extends Constants {

	@DefaultStringValue (VAL_APP_VERSION)
	String cwAppVersion();

}
