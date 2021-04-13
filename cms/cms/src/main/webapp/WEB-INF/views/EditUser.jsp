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
	<form class="form-horizontal" action="./edit-user" method="post"
		id="editForm">
		<input type="hidden" value="" id="action" name="action"> <input
			type="hidden" value="${managedUser.username}" id="managedUsername"
			name="managedUsername">
		<div class="col-xs-12 col-sm-8">
			<div class="row form-group">
				<label class="col-xs-12 col-sm-2">Name</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="fullName" name="fullName"
						value="${managedUser.fullName}" class="form-control"
						placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-2">Username</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="username" name="username"
						value="${managedUser.username}" class="form-control"
						placeholder="Username" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-10 col-sm-offset-2">
					<button type="button" class="btn btn-info"
						onclick="document.getElementById('action').value='reset';document.getElementById('editForm').submit();">Reset</button>
					&nbsp;
					<c:if test="${managedUser.enabled.booleanValue()}">
						<button type="button" class="btn btn-danger"
							onclick="document.getElementById('action').value='disable';document.getElementById('editForm').submit();">Disable</button>
					</c:if>
					<c:if test="${not managedUser.enabled.booleanValue()}">
						<button type="button" class="btn btn-danger"
							onclick="document.getElementById('action').value='enable';document.getElementById('editForm').submit();">Enable</button>
					</c:if>
					&nbsp;
					<button type="button" class="btn btn-primary"
						onclick="document.getElementById('action').value='save';document.getElementById('editForm').submit();">Save</button>
					&nbsp; <a class="btn btn-danger"
						onclick="document.getElementById('action').value='cancel';document.getElementById('editForm').submit();">Cancel</a>&nbsp;
				</div>
			</div>
		</div>
	</form>
</div>

