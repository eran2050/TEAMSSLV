package mvc.newad;

import mvc.IController;
import mvc.IModel;
import dao.newad.NewAdDAO;
import dao.newad.NewAdDAOImpl;

public class NewAdPageController implements IController {
	private final NewAdDAO dao = new NewAdDAOImpl();

	@Override
	public IModel execute(IModel model) {
		String outStringStart="";
		String outStringFinish="";
		String outStringMiddle="";
		// TODO Auto-generated method stub
		NewAdPageModel m = (NewAdPageModel) model;
		
		if (m.getUserName() == null) {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(NOT_LOGGED_IN).concat("</a>"));
		} else {
			m.setLoginStatus("<a class=nm href=/java2/login>".concat(LOGGED_IN).concat(" as ").concat(m.getUserName()).concat("</a>"));
		}
		
		if (m.getName()=="") m.setName(null);
		if ((m.getName()!=null) && (m.getUserName()!=null) && (m.getSavedStatus()==null)) {
			 System.out.println("1.Controller: "+m.getLoginStatus());
			 System.out.println("1.Controller: "+m.getName());
			 System.out.println("1.Controller: "+m.getUserName());
			 System.out.println("1.Controller: "+m.getSavedStatus());
			
		dao.setNewAd(m.getName(), m.getUserName(), m.getParamList());
			m.setSavedStatus("SAVED");
			outStringStart="<table class='ml'>" +
					"<tr>"+
					"<th class='ml'>User</th>"+
					"<th class='ml'>Subject(Name)</th>"+
					"<th class='ml'>Status</th>"+
					"</tr>" +
					"<tr>"+
					"<td>"+m.getUserName()+"</td>"+
					"<td>"+m.getName()+"</td>"+
					"<td>"+m.getSavedStatus()+"</td>"+
					"</table>"+"<BR>";
		//	m.setMsgOut(				);
			
			outStringMiddle="<p>"+
			"<b>Additional information</b>"+
		"</p><table class='ml2'><tr>"+
				"<th class='ml2'>Criteria</th>"+
				"<th class='ml2'>Description</th>"+
			"</tr>"; 
					
			for(NewAdPageParams c : m.getParamList()) {
			      System.out.println(c);
			      outStringMiddle=outStringMiddle+"<tr><td class='ml2'>"+c.getParamName()+"</td>"+
				"<td class='ml2'>"+c.getParamValue()+"</td></tr>" ;
			      			 }
			outStringMiddle=outStringMiddle+"</table>";
			
						 
			m.setMsgOut(outStringStart+	outStringMiddle);
			m.setSavedStatus(null);
			m.setName(null);
			} else {
			if	(m.getUserName()==null) {
				m.setMsgOut("<h2>Please login before inserting advertisement!</h2>");
			}
			if ((m.getUserName()!=null) && (m.getName()==null) && (m.getSavedStatus()==null)) {
				//m.setMsgOut("<h2>Please insert any data before saving advertisement!</h2>");
				m.setSavedStatus(null);

				outStringStart = "<form action='/java2/add/' method=post>" +
						"<fieldset>" +
						"<legend>Advertisement header (name):</legend>" +
						"<input type=text name='name' value='' />" +
						"</fieldset>" ;
						
				 for(NewAdPageParams c : m.getParamList()) {
				      System.out.println(c);
				      outStringMiddle=outStringMiddle+"<fieldset>" +
						"<legend>"+c.getParamName()+":</legend>" +
						"<textarea rows='0' cols='70' name='"+c.getParamName()+"'>" +
						"</textarea></fieldset>" ;
				      
				 }

						
				outStringFinish="<input type='submit' value='Submit'>"+
						"</form>"+"<br>";
				m.setMsgOut(outStringStart+outStringMiddle+outStringFinish);			 
			
			}
				
			}
		 System.out.println("Controller: "+m.getLoginStatus());
		 System.out.println("Controller: "+m.getName());
		 System.out.println("Controller: "+m.getUserName());
		 System.out.println("Controller: "+m.getSavedStatus());
		
			
		//if (m.getName()==null) {m.setName("");}
		
		return m;
	}

}
/*

	<form action="/java2/add/" method="post">
		<fieldset >
			<legend>Advertisement header (name):</legend>
			
			<input type=text name=name value="" />
			
			</fieldset>
			<fieldset >
			<legend>Advertisement text (f1):</legend>
			<textarea rows="10" cols="70" name="addinfo">
			</textarea>
			
			</fieldset>
			<br>
			<input type="submit" value="Submit">
			</form>

*/