<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Management Home</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />">
    <script src="<c:url value="/resources/jquery/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</head>

<body>
    <div class="container">
        <div class="page-header">
            <h1>System Management</h1>
        </div>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header"> <a class="navbar-brand" href="#">Pages</a> </div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="./management-home">Home</a></li>
                    <li><a href="#">Settings</a></li>
                    <li><a href="./sales">Sales</a></li>
                    <li><a href="./products">Products</a></li>
                    <li><a href="./receipts">Add Receipt</a></li>
                </ul>
            </div>
        </nav>
        <div class="container">
            <h3>Welcome</h3>
            <p>Use the navigation bar to navigate through the site.</p>
        </div>
    </div>
</body>

</html>