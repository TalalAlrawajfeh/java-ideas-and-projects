<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Products Management</title>
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
                    <li><a href="./management-home">Home</a></li>
                    <li><a href="#">Settings</a></li>
                    <li><a href="./sales">Sales</a></li>
                    <li class="active"><a href="./products">Products</a></li>
                    <li><a href="./receipts">Add Receipt</a></li>
                </ul>
            </div>
        </nav>

        <div class="container">
            <h2>Products List</h2>
            <p>Available Products:</p>

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
                                <a href="./products?pageNumber=${minPageNumber - 1}">
                                    <span>&laquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                        </c:if>
                        <c:forEach var="i" begin="${minPageNumber}" end="${maxPageNumber}">
                            <li <c:if test="${pageNumber eq i}">class="active"</c:if>>
                                <a href="./products?pageNumber=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${isLastSectionOfPages eq false}">
                            <li>
                                <a href="./products?pageNumber=${maxPageNumber + 1}">
                                    <span>&raquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </c:if>

            <button type="button" class="btn btn-primary" onclick="addProduct();">Add New Product</button>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Code</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Quantity Remaining</th>
                        <th>Quantity Sold</th>
                        <th>Controls</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${pageEntities}" var="product">
                        <tr>
                            <td>${product.code}</td>
                            <td>${product.description}</td>
                            <td>${product.price.toString()}</td>
                            <td>${product.quantityRemaining}</td>
                            <td>${product.quantitySold}</td>
                            <td>
                                <a class="btn btn-info" onclick="editProduct('${product.code}', '${product.description}', '${product.price.toString()}', '${product.quantityRemaining}');" role="button"> <span class="glyphicon glyphicon-pencil"></span> Edit</a>
                                <a class="btn btn-danger" onclick="deleteProduct('${product.code}');"> <span class="glyphicon glyphicon-trash"></span> Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="addNewProductModal" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add New Product</h4>
                        </div>
                        <div class="modal-body">
                            <form id="inputForm" action="./products" method="post">
                                <input type="hidden" id="action" name="action">
                                <input type="hidden" id="oldCode" name="oldCode" value="">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div id="alertInvalid" class="alert alert-warning">
                                            <strong>Error!</strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Code:</label>
                                        <input class="form-control" id="code" name="code">
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Description:</label>
                                        <input class="form-control" id="description" name="description">
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Price:</label>
                                        <input class="form-control" id="price" name="price">
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Quantity:</label>
                                        <input class="form-control" id="quantity" name="quantity">
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button id="modalSubmit" type="button" class="btn btn-default" onclick="validateFieldsAndSubmit();">Add</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value="/resources/management-products.js"/>"></script>
</body>

</html>