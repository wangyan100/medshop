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

        <title>StatisticReport</title>
        <link  href="${contextPath}/resources/css/jquery-ui.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
        <script src="${contextPath}/resources/js/jquery-1.12.4.min.js"></script>
        <script src="${contextPath}/resources/js/jquery-ui.js"></script>
        <script src="${contextPath}/resources/js/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
        <script>



            $(function () {
                $("#fromDate").datepicker({dateFormat: 'yy-mm-dd'});
                $("#toDate").datepicker({dateFormat: 'yy-mm-dd'});
            });

            $("#submit").click(function (event) {
                if (!confirm("confirm x order"))
                    event.preventDefault();
            });

            $(document).ready(function () {

                $('#reportform').validate({// initialize the plugin

                    rules: {

                        fromDate: {
                            required: true
                        },
                        toDate: {
                            required: true
                        },
                        report: {
                            required: true
                        }
                    },

                    messages: {

                        shopName: {
                            required: 'Please select the report'
                        }
                    }
                });

            });
        </script>


    </head>
    <body>
        <div class="container">

            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <form id="logoutForm" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>

                <h2>Statistic Report ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a> | <a href="${contextPath}/welcome">Home</a></h2>

            </c:if>



            <div class="form-group">
                <form  method = "POST" action = "${contextPath}/report" id="reportform">

                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Statistic</th>
                                <th>Report</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    From Date: <input type="text" name="fromDate" id="fromDate" value="${fromDate}"></input>
                                </td>
                                <td>
                                    To Date: <input type="text" name="toDate" id="toDate"  value="${toDate}"/>
                                </td>
                                <td>Report:
                                    <select name="report" id="report"  value="${report}" class="custom-select">
                                        <option  value="shopname-totalprice" >Apotheke - Umsatz</option>
                                        <option value="shopname-totalorder" >Apotheke - Menge</option>
                                        <option value="tourguide-totalprice">Reiseleiter - Umsatz</option>
                                        <option value="tourguide-totalorder">Reiseleiter - Menge</option>
                                    </select>
                                </td>
                            <tr>

                        </tbody>
                    </table>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <input type="submit" id="submit" name="submit" value="report" />



                    <c:if test="${not empty statisticReports}">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>${headercolum1}</th>
                                    <th>${headercolum2}</th>
                                    <th>${headercolum3}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${statisticReports}"  varStatus="orderIndex">
                                    <tr>
                                        <td>${orderIndex.index +1}</td>

                                        <td>
                                            ${order.shopname==null?'':order.shopname} 
                                            ${order.tourguidename==null? '':order.tourguidename}
                                        </td>
                                        <td>
                                            ${order.totalprice==null? '': order.totalprice}
                                            ${order.amount==0? '': order.amount}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>


                </form>
            </div>
        </div>
    </div>
</body>
</html>
