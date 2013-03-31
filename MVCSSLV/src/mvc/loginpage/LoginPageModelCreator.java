package mvc.loginpage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mvc.IModel;
import mvc.IModelCreator;
import net.voaideahost.Util;
import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;
import dao.users.UsersDAO;
import dao.users.UsersDAOImpl;
import domain.loginpage.Users;

public class LoginPageModelCreator implements IModelCreator {

	private final UsersDAO dao = new UsersDAOImpl();

	public IModel createModel(HttpServletRequest r) {

		LoginPageModel lm = new LoginPageModel();
		HttpSession hs = r.getSession();

		// Log out
		String lo = r.getParameter("logoff");
		if (lo != null) {
			lm.setUserName(null);
			lm.setPassword(null);
			lm.setStatusMessage(LOGGED_OUT);
			hs.removeAttribute("username");
			hs.removeAttribute("logoff");
		}

		// Log In
		String ru = r.getParameter("username");
		String su = (String) hs.getAttribute("username");

		// Check User
		boolean userChecked = false;
		if (ru != null) {
			Users u = dao.getUserById(ru);
			lm.setUser(u);
			userChecked = u != null;
		}

		// Double Check User
		if (su == null && userChecked) {
			hs.setAttribute("username", ru);
			if (hs.getAttribute("logoff") != null) {
				hs.removeAttribute("logoff");
			}

			lm.setUserName(ru);
			lm.setPassword("P-hash: ".concat(new Util().getSha(ru,
					ru.concat(ru))));
			lm.setStatusMessage(LOGGED_IN);
		} else if ((ru == null && su != null) || (ru != null && su != null)) {
			Users u = dao.getUserById(su);
			lm.setUser(u);
			lm.setUserName(su);
			lm.setPassword("P-hash: ".concat(new Util().getSha(su,
					su.concat(su))));
			lm.setStatusMessage(LOGGED_IN);
		} else if (ru != null && !userChecked && su == null) {
			lm.setStatusMessage(NO_SUCH_USER);
		} else if (ru == null && su == null) {
			lm.setStatusMessage(NOT_LOGGED_IN);
		}

		// Pages
		int page = 1;
		if (r.getSession().getAttribute("loginpage") != null)
			page = Integer.parseInt(r.getSession().getAttribute("loginpage")
					.toString());
		if (r.getParameter("page") != null) {
			try {
				page = Integer.parseInt(r.getParameter("page"));
			} catch (NumberFormatException e) {
				if (r.getSession().getAttribute("loginpage") == null) {
					page = 1;
				} else {
					page = Integer.parseInt(r.getSession()
							.getAttribute("loginpage").toString());
				}
			}
			r.getSession().setAttribute("loginpage", page);
		}

		if (lm.getUserName() != null) {
			AdsDAO dao = new AdsDAOImpl();
			int adsCount = dao.getCountByUser(lm.getUserName());
			lm.setListingSize(adsCount);

			int maxPage = Math.round((float) adsCount / (float) ADS_PER_PAGE) + 1;
			if (page > maxPage)
				page = maxPage;
			if (page <= 0)
				page = 1;
		} else {
			page = 1;
		}

		lm.setCurrentPage(page);

		return lm;
	}
}
