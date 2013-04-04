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
		<p>
			Status:
			<jsp:getProperty name="model" property="statusMessage" /></p>

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
