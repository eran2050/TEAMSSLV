package mvc.newad;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;


import mvc.IModel;
import mvc.IModelCreator;

@Component
public class NewAdModelCreator implements IModelCreator{

	private HttpSession hs;

	public IModel createModel(HttpServletRequest r) {

		NewAdPageModel m = new NewAdPageModel();
		hs = r.getSession();
		
		
		String userName = null;
		if (hs.getAttribute("username") != null) {
			userName = (String) hs.getAttribute("username");
		}
		m.setUserName(userName);
				
		String name=null;
		name=r.getParameter("name");
		System.out.println("Creator :"+name);
		m.setName(name);
		
		ArrayList<NewAdPageParams> paramList1 = new ArrayList<NewAdPageParams>();

		NewAdPageParams Param1=new NewAdPageParams();
		Param1.setParamName("PRICE");
		Param1.setParamValue(r.getParameter("PRICE"));
		paramList1.add(Param1);
		
		NewAdPageParams Param2=new NewAdPageParams();
		Param2.setParamName("CONDITION");
		Param2.setParamValue(r.getParameter("CONDITION"));
		paramList1.add(Param2);
		
		NewAdPageParams Param3=new NewAdPageParams();
		Param3.setParamName("OTHER");
		Param3.setParamValue(r.getParameter("OTHER"));
		paramList1.add(Param3);
		
		m.setParamList(paramList1);
		
							
		return m;			
	}

	
	
}
