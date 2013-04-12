package mvc.adviewpage;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import domain.addescpage.AdDesc;
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
		//todo
		if (adViewM.getAction().equals(ACTION_UPDATE)) {
			adDesc.updateAdDesc(adViewM.getFullDesc());
		}
		
		adViewM.setFullDesc(adDesc.getFullAdDesc(adViewM.getAdsId()));

		if (adViewM.getUserName() == null) {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>".concat(
					NOT_LOGGED_IN).concat("</a>"));
		} else {
			adViewM.setLoginStatus("<a class=nm href=/java2/login>"
					.concat(LOGGED_IN).concat(" as ")
					.concat(adViewM.getUserName()).concat("</a>"));
		}
		//
		StringBuilder form = new StringBuilder();
		if (adViewM.getAction().equals(EMPTY)
				|| adViewM.getAction().equals(ACTION_VIEW)
				|| adViewM.getAction().equals(ACTION_UPDATE)) {

			form.append("<form name=editaddesc method=post action=\"/java2/adview/\">");
			form.append("<input type=\"hidden\" name=\"adsid\" value="
					+ adViewM.getAds().getId() + ">");
			form.append("<table class=\"ml2\">");
			form.append("<tr>");
			form.append("<th class=\"ml2\">Criteria</th>");
			form.append("<th class=\"ml2\">Description</th>");
			form.append("</tr>");

			for (AdDesc adDesc : adViewM.getFullDesc()) {
				int index = 0;
				form.append("<tr>");
				form.append("<td class=\"ml2\"><input type=\"hidden\" name=\"action\" value=\"edit\">");
				form.append("<input type=\"hidden\" name=\"addescid\" value="
						+ adDesc.getId() + ">");
				form.append(adDesc.getCriteria() + "</td>");
				form.append("<td class=\"ml2\">" + adDesc.getValue() + "</td>");
				// todo
				// form.append("<td><input type=\"checkbox\" name=\"check" +
				// index + "\" value=\"" + adDesc.getId() + "\"></td>");
				form.append("</tr>");
				index++;
			}
			form.append("</table>");
			form.append("<input type=\"submit\" name=\"edit\" value=\"Edit\">");
			form.append("</form>");
		} else if (adViewM.getAction().equals(ACTION_EDIT)) {

			form.append("<form name=updateaddesc method=post action=\"/java2/adview/\">");
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
