package mvc.loginpage;

import dao.ads.AdsDAO;
import mvc.IController;
import mvc.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Component
public class LoginPageController implements IController {

    @Autowired
    private AdsDAO ads;

    public void execute(IModel model, HttpServletRequest req) {

        LoginPageModel lm = (LoginPageModel) model;

        lm.setAds(null);
        lm.setValid(false);

        if (lm.getStatusMessage().equals(NO_SUCH_USER)) {
            lm.setStatusMessage(NO_SUCH_USER);
            lm.setHtmlForm("<form action="
                    .concat(CONTEXT_ROOT)
                    .concat("login/ method=post>")
                    .concat("Input: <input type=text align=center name=username />")
                    .concat("<input type=hidden name=action value=login />")
                    .concat("<input type=image src=")
                    .concat(CONTEXT_ROOT)
                    .concat("images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
                    .concat("</form>"));
            lm.setStatus(NOT_LOGGED_IN);
        } else if (lm.getStatusMessage().equals(LOGGED_IN)
                && (lm.getAction().equals(ACTION_LOGIN) || lm.getAction()
                .equals(EMPTY))) {
            lm.setStatusMessage(LOGGED_IN);
            lm.setHtmlForm("<a href="
                    .concat(CONTEXT_ROOT)
                    .concat("login?action=logout>")
                    .concat("Log Out:&nbsp;<input type=hidden align=center name=logoff value=logout />")
                    .concat("<img src=")
                    .concat(CONTEXT_ROOT)
                    .concat("images/logout.jpg HEIGHT=23 align=center border=0 alt=Go />")
                    .concat("</a>"));
            lm.setStatus(LOGGED_IN + " as ");
            lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
        } else if (lm.getAction().equals(ACTION_DELETE)) {
            lm.setStatusMessage(AD_DELETED);
            lm.setHtmlForm("<a href="
                    .concat(CONTEXT_ROOT)
                    .concat("login?action=logout>")
                    .concat("Log Out:&nbsp;<input type=hidden align=center name=logoff value=logout />")
                    .concat("<img src=")
                    .concat(CONTEXT_ROOT)
                    .concat("images/logout.jpg HEIGHT=23 align=center border=0 alt=Go />")
                    .concat("</a>"));
            lm.setStatus(LOGGED_IN + " as ");
            lm.setAds(ads.getByUser(lm.getUserName(), lm.getCurrentPage()));
        } else if (lm.getStatusMessage().equals(LOGGED_OUT)) {
            lm.setStatusMessage(LOGGED_OUT);
            lm.setHtmlForm("<form action="
                    .concat(CONTEXT_ROOT)
                    .concat("login/ method=post>")
                    .concat("Input: <input type=text align=center name=username />")
                    .concat("<input type=hidden name=action value=login />")
                    .concat("<input type=image src=")
                    .concat(CONTEXT_ROOT)
                    .concat("images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
                    .concat("</form>"));
            lm.setStatus(NOT_LOGGED_IN);
        } else {
            lm.setStatusMessage(NOT_LOGGED_IN);
            lm.setHtmlForm("<form action="
                    .concat(CONTEXT_ROOT)
                    .concat("login/ method=post>")
                    .concat("Input: <input type=text align=center name=username />")
                    .concat("<input type=hidden name=action value=login />")
                    .concat("<input type=image src=")
                    .concat(CONTEXT_ROOT)
                    .concat("images/login.jpg HEIGHT=23 align=center border=0 alt=Go />")
                    .concat("</form>"));
            lm.setStatus(NOT_LOGGED_IN);
        }

        // Giving model to View
        if (lm.getUserName() == null) {
            lm.setPassword(EMPTY);
            lm.setUserName(EMPTY);
        } else {
            lm.setValid(true);
        }

        // PageNumbers
        ArrayList<String> list = new ArrayList<String>();
        int cnt = Math.round(lm.getListingSize() / ADS_PER_LOGIN_PAGE);
        if (cnt > 0) {
            int n;
            for (n = 1; n <= cnt + 1; n++) {
                if (n == lm.getCurrentPage()) {
                    list.add("<a class=pages href=".concat(CONTEXT_ROOT)
                            .concat("login/?page=").concat(Integer.toString(n))
                            .concat("><b><u>").concat(Integer.toString(n))
                            .concat("</u></b></a>&nbsp;"));
                } else {
                    list.add("<a class=pages href=".concat(CONTEXT_ROOT)
                            .concat("login/?page=").concat(Integer.toString(n))
                            .concat(">").concat(Integer.toString(n))
                            .concat("</a>&nbsp;"));
                }

                if (n % PAGES_IN_LINE == 0)
                    list.add("<br>");
            }
            lm.setPageNumbers(list);
        }
    }
}
