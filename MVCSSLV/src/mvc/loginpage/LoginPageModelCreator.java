package mvc.loginpage;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import dao.users.UsersDAO;
import domain.loginpage.Users;
import mvc.IModel;
import mvc.IModelCreator;
import net.voaideahost.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class LoginPageModelCreator implements IModelCreator {

    @Autowired
    private UsersDAO users;
    @Autowired
    private AdsDAO ads;
    @Autowired
    private AdDescDAO adDesc;

    public IModel createModel(HttpServletRequest r) {

        LoginPageModel lm = new LoginPageModel();
        lm.setStatusMessage(EMPTY);
        HttpSession hs = r.getSession();

        // Actions
        String action = r.getParameter("action");
        if (action == null) {
            action = EMPTY;
        }
        lm.setAction(action);

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
            ru = r.getParameter(USERNAME);
            // Check User
            if (ru != null) {
                Users u = users.getUserById(ru);
                lm.setUser(u);
                userChecked = u != null;
            }
        }

        // Delete ad
        if (lm.getAction().equals(ACTION_DELETE)) {
            lm.setStatusMessage(AD_DELETED);
            int adsId = Integer.parseInt(r.getParameter("adsid"));
            lm.setAdsId((long) adsId);            
        }

        // Various checks and Status Setter
        if (su == null && userChecked) {
            hs.setAttribute(USERNAME, ru);

            lm.setUserName(ru);
            lm.setPassword("P-hash: ".concat(new Util().getSha(ru,
                    ru.concat(ru))));
            lm.setStatusMessage(LOGGED_IN);
        } else if ((ru == null && su != null) || (ru != null && su != null)) {
            Users u = users.getUserById(su);
            lm.setUser(u);
            lm.setUserName(su);
            lm.setPassword("P-hash: ".concat(new Util().getSha(su,
                    su.concat(su))));
            lm.setStatusMessage(LOGGED_IN);
        } else if (ru != null && !userChecked && su == null) {
            lm.setStatusMessage(NO_SUCH_USER);
        } else if (ru == null && su == null && lm.getAction().equals(EMPTY)) {
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

        // Return Model to Controller
        return lm;
    }
}
