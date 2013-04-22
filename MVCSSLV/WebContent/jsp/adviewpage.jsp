<%@page import="domain.ads.Ads"%>
<%@page import="mvc.adviewpage.AdViewPageModel"%>
<%@page import="domain.addesc.AdDesc"%>
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
		<%=model.getForm()%>
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
