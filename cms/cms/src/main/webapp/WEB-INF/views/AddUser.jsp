<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<div class="row">
	<div class="col-sm-9 col-sm-offset-2">
		<c:if test="${showError == true}">
			<div class="row">
				<div class="alert alert-danger">
					<strong>Error!</strong> ${errorMessage}
				</div>
			</div>
		</c:if>
	</div>
</div>

<div class="col-sm-9 col-sm-offset-2">
	<h1 class="page-header">Add user</h1>
	<form method="post" action="./add-user">
		<div class="col-xs-12 col-sm-8">
			<div class="row">
				<div class="col-xs-12 col-sm-2">Name</div>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="fullName" name="fullName"
						class="form-control" placeholder="Full Name" required autofocus>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-2">Username</div>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="username" name="username"
						class="form-control" placeholder="Username" required autofocus>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-8">
					<input type="submit" class="btn btn-primary pull-right" />
				</div>
			</div>
		</div>
	</form>
</div>

