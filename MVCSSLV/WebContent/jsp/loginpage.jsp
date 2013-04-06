<%@page import="domain.mainpage.Ads"%>
<jsp:useBean id="model" type="mvc.loginpage.LoginPageModel"
	class="mvc.loginpage.LoginPageModel" scope="request"></jsp:useBean>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/java2/css/global.css" />
<title>java2 - "SS.LV": Login Page</title>
</head>
<body>
	<div class="page">
		<div class="header">
			<table class="nm">
				<tr>
					<td class="nm"><a class="nm" href="/java2/">Home </a></td>
					<td class="nm"><a class="nm" href="/java2/add">Add </a></td>
					<td class="nm"><a class="nm" href="/java2/admin">Admin</a></td>
					<td class="nm3" style="background-color: #999933">Status:&nbsp;
						<jsp:getProperty name="model" property="status" /><jsp:getProperty
							name="model" property="userName" /></td>

				</tr>
			</table>
		</div>
		<div class="body">

			<p>
				Status:
				<jsp:getProperty name="model" property="statusMessage" /></p>

			<%
				if (model.isValid()) {
			%>
			<table class="ml">
				<tr>
					<th class="ml">Id</th>
					<th class="ml">Name</th>
					<th class="ml">Surname</th>
					<th class="ml">e-mail</th>
					<th class="ml">Phone</th>
				</tr>

				<tr>
					<td><%=model.getUser().getId()%></td>
					<td><%=model.getUser().getName()%></td>
					<td><%=model.getUser().getSurName()%></td>
					<td><%=model.getUser().geteMail()%></td>
					<td><%=model.getUser().getPhone()%></td>
				</tr>
				<%
					}
				%>
			</table>

			<p>
				<jsp:getProperty name="model" property="userPassword" /></p>
			<%
				if (model.isValid() && model.getAds().size() > 0) {
			%>
			<table class="ml">
				<tr>
					<th class="ml">Id</th>
					<th class="ml">Summary</th>
					<th class="ml">Date</th>
					<th class="ml">Action</th>
				</tr>
				<%
					for (Ads ml : model.getAds()) {
				%>
				<tr>
					<td width="5%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getId()%></a></td>
					<td width="50%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getName()%></a></td>
					<td width="18%"><%=ml.getCreated()%></td>
					<td width="10%" align="center"><a href="/java2/login?action=delete&adsid=<%=ml.getId()%>">DEL</a>&nbsp;|&nbsp;EDIT</td>
				</tr>


				<%
					} // for loop
				%>
			</table>
			<%
				if (model.getPageNumbers() != null) {
			%>
			<p>
				Pages:&nbsp;
				<%
					for (String pn : model.getPageNumbers()) {
				%>

				<%=pn%>

				<%
					}
				%>
			</p>
			<%
				}
			%>
			<%
				} // if
			%>

			<p><jsp:getProperty name="model" property="htmlForm" />
			</p>
		</div>
		<div class="footer">
			<table class="footer">
				<tr>
					<td class="footer">T2CSupp Staff&nbsp;(c)&nbsp;</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
