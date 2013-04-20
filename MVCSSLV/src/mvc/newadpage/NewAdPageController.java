package mvc.newadpage;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import util.Config;

//import dao.ads.AdsDAO;
import dao.ads.NewAdDAO;

@Component
@Controller(value = "/add/")
public class NewAdPageController extends AbstractController implements Config {

	@Autowired
	private NewAdDAO dao;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("newadpage");

		HttpSession hs = request.getSession();

		String outStringStart = EMPTY;
		String outStringFinish = EMPTY;
		String outStringMiddle = EMPTY;

		NewAdPageModel m = new NewAdPageModel();
		m.setAppVer(APP_VERSION);

		String userName = null;
		if (hs.getAttribute("username") != null) {
			userName = (String) hs.getAttribute("username");
		}
		m.setUserName(userName);

		String name = null;
		name = request.getParameter("name");
		System.out.println("Creator :" + name);
		m.setName(name);

		ArrayList<NewAdPageParams> paramList1 = new ArrayList<NewAdPageParams>();

		NewAdPageParams Param1 = new NewAdPageParams();
		Param1.setParamName("PRICE");
		Param1.setParamValue(request.getParameter("PRICE"));
		paramList1.add(Param1);

		NewAdPageParams Param2 = new NewAdPageParams();
		Param2.setParamName("CONDITION");
		Param2.setParamValue(request.getParameter("CONDITION"));
		paramList1.add(Param2);

		NewAdPageParams Param3 = new NewAdPageParams();
		Param3.setParamName("OTHER");
		Param3.setParamValue(request.getParameter("OTHER"));
		paramList1.add(Param3);

		m.setParamList(paramList1);

		// ////////////////////////////////////////////////////////////////////////////////
		if (m.getUserName() == null) {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(
					NOT_LOGGED_IN).concat("</a>"));
		} else {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(LOGGED_IN)
					.concat(" as ").concat(m.getUserName()).concat("</a>"));
		}

		if (m.getName() != null && m.getName().equals(EMPTY))
			m.setName(null);
		if ((m.getName() != null) && (m.getUserName() != null)
				&& (m.getSavedStatus() == null)) {
			// System.out.println("1.Controller: " + m.getLoginStatus());
			// System.out.println("1.Controller: " + m.getName());
			// System.out.println("1.Controller: " + m.getUserName());
			// System.out.println("1.Controller: " + m.getSavedStatus());
			if (m.getSavedStatus() != null
					&& !m.getSavedStatus().equals("SAVED")) {
				if (hs.getAttribute("BLOCKED") == null) {
					hs.setAttribute("BLOCKED", "1");
					dao.setNewAd(m.getName(), m.getUserName(), m.getParamList());
				}
				m.setSavedStatus("SAVED");
			}
			outStringStart = "<table class='ml'>" + "<tr>"
					+ "<th class='ml'>User</th>"
					+ "<th class='ml'>Subject&nbsp;(Name)</th>"
					+ "<th class='ml'>Status</th>" + "</tr>" + "<tr>" + "<td>"
					+ m.getUserName() + "</td>" + "<td>" + m.getName()
					+ "</td>" + "<td>" + m.getSavedStatus() + "</td>"
					+ "</table>";

			outStringMiddle = "<p>" + "<b>Additional information</b>"
					+ "</p><table class='ml2'><tr>"
					+ "<th class='ml2'>Criteria</th>"
					+ "<th class='ml2'>Description</th>" + "</tr>";

			for (NewAdPageParams c : m.getParamList()) {
				System.out.println(c);
				outStringMiddle = outStringMiddle + "<tr><td class='ml2'>"
						+ c.getParamName() + "</td>" + "<td class='ml2'>"
						+ c.getParamValue() + "</td></tr>";
			}
			outStringMiddle = outStringMiddle + "</table><br>";

			m.setMsgOut(outStringStart + outStringMiddle);
			m.setSavedStatus(null);
			m.setName(null);
		} else {
			if (m.getUserName() == null) {
				m.setMsgOut("<h2>Please login before inserting advertisement!</h2>");
			}
			if ((m.getUserName() != null) && (m.getName() == null)
					&& (m.getSavedStatus() == null)) {
				// m.setMsgOut("<h2>Please insert any data before saving advertisement!</h2>");
				m.setSavedStatus(null);

				outStringStart = "<form action='/java2/add/' method=post>"
						+ "<fieldset style='width: 100%; border: 0px;'>"
						+ "<legend>Advertisement header</legend>"
						+ "<textarea rows='0' cols='90' name='name'/>"
						+ "</textarea>" + "</fieldset>";

				for (NewAdPageParams c : m.getParamList()) {
					System.out.println(c);
					outStringMiddle = outStringMiddle
							+ "<fieldset style='width: 100%; ; border: 0px;'>"
							+ "<legend>" + c.getParamName() + "</legend>"
							+ "<textarea rows='0' cols='90' name='"
							+ c.getParamName() + "'>"
							+ "</textarea></fieldset>";
				}

				outStringFinish = "<br><input type='submit' value='Submit'>"
						+ "</form>" + "<br>";
				m.setMsgOut(outStringStart + outStringMiddle + outStringFinish);
				hs.setAttribute("BLOCKED", null);
			}
		}
		System.out.println("Controller: " + m.getLoginStatus());
		System.out.println("Controller: " + m.getName());
		System.out.println("Controller: " + m.getUserName());
		System.out.println("Controller: " + m.getSavedStatus());

		model.addObject("modelAddNew", m);
		return model;
	}
}
