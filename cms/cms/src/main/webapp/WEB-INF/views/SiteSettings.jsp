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
	<h1 class="page-header">Site Settings</h1>
	<form class="form-horizontal" action="./site-settings" method="post"
		enctype="multipart/form-data">
		<input type="hidden" id="oldSiteSettings" name="oldSiteSettings"
			<c:if test="${not empty siteSettings}"> value="${siteSettings.deliveryUrl}" </c:if>>
		<div class="col-xs-12 col-sm-8">
			<div class="row form-group">
				<div class="col-xs-12 col-sm-2">Web site name</div>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="name" name="name"
						<c:if test="${not empty siteSettings}"> value="${siteSettings.name}" </c:if>
						class="form-control" placeholder="Name" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-2">Site Logo</div>
				<div class="col-xs-12 col-sm-6">
					<input type="file" id="logo" name="logo" class="form-control"
						placeholder="Logo" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-2">Delivery site URL</div>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="deliveryUrl" name="deliveryUrl"
						<c:if test="${not empty siteSettings}"> value="${siteSettings.deliveryUrl}" </c:if>
						class="form-control" placeholder="Delivery URL" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-8">
					<input type="submit" value="Save"
						class="btn btn-primary pull-right" />
				</div>
			</div>
		</div>
	</form>
	<c:if test="${not empty image}">
		<img class="img-responsive" src="data:image/jpg;base64, ${image}" />
	</c:if>
</div>

