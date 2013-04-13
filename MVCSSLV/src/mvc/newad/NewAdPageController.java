package mvc.newad;

import javax.servlet.http.HttpServletRequest;

import mvc.IController;
import mvc.IModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.newad.NewAdDAO;

@Component
public class NewAdPageController implements IController {

	@Autowired
	private NewAdDAO dao;

	@Override
	public void execute(IModel model, HttpServletRequest req) {
		String outStringStart = "";
		String outStringFinish = "";
		String outStringMiddle = "";
		// TODO Auto-generated method stub
		NewAdPageModel m = (NewAdPageModel) model;

		if (m.getUserName() == null) {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(
					NOT_LOGGED_IN).concat("</a>"));
		} else {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(LOGGED_IN)
					.concat(" as ").concat(m.getUserName()).concat("</a>"));
		}

		if (m.getName() == "")
			m.setName(null);
		if ((m.getName() != null) && (m.getUserName() != null)
				&& (m.getSavedStatus() == null)) {
			System.out.println("1.Controller: " + m.getLoginStatus());
			System.out.println("1.Controller: " + m.getName());
			System.out.println("1.Controller: " + m.getUserName());
			System.out.println("1.Controller: " + m.getSavedStatus());

			dao.setNewAd(m.getName(), m.getUserName(), m.getParamList());
			m.setSavedStatus("SAVED");
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
						+ "<fieldset style='width: 95%; border: 0px;'>"
						+ "<legend>Advertisement header (name)</legend>"
						+ "<input type=text name='name' value='' style='width: 30%;'/>"
						+ "</fieldset>";

				for (NewAdPageParams c : m.getParamList()) {
					System.out.println(c);
					outStringMiddle = outStringMiddle
							+ "<fieldset style='width: 95%; ; border: 0px;'>"
							+ "<legend>" + c.getParamName() + "</legend>"
							+ "<textarea rows='0' cols='90' name='"
							+ c.getParamName() + "'>"
							+ "</textarea></fieldset>";
				}

				outStringFinish = "<br><input type='submit' value='Submit'>"
						+ "</form>" + "<br>";
				m.setMsgOut(outStringStart + outStringMiddle + outStringFinish);

			}
		}
		System.out.println("Controller: " + m.getLoginStatus());
		System.out.println("Controller: " + m.getName());
		System.out.println("Controller: " + m.getUserName());
		System.out.println("Controller: " + m.getSavedStatus());
	}
}
