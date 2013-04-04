package mvc.adviewpage;

import javax.servlet.http.HttpServletRequest;
import mvc.IModel;
import mvc.IModelCreator;
import mvc.adviewpage.AdViewPageModel;

public class AdViewPageModelCreator implements IModelCreator {

	@Override
	public IModel createModel(HttpServletRequest request) {

		AdViewPageModel model = new AdViewPageModel();

		int adsId = 0;
		if (request.getParameter("adsid") != null) {
			adsId = Integer.parseInt(request.getParameter("adsid"));
		}
		model.setAdsId(adsId);

		String userName = null;
		if (request.getSession().getAttribute("username") != null) {
			userName = (String) request.getSession().getAttribute("username");
		}
		model.setUserName(userName);
		return model;
	}
}
