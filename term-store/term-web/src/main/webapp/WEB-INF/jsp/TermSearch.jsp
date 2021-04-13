<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<title>User management</title>
<!-- Bootstrap core CSS -->
<link href="./html/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="./html/main.css" rel="stylesheet">
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
			<a class="navbar-brand" href="#">Term Store Management</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="./term-tree">Terms<span class="sr-only">(current)</span></a></li>
				<li><a href="./categories">Categories</a></li>
				<li><a href="./term-search">Search</a></li>
			</ul>
		</div>
	</div>
	</nav>
	<div class="container">
		<h1 class="page-header">Search</h1>
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" action="./term-search" method="post">
					<div class="col-xs-12">
						<div class="row form-group">
							<div class="col-xs-2">Name Starts with</div>
							<div class="col-xs-3">
								<input type="text" name="name" class="form-control"
									placeholder="Name" autofocus>
							</div>
							<div class="col-xs-2">In Category</div>
							<div class="col-xs-3">
								<select name="category" class="form-control">
									<option value="">------</option>
									<c:forEach items="${categories}" var="category">
										<option value="${category}">${category}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-2">Label starts with</div>
							<div class="col-xs-3">
								<input type="text" name="label" class="form-control"
									placeholder="Label" autofocus>
							</div>
							<div class="col-xs-2">Purpose contains</div>
							<div class="col-xs-3">
								<input type="text" name="purpose" class="form-control"
									placeholder="Label" autofocus>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-12">
								<div class="pull-right">
									<button type="submit" class="btn btn-primary">
										<i class="glyphicon glyphicon-search"></i>Search
									</button>
									&nbsp;
									<button class="btn btn-warning">
										<i class="glyphicon glyphicon-refresh"></i>Reset
									</button>
									&nbsp;
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-xs-12">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>Term name</th>
							<th>category</th>
							<th>Label</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${foundTerms}" var="term">
						<tr>
							<td>${term.getName()}</td>
							<td>${term.getCategory().getName()}</td>
							<td>${term.getLabel()}</td>
							<td><a href="./term-tree?display=${term.getId()}" class="btn btn-primary"> <span
									class="glyphicon glyphicon-list-alt"></span>View
							</a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- /container -->
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="./html/bootstrap/js/bootstrap.min.js"></script>
</body>

</html>
