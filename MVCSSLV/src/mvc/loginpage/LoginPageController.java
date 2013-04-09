package mvc.loginpage;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import domain.mainpage.Ads;
import mvc.IController;
import mvc.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Component
public class LoginPageController implements IController {

	@Autowired
	private AdsDAO ads;

	@Autowired
	private AdDescDAO adDesc;

	public void execute(IModel model, HttpServletRequest req) {

		StringBuilder sLoginForm = new StringBuilder();
		sLoginForm
				.append("<form action=")
				.append(CONTEXT_ROOT)
				.append("login/ method=post>")
				.append("Input: <input type=text align=center name=username />")
				.append("<input type=hidden name=action value=login />")
				.append("<input type=image src=")
				.append(CONTEXT_ROOT)
				.append("images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
				.append("</form>");

		StringBuilder sLogoutForm = new StringBuilder();
		sLogoutForm
				.append("<a href=")
				.append(CONTEXT_ROOT)
				.append("login?action=logout>")
				.append("Log Out:&nbsp;<input type=hidden align=center name=logoff value=logout />")
				.append("<img src=")
				.append(CONTEXT_ROOT)
				.append("images/logout.jpg HEIGHT=23 align=center border=0 alt=Go />")
				.append("</a>");

		LoginPageModel lm = (LoginPageModel) model;
		lm.setAppVersion(APP_VERSION);
		lm.setAds(null);
		lm.setValid(false);

		// User not found
		if (lm.getStatusMessage().equals(NO_SUCH_USER)) {
			lm.setStatusMessage(NO_SUCH_USER);
			lm.setHtmlForm(sLoginForm.toString());
			lm.setStatus(NOT_LOGGED_IN);
		} else

		// After Log In or refresh
		if (lm.getStatusMessage().equals(LOGGED_IN)
				&& (lm.getAction().equals(ACTION_LOGIN) || lm.getAction()
						.equals(EMPTY))) {
			lm.setStatusMessage(LOGGED_IN);
			lm.setHtmlForm(sLogoutForm.toString());
			lm.setStatus(LOGGED_IN + " as ");
			lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
		} else

		// Delete Ad
		if (lm.getAction().equals(ACTION_DELETE)) {
			Ads ad = ads.getById(lm.getAdsId().intValue());
			String adsOwner = ad == null ? EMPTY : ad.getOwner();
			String logUser = lm.getUserName() == null ? EMPTY + EMPTY : lm
					.getUserName();

			System.out.println("ad.getOwner()=" + adsOwner.toUpperCase());
			System.out.println("lm.getUserName()=" + logUser.toUpperCase());
			if (logUser != null && !logUser.equals(EMPTY)
					&& adsOwner.toUpperCase().equals(logUser.toUpperCase())) {
				adDesc.deleteByAdsId(lm.getAdsId().intValue());
				ads.deleteById(lm.getAdsId().intValue());
				lm.setStatusMessage(AD_DELETED);
				lm.setHtmlForm(sLogoutForm.toString());
				lm.setStatus(LOGGED_IN + " as ");
				lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
			} else if (logUser != null && !logUser.equals(EMPTY)
					&& !adsOwner.toUpperCase().equals(logUser.toUpperCase())) {
				lm.setStatusMessage(ACTION_NOT_AUTHORIZED);
				lm.setHtmlForm(sLogoutForm.toString());
				lm.setStatus(LOGGED_IN + " as ");
				lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
			} else {
				lm.setHtmlForm(sLoginForm.toString());
				lm.setStatusMessage(ACTION_NOT_AUTHORIZED);
				lm.setStatus(NOT_LOGGED_IN);
			}
		} else

		// After Log Out
		if (lm.getStatusMessage().equals(LOGGED_OUT)) {
			lm.setStatusMessage(LOGGED_OUT);
			lm.setHtmlForm(sLoginForm.toString());
			lm.setStatus(NOT_LOGGED_IN);
		} else {
			// Default
			lm.setStatusMessage(NOT_LOGGED_IN);
			lm.setHtmlForm(sLoginForm.toString());
			lm.setStatus(NOT_LOGGED_IN);
		}

		// Giving model to View
		if (lm.getUserName() == null) {
			lm.setPassword(EMPTY);
			lm.setUserName(EMPTY);
		} else {
			lm.setValid(true);
		}

		// PageNumbers
		ArrayList<String> list = new ArrayList<String>();
		int cnt = Math.round(lm.getListingSize() / ADS_PER_LOGIN_PAGE);
		if (cnt > 0) {
			int n;
			for (n = 1; n <= cnt + 1; n++) {
				if (n == lm.getCurrentPage()) {
					list.add("<a class=pages href=".concat(CONTEXT_ROOT)
							.concat("login/?page=").concat(Integer.toString(n))
							.concat("><b><u>").concat(Integer.toString(n))
							.concat("</u></b></a>&nbsp;"));
				} else {
					list.add("<a class=pages href=".concat(CONTEXT_ROOT)
							.concat("login/?page=").concat(Integer.toString(n))
							.concat(">").concat(Integer.toString(n))
							.concat("</a>&nbsp;"));
				}

				if (n % PAGES_IN_LINE == 0)
					list.add("<br>");
			}
			lm.setPageNumbers(list);
		}
	}
}
