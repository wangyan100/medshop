<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="GBK" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="zh">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Welcome</title>
        <link  href="${contextPath}/resources/css/jquery-ui.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
        <script src="${contextPath}/resources/js/jquery-1.12.4.min.js"></script>
        <script src="${contextPath}/resources/js/jquery-ui.js"></script>
        <script src="${contextPath}/resources/js/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
    </head>
    <body>
        <script>
                function confirmSubmit(element) {
                var amount;
                $(element).attr('href', function () {
                    amount =$(element).parent().prev().find('input').val();
                    this.href = this.href+'&amount=' + amount;
                });
                
                return ! isNaN(amount)
                //return confirm("add Prouction Amount "+amount);
            }
            
        </script>
        <div class="container">

            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <form id="logoutForm" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>

                <h2>Welcome ${pageContext.request.userPrincipal.name} | 
                    <a onclick="document.forms['logoutForm'].submit()">Logout</a> |
                    <a href="${contextPath}/upload">Upload Excel</a> |
                    <a href="${contextPath}/shoppingcart">ShoppingCart</a> |
                    <a href="${contextPath}/orders">Orders</a> |
                    <a href="${contextPath}/report">StatisticReport</a>
                </h2>

            </c:if>

            <c:if test="${not empty products}">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>PZN</th>
                            <th>German</th>
                            <th>Chinese</th>
                            <th>Unit</th>
                            <th>Price</th>
                            <th> </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${products}"  varStatus="productIndex">
                            <tr>
                                <td>${productIndex.index +1 }</td>
                                <td>${product.pzn}</td>
                                <td>${product.germanName}</td>
                                <td>${product.chineseName}</td>
                                <td>${product.unit}</td>
                                <td>${product.price}</td>
                                <td>
                                    <input name="amount" type="number" value=1  min="1" max="999"/>
                                </td>
                                <td>
                                    <a onclick="confirmSubmit(this)" href="${contextPath}/welcome?action=addShoppingCart&pzn=${product.pzn}">
                                        <button type="button" class="btn btn-primary">Add to ShoppingCart</button>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

        </div>
    </body>
</html>
