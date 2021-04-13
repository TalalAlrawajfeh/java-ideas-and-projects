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
	<h1 class="page-header">Edit Site</h1>
	<form class="form-horizontal" action="./edit-site" method="post">
		<input type="hidden" id="uri" name="uri" value="${site.uri}">
		<div class="col-xs-12 col-sm-8">
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Site Name</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="name" name="name" value="${site.name}"
						class="form-control" placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Site URI</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" disabled value="${site.uri}"
						class="form-control" placeholder="Site URI" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Parent Site</label>
				<div class="col-xs-12 col-sm-6">
					<select disabled class="form-control">
						<c:choose>
							<c:when test="${empty site.parentSite}">
								<option value="none">none</option>
							</c:when>
							<c:otherwise>
								<option value="${site.parentSite.uri}">${site.parentSite.uri}</option>
							</c:otherwise>
						</c:choose>
					</select>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Landing Page</label>
				<div class="col-xs-12 col-sm-6">
					<select name="landingPage" class="form-control">
						<c:forEach items="${pages}" var="page">
							<c:if test="${page.site.uri eq site.uri}">
								<option <c:if test="${site.landingPage.uri eq page.uri}">selected="selected"</c:if> value="${page.uri}">${page.title} - ${page.uri}</option>
							</c:if>
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
