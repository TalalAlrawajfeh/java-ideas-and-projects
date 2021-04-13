<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<div class="col-sm-9 col-sm-offset-2">
	<h1 class="page-header">User management</h1>
	<div class="col-xs-12">
		<div class="pull-right">
			<a class="btn btn-info" href="./add-user">Add User</a>
		</div>
	</div>
	<div class="col-xs-12">
		<table class="table table-striped">
			<thead>
				<th>Name</th>
				<th>Username</th>
				<th></th>
			</thead>
			<tbody>
				<c:forEach items="${users}" var="user">
					<tr>
						<td>${user.fullName}</td>
						<td>${user.username}</td>
						<c:choose>
							<c:when test="${user.username == 'admin'}">
								<td></td>
							</c:when>
							<c:otherwise>
								<td><a href="./edit-user?username=${user.username}"
									class="btn btn-primary">Manage</a></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

