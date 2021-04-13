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

<title>Content Management System</title>

<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/main.css" />" rel="stylesheet">

</head>

<body>
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">

		<div class="navbar-header">
			<c:if test="${not empty image}">
				<img src="data:image/jpg;base64, ${image}" width="50" height="50" />
			</c:if>
		</div>
		<div class="navbar-header">
			<a class="navbar-brand"></a> <a class="navbar-brand"
				style="color: blue;">${websiteName}</a>
			<c:if test="${not empty site.parentSite}">
				<a class="navbar-brand"
					href="./delivery?site_uri=${site.parentSite.uri}">${site.parentSite.name}</a>
			</c:if>
			<c:if test="${empty site.parentSite}">
				<a class="navbar-brand">${site.name}</a>
			</c:if>
		</div>
		<ul class="nav navbar-nav">
			<c:forEach items="${subSites}" var="subSite">
				<li><a href="./delivery?site_uri=${subSite.uri}">${subSite.name}</a></li>
			</c:forEach>
		</ul>

	</div>

	</nav>

	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<c:forEach items="${pages}" var="page">
						<li><a href="./delivery?page_uri=${page.uri}">${page.title}</a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="col-sm-9 col-sm-offset-2">
				<div class="row">
					<center>
						<p></p>
						<p>
						<h2>
							<strong>${selectedPage.title}</strong>
						</h2>
						</p>
					</center>
				</div>
				<div class="row">
					<c:if test="${not empty selectedPage.content}">
						<div class="well">${selectedPage.content}</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</body>
</html>