<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<script src="http://cdn.tinymce.com/4/tinymce.min.js"></script>
<script>
	tinymce.init({
		selector : 'textarea.rich'
	});
</script>

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
	<h1 class="page-header">Add Page</h1>
	<form class="form-horizontal" action="./add-page" method="post">
		<div class="col-xs-12 col-sm-12">
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Page Title</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="title" name="title" class="form-control"
						placeholder="Title" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Page URI</label>
				<div class="col-xs-12 col-sm-6">
					<input type="text" id="uri" name="uri" class="form-control"
						placeholder="URI" required autofocus>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Site</label>
				<div class="col-xs-12 col-sm-6">
					<select name="site" class="form-control">
						<c:forEach items="${sites}" var="site">
							<option value="${site.uri}">${site.name} - ${site.uri}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">SEO</label>
				<div class="col-xs-12 col-sm-9">
					<textarea name="seo" rows="5"></textarea>
				</div>
			</div>
			<div class="row form-group">
				<label class="col-xs-12 col-sm-3">Page Content</label>
				<div class="col-xs-12 col-sm-9">
					<textarea name="content" class="rich" rows="10"></textarea>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-xs-12 col-sm-6 col-sm-offset-3">
					<button type="submit" class="btn btn-primary">Save</button>
					&nbsp; <a class="btn btn-danger" href="./page-management?filter=all">Cancel</a>&nbsp;
				</div>
			</div>
		</div>
	</form>
</div>
