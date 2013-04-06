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
		<p align="left">
			<a href="JavaScript:window.close()">Close</a>
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
		<table class="ml2">
			<tr>
				<th class="ml2">Criteria</th>
				<th class="ml2">Description</th>
			</tr>
			<%
				for (AdDesc adsDesc : model.getFullDesc()) {
			%>
			<tr>
				<td class="ml2"><%=adsDesc.getCriteria()%></td>
				<td class="ml2"><%=adsDesc.getValue()%></td>
			</tr>
			<%
				}
			%>
		</table>

		<br>
		<div class="footer1">
			<table class="footer1">
				<tr>
					<td class="footer1">T2CSupp Staff&nbsp;(c)&nbsp;</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
