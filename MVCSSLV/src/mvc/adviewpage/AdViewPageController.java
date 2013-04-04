package mvc.adviewpage;

import mvc.IController;
import mvc.IModel;
import dao.addesc.AdDescDAO;
import dao.addesc.AdDescDAOImpl;
import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;

public class AdViewPageController implements IController {

	private final AdDescDAO adDesc = new AdDescDAOImpl();
	private final AdsDAO ads = new AdsDAOImpl();

	public IModel execute(IModel model) {
		
		AdViewPageModel adViewM = (AdViewPageModel) model;
		
		adViewM.setAds(ads.getById(5));
		adViewM.setFullDesc(adDesc.getFullAdDesc(5));

		if (adViewM.getUserName() == null) {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(
					NOT_LOGGED_IN).concat("</a>"));
		} else {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(LOGGED_IN)
					.concat(" as ").concat(adViewM.getUserName()).concat("</a>"));
		}

		return adViewM;
	}
}
