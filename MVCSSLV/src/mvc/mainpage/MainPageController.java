package mvc.mainpage;

import dao.ads.AdsDAO;
import mvc.IController;
import mvc.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Component
public class MainPageController implements IController {

    @Autowired
    private AdsDAO ads;

    public void execute(IModel model, HttpServletRequest req) {
        MainPageModel m = (MainPageModel) model;

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
            m.setPageNumbers(list);
        }
    }
}
