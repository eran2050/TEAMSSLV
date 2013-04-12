package mvc.adviewpage;

import mvc.IModel;
import mvc.IModelCreator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
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
