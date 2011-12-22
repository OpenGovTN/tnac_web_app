<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TNAC Octobre 2011</title>
<link
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="http://jquery.bassistance.de/treeview/demo/screen.css"
	type="text/css" />
<link rel="stylesheet"
	href="http://jquery.bassistance.de/treeview/jquery.treeview.css"
	type="text/css" />
<script type="text/javascript"
	src="http://jquery.bassistance.de/treeview/jquery.treeview.js"></script>
<script type="text/javascript" src="js/tnacTree.js"></script>
<link rel="stylesheet" href="css/screen.css" type="text/css" />

<script src="js/highcharts.js" type="text/javascript"></script>
<script src="js/themes/grid.js" type="text/javascript"></script>

</head>
<body>
	<div id="header">
		<h1 align="center">TNAC Octobre 2011 Election Data.
	</div>

	<div class="colmask leftmenu">
		<div class="colleft">
			<div class="col1">
				<!-- Column 1 start -->
				<div id="mainView"></div> 
			</div>
			<div class="col2" style="overflow: auto;height:840px">
				<div id="progress">
					<center>Loading data...</center>
				</div>
				<div id="progressbar"></div>
				<ul id="tnacTree" class="filetree">

				</ul>
			</div>
		</div>
	</div>
	<div align="center"  id="footer">OpenGovTN 2011</div>

</body>
</html>