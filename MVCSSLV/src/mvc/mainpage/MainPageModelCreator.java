package mvc.mainpage;

import dao.ads.AdsDAO;
import mvc.IModel;
import mvc.IModelCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MainPageModelCreator implements IModelCreator {

    @Autowired
    private AdsDAO ads;

    public IModel createModel(HttpServletRequest r) {

        MainPageModel m = new MainPageModel();

        String userName = null;
        if (r.getSession().getAttribute(USERNAME) != null) {
            userName = (String) r.getSession().getAttribute(USERNAME);
        }
        m.setUserName(userName);

        // Pages
        int page = 1;
        if (r.getSession().getAttribute("mainpage") != null)
            page = Integer.parseInt(r.getSession().getAttribute("mainpage")
                    .toString());
        if (r.getParameter("page") != null) {
            try {
                page = Integer.parseInt(r.getParameter("page"));
            } catch (NumberFormatException e) {
                if (r.getSession().getAttribute("mainpage") == null) {
                    page = 1;
                } else {
                    page = Integer.parseInt(r.getSession().getAttribute("page")
                            .toString());
                }
            }
            r.getSession().setAttribute("mainpage", page);
        }

        int adsCount = ads.getCount();

        m.setListingSize(adsCount);
        int maxPage = Math.round((float) adsCount / (float) ADS_PER_MAIN_PAGE) + 1;
        if (page > maxPage)
            page = maxPage;
        if (page <= 0)
            page = 1;

        m.setCurrentPage(page);

        return m;
    }
}
