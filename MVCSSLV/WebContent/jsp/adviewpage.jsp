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
		<p>Status:
		<table class="ml">
			<tr>
				<th class="ml">ID</th>
				<th class="ml">Description</th>
				<th class="ml">Date</th>
				<th class="ml">User</th>
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
<br>
<br>
<br>

		<table class="ml" width="600">
			<tr>
				<th class="ml">Criteria</th>
				<th class="ml">Value</th>
			</tr>
			<%
				for (AdDesc adsDesc : model.getFullDesc()) {
			%>
			<tr>
				<td width="80"><%=adsDesc.getCriteria()%></td>
				<td width="20"><%=adsDesc.getValue()%></td>
			</tr>
			<%
				}
			%>
		</table>



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
