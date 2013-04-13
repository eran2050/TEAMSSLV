package mvc.mainpage;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import util.Config;
import dao.ads.AdsDAO;

@Component
@Controller(value="/")
public class MainPageController extends AbstractController implements Config {

	@Autowired
	private AdsDAO ads;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("mainpage");
		MainPageModel m = new MainPageModel();

		// Model Creator
		String userName = null;
		if (request.getSession().getAttribute(USERNAME) != null) {
			userName = (String) request.getSession().getAttribute(USERNAME);
		}
		m.setUserName(userName);

		// Pages
		int page = 1;
		if (request.getParameter("page") != null) {
			try {
				page = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		int adsCount = ads.getCount();
		m.setListingSize(adsCount);

		int maxPage = Math.round((float) adsCount / (float) ADS_PER_MAIN_PAGE) + 1;
		if (page > maxPage)
			page = maxPage;
		if (page <= 0)
			page = 1;
		m.setCurrentPage(page);

		// Controller
		m.setAppVersion(APP_VERSION);
		m.setAvailable(m.getListingSize() > 0);
		m.setListing(ads.getMainListing(m.getCurrentPage()));

		if (m.getUserName() == null) {
			m.setLoginStatus("<a class=nm href=".concat(CONTEXT_ROOT)
					.concat("login>").concat(NOT_LOGGED_IN).concat("</a>"));
		} else {
			m.setLoginStatus("<a class=nm href=".concat(CONTEXT_ROOT)
					.concat("login>").concat(LOGGED_IN).concat(" as ")
					.concat(m.getUserName()).concat("</a>"));
		}

		// PageNumbers
		ArrayList<String> list = new ArrayList<String>();
		int cnt = Math.round(m.getListingSize() / ADS_PER_MAIN_PAGE);
		if (cnt > 0) {
			int n;
			if (cnt * ADS_PER_MAIN_PAGE == m.getListingSize())
				cnt -= 1;
			if (cnt > 0) {
				for (n = 1; n <= cnt + 1; n++) {
					if (n == m.getCurrentPage()) {
						list.add("<a class=pages href=".concat(CONTEXT_ROOT)
								.concat("?page=").concat(Integer.toString(n))
								.concat("><b><u>").concat(Integer.toString(n))
								.concat("</u></b></a>&nbsp;"));
					} else {
						list.add("<a class=pages href=".concat(CONTEXT_ROOT)
								.concat("?page=").concat(Integer.toString(n))
								.concat(">").concat(Integer.toString(n))
								.concat("</a>&nbsp;"));
					}

					if (n % PAGES_IN_LINE == 0)
						list.add("<br />");
				}
			} else
				list = null;
			m.setPageNumbers(list);
		}
		model.addObject("model", m);
		return model;
	}
}
