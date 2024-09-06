<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Personal Page</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
    <!-- iamport.payment.js -->
    <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>


<c:forEach items="${requestScope.paymentList}" var="payment">
    <c:if test="${payment.valid}">
        <div>
            <div>${payment.id} <a href="/payment/${payment.id}/cancel">결제 취소</a>
            </div>
        </div>
    </c:if>
</c:forEach>




</body>
</html>