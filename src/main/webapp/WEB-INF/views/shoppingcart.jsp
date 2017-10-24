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

        <title>ShoppingCart</title>
        <link  href="${contextPath}/resources/css/jquery-ui.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
        <script src="${contextPath}/resources/js/jquery-1.12.4.min.js"></script>
        <script src="${contextPath}/resources/js/jquery-ui.js"></script>
        <script src="${contextPath}/resources/js/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
        <script>

            function addURL(element)
            {
                $(element).attr('href', function () {
                    return this.href
                            + '&shopName=' + $('#shopName').val()
                            + '&tourGuideName=' + $('#tourGuideName').val()
                            + '&tourGuideID=' + $('#tourGuideID').val()
                            + '&touristName=' + $('#touristName').val()
                            + '&pickupDate=' + $('#pickupDate').val()
                            + '&pickupTime=' + $('#pickupTime').val();

                    ;
                });
            }

            $(function () {
                $("#pickupDate").datepicker({dateFormat: 'yy-mm-dd'});
            });


            $("#submit").click(function (event) {
                if (!confirm("confirm x order"))
                    event.preventDefault();
            });

            $(document).ready(function () {


                /*
                 $.validator.addMethod("valueNotEquals", function (value, element, arg) {
                 return arg !== value;
                 }, "Choose Apotheke");
                 */
                $('#shoppingcartform').validate({// initialize the plugin

                    submitHandler: function (form) {
                        if (confirm("Click Ok to Submit")) {
                            form.submit();
                        }
                    },
                    rules: {
                        shopName: {
                            required: true
                        },

                        pickupDate: {
                            required: true
                        },
                        pickupTime: {
                            required: true
                        },
                        tourGuideName: {
                            required: true
                        },
                        tourGuideID: {
                            required: true
                        },
                        touristName: {
                            required: true
                        }
                    },

                    messages: {

                        shopName: {
                            required: 'Please select the Apotheke'
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

                <h2>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a> | <a href="${contextPath}/welcome">Home</a></h2>

            </c:if>


            <c:if test="${not empty sessionScope.shoppingCart.orders}">


                <div class="form-group">
                    <form accept-charset="UTF-8"  method = "POST" action = "${contextPath}/shoppingcart" id="shoppingcartform">

                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Apotheken Bestellung</th>
                                    <th>D&C Health Management</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        Abholung Datum: <input type="text" name="pickupDate" id="pickupDate" value="${pickupDate}"></input>
                                    </td>
                                    <td>
                                        Abholung Time: <input type="text" name="pickupTime" id="pickupTime"  value="${pickupTime}"/>
                                    </td>
                                    <td>Apotheke:
                                        <select name="shopName" id="shopName"  value="${shopName}" class="custom-select">
                                            <option value="" ${shopName==''?'selected':''}>Select One...</option>
                                            <option  value="Muenchen Dr. Beckers Central Apotheke" ${shopName=='Muenchen Dr. Beckers Central Apotheke'?'selected':''} >Muenchen - Dr. Beckers Central Apotheke</option>
                                            <option value="Frankfurt Prinzen Apotheke"  ${shopName=='Frankfurt Prinzen Apotheke'?'selected':''} >Frankfurt - Prinzen Apotheke</option>
                                            <option value="Frankfurt Struwwelpeter Apotheke" ${shopName=='Frankfurt Struwwelpeter Apotheke'?'selected':''} >Frankfurt - Struwwelpeter Apotheke</option>
                                            <option value="Fuessen - Stadtapotheke" ${shopName=='Fuessen - Stadtapotheke'?'selected':''} >Fuessen - Stadtapotheke</option>
                                            <option value="Hamburg - Apotheke am Hauptbahnhof" ${shopName=='Hamburg - Apotheke am Hauptbahnhof'?'selected':''} >Hamburg - Apotheke am Hauptbahnhof</option>
                                        </select>
                                    </td>
                                <tr>
                                <tr>
                                    <td>    
                                        ReiseleiterName: <input type="text" name="tourGuideName" value="${tourGuideName}" id="tourGuideName"/>
                                    </td>
                                    <td> 
                                        Reiseleiter ID : <input type="text" name="tourGuideID" value="${tourGuideID}" id="tourGuideID"  />
                                    </td>
                                    <td> 
                                        GastName : <input type="text" name="touristName" value="${touristName}" id="touristName"/>
                                    </td>
                                <tr>
                            </tbody>
                        </table>



                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>PZN</th>
                                    <th>German</th>
                                    <th>Amount</th>
                                    <th>Chinese</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${sessionScope.shoppingCart.orders}"  varStatus="productIndex">
                                    <tr>
                                        <td>${productIndex.index +1}</td>
                                        <td>${order.key.pzn}</td>
                                        <td>${order.key.germanName}</td>
                                        <td>
                                            <a onclick="javascript:addURL(this);" href="${contextPath}/shoppingcart?action=plus&pzn=${order.key.pzn}">
                                                <span style="font-size:150%;" class="glyphicon glyphicon-plus"></span>
                                            </a>

                                            ${order.value}

                                            <a onclick="javascript:addURL(this);" href="${contextPath}/shoppingcart?action=minus&pzn=${order.key.pzn}">
                                                <span style="font-size:150%;" class="glyphicon glyphicon-minus"></span>
                                            </a>
                                        </td>
                                        <td>${order.key.chineseName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <input type="submit" id="submit" name="submit" value="submitorder" />
                    </form>
                </div>
            </div>




        </c:if>




    </div>
</body>
</html>
