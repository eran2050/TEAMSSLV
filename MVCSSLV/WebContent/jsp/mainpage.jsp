<%@page import="domain.mainpage.Ads"%>
<jsp:useBean id="model" type="mvc.mainpage.MainPageModel"
	class="mvc.mainpage.MainPageModel" scope="request"></jsp:useBean>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>java2 - "SS.LV": Main Page</title>
<link rel="stylesheet" type="text/css" href="/java2/css/global.css" />
<meta charset="utf-8">
<meta name="description" content="">
<meta name="keywords" content="">
</head>
<body>
	<div class="page">
		<div class="header">
			<table class="nm">
				<tr>
					<td class="nm" style="background-color: #999933"><a class="nm"
						href="/java2/">Home </a></td>
					<td class="nm"><a class="nm" href="/java2/add">Add </a></td>
					<td class="nm"><a class="nm" href="/java2/admin">Admin</a></td>
					<td class="nm2">Status:&nbsp; <jsp:getProperty name="model"
							property="loginStatus" />
					</td>
				</tr>
			</table>
		</div>
		<div class="body">
			<p>
				Total Ads:&nbsp;<%=model.getListingSize()%>
			</p>
			<table class="ml">
				<tr>
					<th class="ml">Id</th>
					<th class="ml">Summary</th>
					<th class="ml">Date</th>
					<th class="ml">User</th>
				</tr>
				<%
					if (model.isAvailable())
						for (Ads ml : model.getListing()) {
				%>
				<tr>
					<td width="5%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getId()%></a></td>
					<td width="50%"><a href="/java2/adview?adsid=<%=ml.getId()%>"
						target="_blank"><%=ml.getName()%></a></td>
					<td width="18%"><%=ml.getCreated()%></td>
					<td width="14%"><%=ml.getOwner()%></td>
				</tr>
				<%
					}
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
				} else {
			%>
			<br />
			<%
				}
			%>
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
