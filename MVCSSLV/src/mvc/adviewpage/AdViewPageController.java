package mvc.adviewpage;

import java.util.ArrayList;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import domain.addescpage.AdDesc;
import domain.mainpage.Ads;
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

		// Process request
		String userName = null;
		if (req.getSession().getAttribute("username") != null) {
			userName = (String) req.getSession().getAttribute("username");
		}
		int adsId = 0;
		if (req.getParameter("adsid") != null) {
			adsId = Integer.parseInt(req.getParameter("adsid"));
		}

		String adName = null;
		if (req.getParameter("name") != null) {
			adName = req.getParameter("name");
		}

		String adDescId[] = null;
		if (req.getParameter("addescid") != null) {
			adDescId = req.getParameterValues("addescid");
		}
		String criteria[] = null;
		if (req.getParameterValues("criteria") != null) {
			criteria = req.getParameterValues("criteria");
		}
		String value[] = null;
		if (req.getParameter("value") != null) {
			value = req.getParameterValues("value");
		}

		String action = req.getParameter("action");
		if (action == null) {
			action = EMPTY;
		}
		// Initialize model
		AdViewPageModel adViewM = (AdViewPageModel) model;
		adViewM.setAdsId(adsId);
		adViewM.setUserName(userName);
		adViewM.setAction(action);
		adViewM.setAds(ads.getById(adViewM.getAdsId()));
		adViewM.setAppVersion(APP_VERSION);
		adViewM.setFullDesc(adDesc.getFullAdDesc(adViewM.getAdsId()));

		// Complete action
		if (adViewM.getAction().equals(ACTION_UPDATE)) {
			ArrayList<AdDesc> fullDesc = new ArrayList<AdDesc>();

			Ads ad = new Ads();
			// adDesc.updateAdDesc(fullDesc);
			ad.setName(adName);
			ad.setId(adsId);
			ads.updateAds(ad);
			adViewM.setAds(ads.getById(adViewM.getAdsId()));

			for (int i = 0; i < adDescId.length; i++) {

				AdDesc adDesc = new AdDesc();
				adDesc.setId(Integer.parseInt(adDescId[i]));
				adDesc.setAdsId(adsId);
				adDesc.setCriteria(criteria[i]);
				adDesc.setValue(value[i]);
				fullDesc.add(adDesc);

			}
			adDesc.updateAdDesc(fullDesc);
			adViewM.setFullDesc(fullDesc);
		}

		// Create http elements
		if (adViewM.getUserName() == null) {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(
					NOT_LOGGED_IN).concat("</a>"));
		} else {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>"
					.concat(LOGGED_IN).concat(" as ")
					.concat(adViewM.getUserName()).concat("</a>"));
		}

		StringBuilder form = new StringBuilder();
		if (adViewM.getAction().equals(EMPTY)
				|| adViewM.getAction().equals(ACTION_VIEW)
				|| adViewM.getAction().equals(ACTION_UPDATE)) {

			form.append("<form name=editaddesc method=post action=\"/java2/adview/\">");

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
				form.append("<td class=\"ml2\">" + adDesc.getValue() + "</td>");
				form.append("</tr>");
			}
			form.append("</table>");
			form.append("<input type=\"submit\" name=\"edit\" value=\"Edit\">");
			form.append("</form>");
		} else if (adViewM.getAction().equals(ACTION_EDIT)) {

			form.append("<form name=updateaddesc method=post action=\"/java2/adview/\">");
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
			form.append("<td width=\"50%\"><input type=\"text\" name=\"name\" value="
					+ ads.getName() + "></td>");
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
				form.append("<input type=\"text\" name=\"criteria\" value="
						+ adDesc.getCriteria() + "></td>");
				form.append("<td class=\"ml2\"><input type=\"text\" name=\"value\"	value="
						+ adDesc.getValue() + "></td>");
				form.append("</tr>");
			}
			form.append("</table>");
			form.append("<input type=\"submit\" name=\"update\" value=\"Update\">");
			form.append("</form>");
		}
		adViewM.setForm(form.toString());

	}
}
