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
	<h1 class="page-header">Add Site</h1>
	<form class="form-horizontal" action="./add-site" method="post">
		<div class="col-xs-12 col-sm-8">
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Site Name</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="name" name="name" class="form-control"
						placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Site URI</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="uri" name=uri class="form-control"
						placeholder="URI" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Parent Site</label>
				<div class="col-xs-12 col-sm-6">
					<select name="parentSite" class="form-control">
						<c:forEach items="${sites}" var="site">
							<option value="${site.uri}">${site.name} - ${site.uri}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-6 col-sm-offset-3">
					<button type="submit" class="btn btn-primary">Save</button>
					&nbsp; <a class="btn btn-danger" href="./site-management">Cancel</a>&nbsp;
				</div>
			</div>
		</div>
	</form>
</div>
