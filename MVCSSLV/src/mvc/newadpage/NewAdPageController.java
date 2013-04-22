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

		//Filling up model
		//Customer userName from Session parameter
		String userName = null;
		if (hs.getAttribute("username") != null) {
			userName = (String) hs.getAttribute("username");
		}
		m.setUserName(userName);
		
		//Main Ad description field - from session (if exists)
		String name = null;
		name = request.getParameter("name");
		m.setName(name);

		//Set if additional parameters
		//For this moment this is a set of 3 predefined parameters
		//However as future project evolution - this should be changed to dynamical
		ArrayList<NewAdPageParams> paramList1 = new ArrayList<NewAdPageParams>();
		//PRICE initialisation > read from Http_request > write to temporary variable 
		NewAdPageParams Param1 = new NewAdPageParams();
		Param1.setParamName("PRICE");
		Param1.setParamValue(request.getParameter("PRICE"));
		paramList1.add(Param1);
		//Condition initialisation > read from Http_request > write to temporary variable 
		NewAdPageParams Param2 = new NewAdPageParams();
		Param2.setParamName("CONDITION");
		Param2.setParamValue(request.getParameter("CONDITION"));
		paramList1.add(Param2);
		//Other initialisation > read from Http_request > write to temporary variable 
		NewAdPageParams Param3 = new NewAdPageParams();
		Param3.setParamName("OTHER");
		Param3.setParamValue(request.getParameter("OTHER"));
		paramList1.add(Param3);
		
		//Writetemporary varianle list to the model
		m.setParamList(paramList1);
		
		//
		//
		//Business logic starts HERE: check if customer is logged
		//
		//
		
		if (m.getUserName() == null) {
			//
			//Exit if customer is not logged
			//
			
			System.out.println("NewAdpageController.line81");
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(					NOT_LOGGED_IN).concat("</a>"));
			m.setMsgOut("<h2>Please login before inserting advertisement!</h2>");
			
		} else {
							
				//If cusstomer is logged in
				m.setLoginStatus("<a class=nm href=/java2/login>".concat(LOGGED_IN)
					.concat(" as ").concat(m.getUserName()).concat("</a>"));
				
				//
				//checking if main advertisement data exists
				//
				if (m.getName()!= null) {
					
					//If exists, then go ON with data validation
					//
					if (m.getName().equals(EMPTY)) {
						//preventing zero input in DB for main field
						m.setName(null);
						m.setMsgOut("<h2>Please insert any data before saving advertisement!</h2>");
					
					} else {
						//If it exists and not null
						//We're checking if model had been saved
						if (m.getSavedStatus() == null)
						{ 
						//if model hadn't been saved yet then saving it
							if (hs.getAttribute("BLOCKED") == null) {
								//blocking session to SQL to avoid multiple insert
								hs.setAttribute("BLOCKED", "1");
								if (!dao.setNewAd(m.getName(), m.getUserName(), m.getParamList()))
								{
									m.setMsgOut("<h2>DATABASE exception! Please call system administrator</h2>");
									model.addObject("modelAddNew", m);
									return model;
								}							}
							//nevertheless the SAVE was blocked or not - going further
							//to display summary for save
							m.setSavedStatus("SAVED");
												
							//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
							/*HTML structure generation*/
							//Filling up result table
							//Main data in the advertisement
							outStringStart = "<table class='ml'>" + "<tr>"
									+ "<th class='ml'>User</th>"
									+ "<th class='ml'>Subject&nbsp;(Name)</th>"
									+ "<th class='ml'>Status</th>" + "</tr>" + "<tr>" + "<td>"
									+ m.getUserName() + "</td>" + "<td>" + m.getName()
									+ "</td>" + "<td>" + m.getSavedStatus() + "</td>"
									+ "</table>";
							
							//Additional data in the advertisement
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
							
							//result output to view
							m.setMsgOut(outStringStart + outStringMiddle);
							
							//Variable reset for the next run!
							m.setSavedStatus(null);
							m.setName(null);
						} 	//EO getSavedStatus==null								
				
					} //EO else of m.getName().equals(EMPTY)
					
				} //EO m.getName()!= null
				else { //Main advertisement text does not exist (Null)
					
					//NEWLY CREATED RECORD, ALL input fields are empty
					//even if there had been previous save, we're making new record
					
					m.setSavedStatus(null);
					hs.setAttribute("BLOCKED", null);
					//Filling up empty HTML form layout				
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
								
			} //EO Main advertisement text does not exist (Null)
								
			}
		model.addObject("modelAddNew", m);
		
		return model;
	}
}
