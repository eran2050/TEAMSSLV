<%@page import="domain.newad.NewAd"%>
<jsp:useBean id="model" type="mvc.newad.NewAdPageModel"
	class="mvc.newad.NewAdPageModel" scope="request"></jsp:useBean>
<html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Java2 - "SS.LV": Main Page</title>
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
					<td class="nm" ><a class="nm"
						href="/java2/">Home </a></td>
					<td class="nm" style="background-color: #999933"><a class="nm" href="/java2/add/">Add </a></td>
					<td class="nm"><a class="nm" href="/java2/admin">Admin</a></td>
					<td class="nm2">Status:&nbsp; <jsp:getProperty name="model"
							property="loginStatus" />
					</td>
				</tr>
			</table>
		</div>
		<div class="body">
		<%
		if (model.getMsgOut()!=null) {
		%>
		<jsp:getProperty name="model" property="msgOut" />
		<%
				} 
		%>
	
				
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
