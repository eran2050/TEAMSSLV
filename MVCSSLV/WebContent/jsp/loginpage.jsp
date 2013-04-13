<%@page import="domain.ads.Ads"%>
<jsp:useBean id="modelLoginPage" type="mvc.loginpage.LoginPageModel"
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
						<%=modelLoginPage.getStatus()%><%=modelLoginPage.getUserName()%></td>

				</tr>
			</table>
		</div>
		<div class="body">

			<p>
				Status:
				<%=modelLoginPage.getStatusMessage()%></p>

			<%
				if (modelLoginPage.isValid()) {
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
					<td><%=modelLoginPage.getUser().getId()%></td>
					<td><%=modelLoginPage.getUser().getName()%></td>
					<td><%=modelLoginPage.getUser().getSurName()%></td>
					<td><%=modelLoginPage.getUser().geteMail()%></td>
					<td><%=modelLoginPage.getUser().getPhone()%></td>
				</tr>
				<%
					}
				%>
			</table>

			<p>
				<%=modelLoginPage.getUserPassword()%></p>
			<%
				if (modelLoginPage.isValid() && modelLoginPage.getAds().size() > 0) {
			%>
			<table class="ml">
				<tr>
					<th class="ml">Id</th>
					<th class="ml">Summary</th>
					<th class="ml">Date</th>
					<th class="ml">Action</th>
				</tr>
				<%
					for (Ads ml : modelLoginPage.getAds()) {
				%>
				<tr>
					<td width="5%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getId()%></a></td>
					<td width="50%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getName()%></a></td>
					<td width="18%"><%=ml.getCreated()%></td>
					<td width="10%" align="center"><a
						href="/java2/login?action=delete&adsid=<%=ml.getId()%>">DEL</a></td>
				</tr>


				<%
					} // for loop
				%>
			</table>
			<%
				if (modelLoginPage.getPageNumbers() != null) {
			%>
			<p>
				Pages:&nbsp;
				<%
					for (String pn : modelLoginPage.getPageNumbers()) {
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
				}
			%>

			<p><%=modelLoginPage.getHtmlForm()%>
			</p>
		</div>
		<div class="footer">
			<table class="footer">
				<tr>
					<td class="footer">T2CSupp Staff&nbsp;(c)&nbsp;<%=modelLoginPage.getAppVersion()%>&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
