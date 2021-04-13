<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>User management</title>

<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/main.css" />" rel="stylesheet">
</head>

<body>
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Content Management System</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="glyphicon glyphicon-user"></i>${currentUser.username}<span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="./change-password"><i
								class="glyphicon glyphicon-pencil"></i>Change Password</a></li>
						<li><a href="./logout"><i
								class="glyphicon glyphicon-log-out"></i>Logout</a></li>
					</ul></li>
			</ul>
		</div>
	</div>
	</nav>

	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li><a href="./site-settings">Site Settings</a></li>
					<li><a href="./user-management">Users Management</a></li>
					<li><a href="./site-management">Sites Management</a></li>
					<li><a href="./page-management?filter=all">Page Management</a></li>
				</ul>
			</div>

			<jsp:include page="/WEB-INF/views/${includedPage}.jsp"></jsp:include>
		</div>
	</div>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</body>
</html>