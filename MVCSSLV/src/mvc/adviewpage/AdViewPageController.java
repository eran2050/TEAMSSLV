package mvc.adviewpage;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import mvc.IController;
import mvc.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AdViewPageController implements IController {

    @Autowired
    private AdDescDAO adDesc;
    @Autowired
    private AdsDAO ads;

    public void execute(IModel model, HttpServletRequest req) {

        AdViewPageModel adViewM = (AdViewPageModel) model;

        adViewM.setAppVersion(APP_VERSION);
        adViewM.setAds(ads.getById(adViewM.getAdsId()));
        adViewM.setFullDesc(adDesc.getFullAdDesc(adViewM.getAdsId()));

        if (adViewM.getUserName() == null) {
            adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(
            		NOT_LOGGED_IN).concat("</a>"));
        } else {
            adViewM.setLoginStatus("<a class=nm href=/java2/login>"
                    .concat(LOGGED_IN).concat(" as ")
                    .concat(adViewM.getUserName()).concat("</a>"));
        }
    }
}
