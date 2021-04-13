<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<div class="col-sm-9 col-sm-offset-2">
	<h1 class="page-header">Site Management</h1>
	<div class="col-xs-12">
		<div class="pull-right">
			<a class="btn btn-info" href="./add-site">Add Site</a>
		</div>
	</div>

	<div class="col-xs-12">
		<table class="table table-striped table-hover">
			<thead>
				<th>Site name</th>
				<th>Site URI</th>
				<th>Parent</th>
				<th></th>
			</thead>
			<tbody>
				<c:forEach items="${sites}" var="site">
					<tr>
						<td>${site.name}</td>
						<td>${site.uri}</td>
						<c:choose>
							<c:when test="${empty site.parentSite}">
								<td>none</td>
							</c:when>
							<c:otherwise>
								<td>${site.parentSite.uri}</td>
							</c:otherwise>
						</c:choose>
						<c:if test="${site.uri ne '/root'}">
							<td><a class="btn btn-primary"
								href="./edit-site?uri=${site.uri}">Edit</a></td>
						</c:if>
						<c:if test="${site.uri == '/root'}">
							<td></td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

