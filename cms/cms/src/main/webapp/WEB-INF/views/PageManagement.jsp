<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<div class="col-sm-9 col-sm-offset-2">
	<h1 class="page-header">Pages Management</h1>
	<div class="col-xs-6">
		<form class="form-horizontal" action="./page-management" method="get">
			<div class="input-group">
				<select id="filter" name="filter" class="form-control">
					<option
						<c:if test="${param.filter eq 'all'}">selected="selected"</c:if>
						value="all">All</option>
					<c:forEach items="${sites}" var="site">
						<option
							<c:if test="${param.filter eq site.uri}">selected="selected"</c:if>
							value="${site.uri}">${site.name} - ${site.uri}</option>
					</c:forEach>
				</select> <span class="input-group-btn">
					<button class="btn btn-default" type="submit">
						<i class="glyphicon glyphicon-filter"></i>Filter
					</button>
				</span>
			</div>
		</form>
	</div>
	<div class="col-xs-6">
		<div class="pull-right">
			<a class="btn btn-info" href="./add-page">Add Page</a>
		</div>
	</div>
	<div class="col-xs-12">
		<table class="table table-striped table-hover">
			<thead>
				<th>Page Title</th>
				<th>Page URI</th>
				<th>Site</th>
				<th>Is Landing</th>
				<th>Is Published</th>
				<th></th>
			</thead>
			<tbody>
				<c:forEach items="${pages}" var="page">
					<tr>
						<td>${page.title}</td>
						<td>${page.uri}</td>
						<c:choose>
							<c:when test="${empty page.site}">
								<td>none</td>
								<td>No</td>
							</c:when>
							<c:otherwise>
								<td>${page.site.name}- ${page.site.uri}</td>
								<c:choose>
									<c:when test="${page.uri eq page.site.landingPage.uri}">
										<td>Yes</td>
									</c:when>
									<c:otherwise>
										<td>No</td>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${page.isPublished.booleanValue() eq true}">
								<td>Yes</td>
							</c:when>
							<c:otherwise>
								<td>No</td>
							</c:otherwise>
						</c:choose>
						<td><a class="btn btn-primary"
							href="./edit-page?uri=${page.uri}">Edit</a> &nbsp;<a
							class="btn btn-danger" onclick="deletePage('${page.uri}')">Delete</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<script>
	function deletePage(uri) {
		var xmlHttpRequest = new XMLHttpRequest();
		xmlHttpRequest.open("DELETE", "./edit-page?uri=" + uri, false);

		xmlHttpRequest.onreadystatechange = function() {
			if (xmlHttpRequest.readyState === XMLHttpRequest.DONE) {
				if (xmlHttpRequest.status === 200) {
					window.location.reload();
					return;
				}

				if (xmlHttpRequest.status === 400) {
					window
							.alert("Error! A site has this page as its landing page.");
					return;
				}

				window.alert("An error occured while processing request");
			}
		};

		xmlHttpRequest.send();
	}
</script>
