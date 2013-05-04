package net.voaideahost.sslv.mvc.adviewpage;

import static net.voaideahost.sslv.common.Config.ACTION_EDIT;
import static net.voaideahost.sslv.common.Config.ACTION_UPDATE;
import static net.voaideahost.sslv.common.Config.ACTION_VIEW;
import static net.voaideahost.sslv.common.Config.AD_UPDATED;
import static net.voaideahost.sslv.common.Config.STATUS_LOGGED_IN;
import static net.voaideahost.sslv.common.Config.STATUS_NOT_LOGGED_IN;
import static net.voaideahost.sslv.common.Config.VAL_APP_VERSION;
import static net.voaideahost.sslv.common.Config.VAL_EMPTY;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.voaideahost.sslv.dao.addesc.AdDescDAO;
import net.voaideahost.sslv.dao.ads.AdsDAO;
import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.addesc.AdDesc;
import net.voaideahost.sslv.domain.ads.Ads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


@Component
@Controller (value = "/adview")
public class AdViewPageController extends AbstractController {

	@Autowired
	private AdDescDAO	adDesc;

	@Autowired
	private AdsDAO		ads;

	@Autowired
	private UsersDAO	users;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("adviewpage");

		// Process request
		String userName = null;
		if (request.getSession().getAttribute("username") != null) {
			userName = (String) request.getSession().getAttribute("username");
		}
		int adsId = 0;
		if (request.getParameter("adsid") != null) {
			adsId = Integer.parseInt(request.getParameter("adsid"));
		}

		String adName = null;
		if (request.getParameter("name") != null) {
			adName = request.getParameter("name");
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
			action = VAL_EMPTY;
		}
		// Initialize model
		AdViewPageModel adViewM = new AdViewPageModel();
		adViewM.setAdsId(adsId);
		adViewM.setUserName(userName);
		adViewM.setAction(action);
		adViewM.setAds(ads.getSingleAdsById(adViewM.getAdsId()));
		adViewM.setAppVersion(VAL_APP_VERSION);
		adViewM.setFullDesc(adDesc.getFullAdDescByHQL(adViewM.getAdsId()));

		// Complete action
		if (adViewM.getAds().getOwner()
				.equals((String) request.getSession().getAttribute("username"))) {
			if (adViewM.getAction().equals(ACTION_UPDATE)) {
				ArrayList<AdDesc> fullDesc = new ArrayList<AdDesc>();

				Ads ad = new Ads();
				ad.setName(adName);
				ad.setId(adsId);
				ads.updateAds(ad);
				adViewM.setAds(ads.getSingleAdsById(adViewM.getAdsId()));

				for (int i = 0; i < adDescId.length; i++) {

					AdDesc adDesc = new AdDesc();
					adDesc.setId(Integer.parseInt(adDescId[i]));
					adDesc.setAdsId(adsId);
					adDesc.setCriteria(criteria[i]);
					adDesc.setValue(value[i]);
					fullDesc.add(adDesc);

				}
				adViewM.setStatusMessage(AD_UPDATED);
				adDesc.updateAdDesc(fullDesc);
				adViewM.setFullDesc(fullDesc);
			}
		}
		// Create http elements
		if (adViewM.getUserName() == null) {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(
					STATUS_NOT_LOGGED_IN).concat("</a>"));
		} else {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>"
					.concat(STATUS_LOGGED_IN).concat(" as ")
					.concat(adViewM.getUserName()).concat("</a>"));
		}

		StringBuilder form = new StringBuilder();
		if (adViewM.getAction().equals(VAL_EMPTY)
				|| adViewM.getAction().equals(ACTION_VIEW)
				|| adViewM.getAction().equals(ACTION_UPDATE)) {

			form.append("<form name=editaddesc method=post action=\"/java2/adview\">");

			// ///
			form.append("<table class=\"ml1\">");
			form.append("<tr>");
			form.append("<th class=\"ml1\">Id</th>");
			form.append("<th class=\"ml1\">Summary</th>");
			form.append("<th class=\"ml1\">Date</th>");
			form.append("<th class=\"ml1\">User</th>");
			form.append("</tr>");

			Ads ads = adViewM.getAds();

			form.append("<tr>");
			form.append("<td width=\"5%\">" + ads.getId() + "</td>");
			form.append("<td width=\"50%\">" + ads.getName() + "</td>");
			form.append("<td width=\"18%\">" + ads.getCreated() + "</td>");
			form.append("<td width=\"14%\">" + ads.getOwner() + "</td>");
			form.append("</tr>");
			form.append("</table>");
			form.append("<p>");
			form.append("<b>Additional information</b>");
			form.append("</p>");
			// ///
			if (adViewM.getFullDesc().size() != 0) {
				form.append("<input type=\"hidden\" name=\"adsid\" value="
						+ adViewM.getAds().getId() + ">");
				form.append("<table class=\"ml2\">");
				form.append("<tr>");
				form.append("<th class=\"ml2\">Criteria</th>");
				form.append("<th class=\"ml2\">Description</th>");
				form.append("</tr>");

				for (AdDesc adDesc : adViewM.getFullDesc()) {
					form.append("<tr>");
					form.append("<td class=\"ml2\"><input type=\"hidden\" name=\"action\" value=\"edit\">");
					form.append("<input type=\"hidden\" name=\"addescid\" value="
							+ adDesc.getId() + ">");
					form.append(adDesc.getCriteria() + "</td>");
					form.append("<td class=\"ml2\">" + adDesc.getValue()
							+ "</td>");
					form.append("</tr>");
				}
				form.append("</table>");

				if (ads.getOwner().equals(
						(String) request.getSession().getAttribute("username"))) {
					form.append("<br><input type=\"submit\" name=\"edit\" value=\"Edit\">");
				}
				form.append("</form>");
			} else
				form.append("<p>Description not found</p>");

		} else if (adViewM.getAction().equals(ACTION_EDIT)) {

			form.append("<form name=updateaddesc method=post action=\"/java2/adview\">");
			// ///
			form.append("<table class=\"ml1\">");
			form.append("<tr>");
			form.append("<th class=\"ml1\">Id</th>");
			form.append("<th class=\"ml1\">Summary</th>");
			form.append("<th class=\"ml1\">Date</th>");
			form.append("<th class=\"ml1\">User</th>");
			form.append("</tr>");

			Ads ads = adViewM.getAds();

			form.append("<tr>");
			form.append("<td width=\"5%\">" + ads.getId() + "</td>");
			form.append("<td width=\"50%\"><input type=\"text\" name=\"name\" value=\""
					+ ads.getName() + "\"></td>");
			form.append("<td width=\"18%\">" + ads.getCreated() + "</td>");
			form.append("<td width=\"14%\">" + ads.getOwner() + "</td>");
			form.append("</tr>");
			form.append("</table>");
			form.append("<p>");
			form.append("<b>Additional information</b>");
			form.append("</p>");
			// ///
			form.append("<input type=\"hidden\" name=\"adsid\" value="
					+ adViewM.getAds().getId() + ">");
			form.append("<table class=\"ml2\">");
			form.append("<tr>");
			form.append("<th class=\"ml2\">Criteria</th>");
			form.append("<th class=\"ml2\">Description</th>");
			form.append("</tr>");

			for (AdDesc adDesc : adViewM.getFullDesc()) {
				form.append("<tr>");
				form.append("<td class=\"ml2\"><input type=\"hidden\" name=\"action\" value=\"update\">");
				form.append("<input type=\"hidden\" name=\"addescid\" value="
						+ adDesc.getId() + ">");
				form.append("<input type=\"text\" name=\"criteria\" value=\""
						+ adDesc.getCriteria() + "\"></td>");
				form.append("<td class=\"ml2\"><input type=\"text\" name=\"value\"	value=\""
						+ adDesc.getValue() + "\"></td>");
				form.append("</tr>");
			}
			form.append("</table>");
			form.append("<br><input type=\"submit\" name=\"update\" value=\"Update\">");
			form.append("</form>");
		}
		adViewM.setForm(form.toString());

		// Giving model to View

		model.addObject("model", adViewM);
		return model;

	}
}