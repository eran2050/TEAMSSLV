<%@page import="net.voaideahost.sslv.domain.ads.Ads"%>
<%@page import="net.voaideahost.sslv.mvc.adviewpage.AdViewPageModel"%>
<%@page import="net.voaideahost.sslv.domain.addesc.AdDesc"%>
<jsp:useBean id="model" type="net.voaideahost.sslv.mvc.adviewpage.AdViewPageModel"
	class="net.voaideahost.sslv.mvc.adviewpage.AdViewPageModel" scope="request"></jsp:useBean>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/java2/css/global.css" />
<title>java2 - "SS.LV": Ads description</title>
</head>
<body>
	<div class="page">
		<table class="ml1">
			<tr>
				<td class="nm" align="right" style="background-color: #999933">Status:&nbsp;
					<%=model.getLoginStatus()%></td>
			</tr>

			<tr>
				<td class="nm" align="right">
					<a href="JavaScript:window.close()"><i>Close this window</i></a>
				</td>
			</tr>
		</table>
		<div class="body">

			<p>
				<%
					if (model.getStatusMessage() != null) {
						out.println("Status : " + model.getStatusMessage());

					}
				%>
			</p>


		</div>

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
