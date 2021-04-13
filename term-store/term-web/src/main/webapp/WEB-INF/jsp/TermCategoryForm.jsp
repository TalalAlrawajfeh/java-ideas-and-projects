<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
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
		<h1 class="page-header">Edit Category</h1>
		<div class="row">
			<form action="./categories" method="post">
				<input type="hidden" name="action" value="updateCategory"> <input
					type="hidden" name="oldName" value="${category.getName()}">
				<div class="col-xs-6 form-horizontal">
					<div class="row form-group">
						<div class="col-xs-12">Name</div>
						<div class="col-xs-12">
							<input type="text" name="name" class="form-control"
								placeholder="Name" value="${category.getName()}" required
								autofocus>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-xs-12">Allows Children</div>
						<div class="col-xs-12">
							<select name="childrenPermitted" class="form-control" s>
								<option value="Yes">Yes</option>
								<option value="No">No</option>
							</select>
						</div>
					</div>
					<div class="pull-right">
						<button type="button" class="btn btn-default"
							onclick="redirect();">Cancel</button>
						<button type="submit" class="btn btn-primary">Save</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- /container -->
	<div class="modal fade" id="newCategoryModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<form action=".">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">New Category</h4>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="submit" class="btn btn-primary">Save
							changes</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<input type="hidden" id="redirectPath"
		value="${requestScope['javax.servlet.forward.request_uri']}">
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="./html/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function redirect() {
			window.location.href = document.getElementById("redirectPath").value;
		}
	</script>
</body>

</html>
