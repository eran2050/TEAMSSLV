package mvc.loginpage;

import java.util.ArrayList;

import mvc.IController;
import mvc.IModel;
import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;

public class LoginPageController implements IController {

	public IModel execute(IModel model) {

		LoginPageModel lm = (LoginPageModel) model;
		AdsDAO adsDao = new AdsDAOImpl();
		lm.setAds(null);
		lm.setValid(false);

		if (lm.getStatusMessage().equals(NO_SUCH_USER)) {
			lm.setStatusMessage(NO_SUCH_USER);
			lm.setHtmlForm("<form action=/java2/login/ method=post>"
					.concat("Input: <input type=text align=center name=username />")
					.concat("<input type=image src=/java2/images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
					.concat("</form>"));
			lm.setStatus(NOT_LOGGED_IN);

		} else if (lm.getStatusMessage().equals(LOGGED_IN)) {
			lm.setStatusMessage(LOGGED_IN);
			lm.setHtmlForm("<form action=/java2/login/ method=post>"
					.concat("Log Out:&nbsp;<input type=hidden align=center name=logoff value=logoff />")
					.concat("<input type=image src=/java2/images/logout.jpg HEIGHT=23 align=center border=0 alt=Go />")
					.concat("</form>"));
			lm.setStatus(LOGGED_IN + " as ");
			lm.setAds(adsDao.getByUser(lm.getUserName(), lm.getCurrentPage()));
		} else if (lm.getStatusMessage().equals(LOGGED_OUT)) {
			lm.setStatusMessage(LOGGED_OUT);
			lm.setHtmlForm("<form action=/java2/login/ method=post>"
					.concat("Input: <input type=text align=center name=username />")
					.concat("<input type=image src=/java2/images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
					.concat("</form>"));
			lm.setStatus(NOT_LOGGED_IN);
		} else {
			lm.setStatusMessage(NOT_LOGGED_IN);
			lm.setHtmlForm("<form action=/java2/login/ method=post>"
					.concat("Input: <input type=text align=center name=username />")
					.concat("<input type=image src=/java2/images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
					.concat("</form>"));
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
		int cnt = Math.round(lm.getListingSize() / 10);
		if (cnt > 0) {
			int n;
			for (n = 1; n <= cnt + 1; n++) {
				if (n == lm.getCurrentPage()) {
					list.add("<a class=pages href=/java2/login/?page="
							.concat(Integer.toString(n)).concat("><b><u>")
							.concat(Integer.toString(n))
							.concat("</u></b></a>&nbsp;"));
				} else {
					list.add("<a class=pages href=/java2/login/?page="
							.concat(Integer.toString(n)).concat(">")
							.concat(Integer.toString(n)).concat("</a>&nbsp;"));
				}

				// if (n % PAGES_IN_LINE == 0)
				// list.add("<br>");
			}
			lm.setPageNumbers(list);
		}

		return lm;
	}
}
