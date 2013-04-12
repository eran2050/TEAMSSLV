package mvc.adviewpage;

import java.util.ArrayList;
import java.util.Arrays;

import mvc.IModel;
import mvc.IModelCreator;
import org.springframework.stereotype.Component;

import domain.addescpage.AdDesc;
import domain.mainpage.Ads;

import javax.servlet.http.HttpServletRequest;

@Component
public class AdViewPageModelCreator implements IModelCreator {

	@Override
	public IModel createModel(HttpServletRequest request) {

		AdViewPageModel model = new AdViewPageModel();

		String userName = null;
		if (request.getSession().getAttribute("username") != null) {
			userName = (String) request.getSession().getAttribute("username");
		}
		int adsId = 0;
		if (request.getParameter("adsid") != null) {
			adsId = Integer.parseInt(request.getParameter("adsid"));
		}
		String adDescId[] = null;
		if (request.getParameter("addescid") != null) {
			adDescId = request.getParameterValues("addescid");
		}
		String criteria[] = null;
		if (request.getParameterValues("criteria") != null) {
			criteria = request.getParameterValues("criteria");
		}
		String value[] = null;
		if (request.getParameter("value") != null) {
			value = request.getParameterValues("value");
		}

		String action = request.getParameter("action");
		if (action == null) {
			action = EMPTY;
		}

		model.setAdsId(adsId);
		model.setUserName(userName);
		model.setAction(action);

		if (model.getAction().equals(ACTION_UPDATE)) {
// todo 

			ArrayList<AdDesc> fullDesc = new ArrayList<AdDesc>();
			for (int i = 0; i < adDescId.length; i++) {
				AdDesc adDesc = new AdDesc();
				adDesc.setId(Integer.parseInt(adDescId[i]));
				adDesc.setAdsId(adsId);
				adDesc.setCriteria(criteria[i]);
				adDesc.setValue(value[i]);
				fullDesc.add(adDesc);
//				System.out.println(adDescId[i] + adsId + criteria[i]);
			}
			model.setFullDesc(fullDesc);
//			for (AdDesc z : model.getFullDesc()) {
//				System.out.println("id" + z.getId() + "criteria"+
//						z.getCriteria() + "value"+ z.getValue());
//			}
		}

		return model;
	}
}
