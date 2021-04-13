<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Login Page</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />">
    <script src="<c:url value="/resources/jquery/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</head>

<body>
    <div class="container">
        <div class="page-header">
            <h1>Login</h1>
        </div>
        <c:if test="${showError == true}">
            <div class="row">
                <div class="col-md-4">
        		    <div class="alert alert-danger">
        		        <strong>Error!</strong> ${errorMessage}
        	        </div>
        	    </div>
            </div>
        </c:if>
        <form action="" method="post" class="form-signin">
            <div class="row">
                <div class="col-md-4">
                    <label>User Name:</label>
                    <input class="form-control" id="username" name="username">
                </div>
            </div>
            <div class="row">&nbsp;</div>
            <div class="row">
                <div class="col-md-4">
                    <label>Password:</label>
                    <input type="password" class="form-control" id="password" name="password"> </div>
            </div>
            <div class="row">&nbsp;</div>
            <div class="row">
                <div class="col-md-4">
                    <button type="submit" class="btn btn-default">Login</button>
                </div>
            </div>
        </form>
    </div>
</body>

</html>