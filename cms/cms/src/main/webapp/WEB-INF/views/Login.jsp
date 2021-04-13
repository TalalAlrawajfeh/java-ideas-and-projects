<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Sign in to CMS</title>

<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<style type="text/css">
.form-signin .form-signin-heading, .form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin .checkbox {
	font-weight: normal;
}

.form-signin .form-control {
	position: relative;
	height: auto;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 10px;
	font-size: 16px;
}

.form-signin .form-control:focus {
	z-index: 2;
}

.form-signin input[type="input"] {
	margin-bottom: -1px;
	border-bottom-right-radius: 0;
	border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
	margin-bottom: 10px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}
</style>

</head>

<body>
	<div class="container">
		<c:if test="${showError == true}">
			<div class="row">
				<div class="alert alert-danger">
					<strong>Error!</strong> ${errorMessage}
				</div>
			</div>
		</c:if>
		<div class="row">
			<div class="col-xs-10 col-sm-4 col-sm-offset-4">
				<form action="./login" method="post" class="form-signin">
					<h2 class="form-signin-heading">Please sign in to CMS</h2>
					<label class="sr-only">Username</label> <input type="text"
						id="username" name="username" class="form-control"
						placeholder="Username" required autofocus> <label
						class="sr-only">Password</label> <input type="password"
						id="password" name="password" class="form-control"
						placeholder="Password" required>
					<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
						in</button>
				</form>
			</div>
		</div>
	</div>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</body>
</html>

