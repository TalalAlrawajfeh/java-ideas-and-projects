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
	<h1 class="page-header">Edit user</h1>
	<form class="form-horizontal" id="passwordForm" method="post"
		action="./change-password">
		<input type="hidden" id="action" name="action" value="">
		<div class="col-xs-12 col-sm-8">
			<div class="row form-group">
				<label class="col-xs-12 col-sm-2">Password</label>
				<div class="col-xs-12 col-sm-6">
					<input type="password" id="oldPassword" name="oldPassword"
						class="form-control" placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-2">New Password</label>
				<div class="col-xs-12 col-sm-6">
					<input type="password" id="newPassword" name="newPassword"
						class="form-control" placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-2">Confirm Password</label>
				<div class="col-xs-12 col-sm-6">
					<input type="password" id="confirmPassword" name="confirmPassword"
						oninput="checkTwoPasswords();" class="form-control"
						placeholder="Name" required autofocus>
				</div>
				<label class="col-xs-12 col-sm-2" id="errorLabel"></label>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-10 col-sm-offset-2">
					<button type="button" class="btn btn-primary"
						onclick="confirmPasswords();">Save</button>
					&nbsp; <a class="btn btn-danger"
						onclick="document.getElementById('action').value='cancel';document.getElementById('passwordForm').submit();">Cancel</a>&nbsp;
				</div>
			</div>
		</div>
	</form>
</div>

<script>
	function checkTwoPasswords() {
		newPassword = document.getElementById('newPassword');
		confirmPassword = document.getElementById('confirmPassword');
		errorLabel = document.getElementById('errorLabel');
		if (newPassword.value === confirmPassword.value) {
			errorLabel.style.color = 'green';
			errorLabel.innerHTML = "the two match";
		} else {
			errorLabel.style.color = 'red';
			errorLabel.innerHTML = "the two don't match";
		}
	}

	function confirmPasswords() {
		newPassword = document.getElementById('newPassword');
		confirmPassword = document.getElementById('confirmPassword');
		if (newPassword.value === confirmPassword.value) {
			document.getElementById('action').value = 'save';
			document.getElementById('passwordForm').submit();
		} else {
			window
					.alert("Please first confirm that the two passwords you entered match");
		}
	}
</script>
