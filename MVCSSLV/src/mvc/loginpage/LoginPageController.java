package mvc.loginpage;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import util.Config;
import util.Util;
import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import dao.users.UsersDAO;
import domain.ads.Ads;
import domain.users.Users;

@Component
@Controller(value = "/login/")
public class LoginPageController extends AbstractController implements Config {

	private final Logger logger = LoggerFactory.getLogger(LoginPageController.class);
	
	@Autowired
	private AdsDAO ads;
	
	@Autowired
	private AdDescDAO adDesc;
	
	@Autowired
	private UsersDAO users;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long loadStart = System.nanoTime();
		ModelAndView model = new ModelAndView("loginpage");

		// Model Creator
		LoginPageModel lm = new LoginPageModel();
		lm.setStatusMessage(EMPTY);
		HttpSession hs = request.getSession();

		// Actions
		String action = request.getParameter("action");
		if (action == null) {
			action = EMPTY;
		}
		lm.setAction(action);
		logger.debug("action = ".concat(lm.getAction()));

		// Log out
		if (lm.getAction().equals(ACTION_LOGOUT)) {
			lm.setStatusMessage(LOGGED_OUT);
			lm.setUserName(null);
			lm.setPassword(null);
			hs.removeAttribute(USERNAME);
		}

		// Log In
		String ru = null;
		String su = (String) hs.getAttribute(USERNAME);
		boolean userChecked = false;
		if (lm.getAction().equals(ACTION_LOGIN)) {
			ru = request.getParameter(USERNAME);
			// Check User
			if (ru != null) {
				logger.debug("Authorizing User [1]..");
				Users u = users.getUserById(ru);
				lm.setUser(u);
				userChecked = u != null;
			}
		}

		// Delete ad
		if (lm.getAction().equals(ACTION_DELETE)) {
			lm.setStatusMessage(AD_DELETED);
			int adsId = Integer.parseInt(request.getParameter("adsid"));
			lm.setAdsId((long) adsId);
			logger.debug("adsId for deletion: " + lm.getAdsId().toString());
		}

		// Various checks and Status Setter
		if (su == null && userChecked) {
			hs.setAttribute(USERNAME, ru);

			lm.setUserName(ru);
			lm.setPassword("P-hash: ".concat(new Util().getSha(ru,
					ru.concat(ru))));
			lm.setStatusMessage(LOGGED_IN);
			logger.info(LOGGED_IN);
		} else if ((ru == null && su != null) || (ru != null && su != null)) {
			logger.debug("LOGIN: Authorizing User [2]..");
			Users u = users.getUserById(su);
			lm.setUser(u);
			lm.setUserName(su);
			lm.setPassword("P-hash: ".concat(new Util().getSha(su,
					su.concat(su))));
			lm.setStatusMessage(LOGGED_IN);
			logger.info(LOGGED_IN);
		} else if (ru != null && !userChecked && su == null) {
			lm.setStatusMessage(NO_SUCH_USER);
			logger.warn(NO_SUCH_USER);
		} else if (ru == null && su == null && lm.getAction().equals(EMPTY)) {
			lm.setStatusMessage(NOT_LOGGED_IN);
			logger.info(NOT_LOGGED_IN);
		}

		// Controller
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

		lm.setAppVersion(APP_VERSION);
		lm.setAds(null);
		lm.setValid(false);

		// Pages
		int page = 1;
		if (request.getParameter("page") != null) {
			try {
				page = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		lm.setCurrentPage(page);

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

			if (lm.getUserName() != null) {
				logger.debug("Getting count of Ads by User..");
				int adsCount = ads.getCountByUser(lm.getUserName());
				lm.setListingSize(adsCount);

				int maxPage = Math.round((float) adsCount
						/ (float) ADS_PER_LOGIN_PAGE) + 1;
				if (page > maxPage)
					page = maxPage;
				if (page <= 0)
					page = 1;
			} else {
				page = 1;
			}
			lm.setCurrentPage(page);

			logger.debug("Getting Ads for showing up the list..");
			lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
		} else

		// Delete Ad
		if (lm.getAction().equals(ACTION_DELETE)) {
			logger.debug("Getting authorization for Deletion..");
			Ads ad = ads.getById(lm.getAdsId().intValue());
			String adsOwner = ad == null ? EMPTY : ad.getOwner();
			String logUser = lm.getUserName() == null ? EMPTY + EMPTY : lm
					.getUserName();

			if (logUser != null && !logUser.equals(EMPTY)
					&& adsOwner.toUpperCase().equals(logUser.toUpperCase())) {
				logger.debug("Deleting Ad..");
				adDesc.deleteByAdsId(lm.getAdsId().intValue());
				ads.deleteById(lm.getAdsId().intValue());
				lm.setStatusMessage(AD_DELETED);
				lm.setHtmlForm(sLogoutForm.toString());
				lm.setStatus(LOGGED_IN + " as ");

				if (lm.getUserName() != null) {
					logger.debug("Getting count of Ads by User..");
					int adsCount = ads.getCountByUser(lm.getUserName());
					lm.setListingSize(adsCount);

					int maxPage = Math.round((float) adsCount
							/ (float) ADS_PER_LOGIN_PAGE) + 1;
					if (page > maxPage)
						page = maxPage;
					if (page <= 0)
						page = 1;
				} else {
					page = 1;
				}
				lm.setCurrentPage(page);

				logger.debug("Getting Ads after Deletion..");
				lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
			} else if (logUser != null && !logUser.equals(EMPTY)
					&& !adsOwner.toUpperCase().equals(logUser.toUpperCase())) {
				lm.setStatusMessage(ACTION_NOT_AUTHORIZED);
				logger.warn(ACTION_NOT_AUTHORIZED);
				lm.setHtmlForm(sLogoutForm.toString());
				lm.setStatus(LOGGED_IN + " as ");

				if (lm.getUserName() != null) {
					logger.debug("Getting count of Ads by User..");
					int adsCount = ads.getCountByUser(lm.getUserName());
					lm.setListingSize(adsCount);

					int maxPage = Math.round((float) adsCount
							/ (float) ADS_PER_LOGIN_PAGE) + 1;
					if (page > maxPage)
						page = maxPage;
					if (page <= 0)
						page = 1;
				} else {
					page = 1;
				}
				lm.setCurrentPage(page);

				logger.debug("Getting ads after not authorized Deletion..");
				lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
			} else {
				lm.setHtmlForm(sLoginForm.toString());
				lm.setStatusMessage(ACTION_NOT_AUTHORIZED);
				logger.warn(ACTION_NOT_AUTHORIZED);
				lm.setStatus(NOT_LOGGED_IN);
				logger.info(NOT_LOGGED_IN);
			}
		} else

		// After Log Out
		if (lm.getStatusMessage().equals(LOGGED_OUT)) {
			lm.setStatusMessage(LOGGED_OUT);
			logger.info(LOGGED_OUT);
			lm.setHtmlForm(sLoginForm.toString());
			lm.setStatus(NOT_LOGGED_IN);
			logger.info(NOT_LOGGED_IN);
		} else {
			// Default
			lm.setStatusMessage(NOT_LOGGED_IN);
			lm.setHtmlForm(sLoginForm.toString());
			lm.setStatus(NOT_LOGGED_IN);
			logger.info(NOT_LOGGED_IN);
		}

		// PageNumbers
		ArrayList<String> list = new ArrayList<String>();
		int cnt = Math.round(lm.getListingSize() / ADS_PER_LOGIN_PAGE);
		if (cnt > 0) {
			int n;
			if (cnt * ADS_PER_LOGIN_PAGE == lm.getListingSize())
				cnt -= 1;
			if (cnt > 0) {
				for (n = 1; n <= cnt + 1; n++) {
					if (n == lm.getCurrentPage()) {
						list.add("<a class=pages href=".concat(CONTEXT_ROOT)
								.concat("login/?page=")
								.concat(Integer.toString(n)).concat("><b><u>")
								.concat(Integer.toString(n))
								.concat("</u></b></a>&nbsp;"));
					} else {
						list.add("<a class=pages href=".concat(CONTEXT_ROOT)
								.concat("login/?page=")
								.concat(Integer.toString(n)).concat(">")
								.concat(Integer.toString(n))
								.concat("</a>&nbsp;"));
					}

					if (n % PAGES_IN_LINE == 0)
						list.add("<br>");
				}
			} else
				list = null;
			lm.setPageNumbers(list);
		}

		// Giving model to View
		if (lm.getUserName() == null) {
			lm.setPassword(EMPTY);
			lm.setUserName(EMPTY);
		} else {
			lm.setValid(true);
		}
		// Elapsed loading time calculations
		long loadEnd = System.nanoTime();
		double loadElapsedTime = (double) (loadEnd - loadStart) / 1000000.0;
		lm.setLoadingTime(Math.round(loadElapsedTime));
		//
		model.addObject("modelLoginPage", lm);
		return model;
	}
}
