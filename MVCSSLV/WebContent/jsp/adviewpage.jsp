<%@page import="com.sun.corba.se.spi.orbutil.fsm.Action"%>
<%@page import="domain.mainpage.Ads"%>
<%@page import="mvc.adviewpage.AdViewPageModel"%>
<%@page import="domain.addescpage.AdDesc"%>
<jsp:useBean id="model" type="mvc.adviewpage.AdViewPageModel"
	class="mvc.adviewpage.AdViewPageModel" scope="request"></jsp:useBean>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/java2/css/global.css" />
<title>java2 - "SS.LV": Ads description</title>
</head>
<body>
	<div class="body">
		<div class="footer1">
			<table class="footer1">
				<tr>
					<td class="footer">&nbsp;</td>
				</tr>
			</table>
		</div>
		<p align="left">
			<a href="JavaScript:window.close()"><b>Close this window</b></a>
		</p>
		<table class="ml1">
			<tr>
				<th class="ml1">Id</th>
				<th class="ml1">Summary</th>
				<th class="ml1">Date</th>
				<th class="ml1">User</th>
			</tr>

			<%
				Ads ads = model.getAds();
			%>
			<tr>
				<td width="5%"><%=ads.getId()%></td>
				<td width="50%"><%=ads.getName()%></td>
				<td width="18%"><%=ads.getCreated()%></td>
				<td width="14%"><%=ads.getOwner()%></td>
			</tr>
		</table>
		<p>
			<b>Additional information</b>
		</p>

		<%
			out.println(model.getForm());
		%>
		<br>
		<div class="footer1">
			<table class="footer1">
				<tr>
					<td class="footer">T2CSupp Staff&nbsp;(c)&nbsp;<%=model.getAppVersion()%>&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
