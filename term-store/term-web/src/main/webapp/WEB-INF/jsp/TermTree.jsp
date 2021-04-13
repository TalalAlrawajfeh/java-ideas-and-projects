<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<h1 class="page-header">Terms</h1>
		<div class="row">
			<div class="col-xs-4">
				<div id="term-tree"></div>
			</div>
			<div class="col-sm-8">
				<form class="form-horizontal" action="./term-tree" method="post">
					<input type="hidden" id="actionInput" name="action" value="">
					<input type="hidden" id="termId" name="id" value="">
					<div class="col-xs-12">
						<div class="row form-group">
							<div class="col-xs-12">Name</div>
							<div class="col-xs-12">
								<input type="text" id="termName" name="name" value="${displayTerm.getName()}" 
									class="form-control" placeholder="Name" required autofocus>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-12">Category</div>
							<div class="col-xs-12">
								<select id="termCategory" name="category" value="${displayTerm.getCategory().getName()}" class="form-control">
									<c:forEach items="${categories}" var="category">
										<option value="${category}">${category}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-12">Label</div>
							<div class="col-xs-12">
								<input type="text" id="termLabel" name="label"
									class="form-control" placeholder="Label" value="${displayTerm.getLabel()}" required autofocus>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-12">Purpose</div>
							<div class="col-xs-12">
								<textarea id="termPurpose" class="form-control" rows="6"
									name="purpose">${displayTerm.getPurpose()}</textarea>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-12">
								<div class="pull-right">
									<button type="submit" class="btn btn-primary"
										onclick="saveTerm();">
										<i class="glyphicon glyphicon-floppy-disk"></i>Save
									</button>
									&nbsp;
									<button type="button" class="btn btn-warning"
										onclick="resetTerm();">
										<i class="glyphicon glyphicon-refresh"></i>Reset
									</button>
									&nbsp;
									<button type="submit" class="btn btn-danger"
										onclick="deleteTerm();">
										<i class="glyphicon glyphicon-trash"></i>Delete
									</button>
									&nbsp;
								</div>
								<div class="pull-left">
									<button type="button" class="btn btn-primary"
										data-toggle="modal" data-target="#newTermModal"
										onclick="addChild();">
										<i class="glyphicon glyphicon-plus"></i>Add child
									</button>
									&nbsp;
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- /container -->
	</div>
	<div class="modal fade" id="newTermModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<form method="post" action="./term-tree">
					<input type="hidden" name="action" value="addChild"> <input
						type="hidden" id="parentIdInput" name="parentId" value="">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Add child term</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal">
							<div class="row form-group">
								<div class="col-xs-12">Name</div>
								<div class="col-xs-12">
									<input type="text" name="name" class="form-control"
										placeholder="Name" required autofocus>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-xs-12">Category</div>
								<div class="col-xs-12">
									<select name="category" class="form-control">
										<c:forEach items="${categories}" var="category">
											<option value="${category}">${category}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-xs-12">Label</div>
								<div class="col-xs-12">
									<input type="text" name="label" class="form-control"
										placeholder="Label" required autofocus>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-xs-12">Purpose</div>
								<div class="col-xs-12">
									<textarea class="form-control" rows="6" name="purpose"></textarea>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="submit" class="btn btn-primary">Save
							changes</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="modal fade" id="exceptionModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<form>
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Error</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal">
							<div class="row form-group">
								<div class="col-xs-12">${exceptionMessage}</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<input type=hidden id="showExceptionModal"
		value="${showExceptionModal}">

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="./html/bootstrap/js/bootstrap.min.js"></script>
	<script src="./html/bootstrap/js/bootstrap-treeview.min.js"></script>


	<script type="text/javascript">
		var tree;

		function getTermTree() {
			if (this.readyState == 4 && this.status == 200) {
				window.tree = JSON.parse(this.responseText);
				$('#term-tree').treeview({
					data : window.tree,
					onNodeSelected : function(event, data) {
						xmlhttp.open("GET", data.href, true);
						xmlhttp.onreadystatechange = getTermTree;
						xmlhttp.send(null);

						var id = getId(data.href);
						document.getElementById("termId").value = id;
						fillInputsByTermId(id);
					}
				});
			}
		}

		var xmlhttp = new XMLHttpRequest();
		xmlhttp.open("GET", "./restful/terms?format=json", true);
		xmlhttp.onreadystatechange = getTermTree;
		xmlhttp.send();

		function saveTerm() {
			document.getElementById("actionInput").value = "saveTerm";
		}

		function deleteTerm() {
			document.getElementById("actionInput").value = "deleteTerm";
		}

		function resetTerm() {
			fillInputsByTermId(document.getElementById("termId").value);
		}

		function addChild() {
			document.getElementById("parentIdInput").value = document
					.getElementById("termId").value;
		}

		function getId(href) {
			parameters = href.split("?")[1];
			hrefId = parameters.split("&")[1];
			return hrefId.split("=")[1];
		}

		function fillInputsByTermId(id) {
			xmlhttpInputs = new XMLHttpRequest();
			xmlhttpInputs.open("GET", "./term-tree?id=" + id, true);
			xmlhttpInputs.onreadystatechange = function() {
				term = JSON.parse(this.responseText);
				document.getElementById("termName").value = term.name;
				document.getElementById("termLabel").value = term.label;
				document.getElementById("termPurpose").value = term.purpose;
				document.getElementById("termCategory").value = term.category;
			};
			xmlhttpInputs.send(null);
		}

		if (document.getElementById('showExceptionModal').value === "Yes") {
			$('#exceptionModal').modal();
		}
	</script>
</body>

</html>
