<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Management Page</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />">
    <script src="<c:url value="/resources/jquery/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</head>

<body>
    <div class="container">
        <div class="page-header">
            <h1>System Management</h1> </div>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header"> <a class="navbar-brand" href="#">Pages</a> </div>
                <ul class="nav navbar-nav">
                    <li><a href="./management-home">Home</a></li>
                    <li><a href="#">Settings</a></li>
                    <li class="active"><a href="./sales">Sales</a></li>
                    <li><a href="./products">Products</a></li>
                    <li><a href="./receipts">Add Receipt</a></li>
                </ul>
            </div>
        </nav>

        <div class="container">
            <h2>Sales</h2>
            <p>All sales from newest to oldest:</p>

            <c:if test="${showError == true}">
                <div class="alert alert-danger">
                    <strong>Error!</strong> ${errorMessage}
                </div>
            </c:if>

            <c:if test="${not empty minPageNumber and minPageNumber ge 1}">
                <div>
                    <ul class="pagination pagination-lg">
                        <c:if test="${minPageNumber > 1}">
                            <li>
                                <a href="./sales?pageNumber=${minPageNumber - 1}">
                                    <span>&laquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                        </c:if>
                        <c:forEach var="i" begin="${minPageNumber}" end="${maxPageNumber}">
                            <li <c:if test="${pageNumber eq i}">class="active"</c:if>>
                                <a href="./sales?pageNumber=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${isLastSectionOfPages eq false}">
                            <li>
                                <a href="./sales?pageNumber=${maxPageNumber + 1}">
                                    <span>&raquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </c:if>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Date and Time</th>
                        <th>Code</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${pageEntities}" var="receipt">
                        <tr>
                            <td>${receipt.date}</td>
                            <td>${receipt.product.code} - ${receipt.product.description}</td>
                            <td>${receipt.price.toString()}</td>
                            <td>${receipt.quantity}</td>
                            <td>${receipt.total.toString()}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>

</html>