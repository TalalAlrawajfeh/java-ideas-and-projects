<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Add Receipt</title>
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
                    <li><a href="./products">Products</a></li>
                    <li class="active"><a href="./receipts">Add Receipt</a></li>
                </ul>
            </div>
        </nav>

        <div class="container">
            <h2>Receipt List</h2>
            <p>Receipts:</p>

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
                                <a href="./receipts?pageNumber=${minPageNumber - 1}">
                                    <span>&laquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                        </c:if>
                        <c:forEach var="i" begin="${minPageNumber}" end="${maxPageNumber}">
                            <li <c:if test="${pageNumber eq i}">class="active"</c:if>>
                                <a href="./receipts?pageNumber=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${isLastSectionOfPages eq false}">
                            <li>
                                <a href="./receipts?pageNumber=${maxPageNumber + 1}">
                                    <span>&raquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </c:if>

            <button type="button" class="btn btn-primary" onclick="addReceipt();">Add New Receipt</button>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                        <th>Controls</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${pageEntities}" var="receipt">
                        <tr>
                            <td>${receipt.product.code} - ${receipt.product.description}</td>
                            <td>${receipt.price.toString()}</td>
                            <td>${receipt.quantity}</td>
                            <td>${receipt.total.toString()}</td>
                            <td>
                                <a class="btn btn-info" onclick="editReceipt('${receipt.id}', '${receipt.product.code}', '${receipt.price.toString()}', '${receipt.quantity}', '${receipt.total.toString()}');"> <span class="glyphicon glyphicon-pencil"></span> Edit</a>
                                <a class="btn btn-danger" onclick="deleteReceipt('${receipt.id}');"> <span class="glyphicon glyphicon-trash"></span> Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="addReceiptModal" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add New Receipt</h4>
                        </div>
                        <div class="modal-body">
                            <form id="inputForm" action="./receipts" method="post">
                                <input type="hidden" id="action" name="action">
                                <input type="hidden" id="receiptId" name="receiptId" value="">
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
                                        <label for="code">Product:</label>
                                        <select class="form-control" id="code" name="code" onclick="getProductPrice(value);">
                                            <c:forEach items="${products}" var="product">
                                                <option value="${product.code}">${product.code} - ${product.description}</option>
                                            </c:forEach>
                                        </select>
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
                                        <input class="form-control" id="quantity" name="quantity" oninput="calculateTotal();">
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Total:</label>
                                        <input class="form-control" id="total" name="total">
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

    <script src="<c:url value="/resources/management-receipts.js"/>"></script>
</body>

</html>