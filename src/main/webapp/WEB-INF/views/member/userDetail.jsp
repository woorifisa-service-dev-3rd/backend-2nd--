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
</head>
<body>
<header>
    <h1>Welcome to My Personal Page</h1>
</header>
<button onclick="requestPay()">결제하기</button>
<a href="/payment/list">취소하러 가기</a>
<div>
    현재 남은 프리미엄 일 수는 ${member.premium}
</div>

<script>
    var IMP = window.IMP;
    IMP.init("imp44545746"); // 가맹점 식별코드, 예: imp00000000

    function requestPay() {
        // IMP.request_pay(param, callback) 결제창 호출
        IMP.request_pay({ // param
            pg: "html5_inicis",
            pay_method: "card",
            merchant_uid: "<%= "ORD" + System.currentTimeMillis() %>", // 고유 주문번호 생성
            name: "왕관",
            amount: 100,
            buyer_email: "gildong@gmail.com",
            buyer_name: "홍길동",
            buyer_tel: "010-4242-4242",
            buyer_addr: "서울특별시 강남구 신사동",
            buyer_postcode: "01181"
        }, function (rsp) { // callback
            if (rsp.success) {
                // 결제 성공 시 처리 로직

                $.ajax({
                    url: "/payment/validate/" + rsp.imp_uid,
                    method: "post",
                    contentType: "application/json",
                    data: JSON.stringify({
                        impUid: rsp.imp_uid,            // 결제 고유번호
                        merchantUid: rsp.merchant_uid,   // 주문번호
                        amount: rsp.paid_amount
                    }),
                }).done(function () {
                    alert("결제가 성공했습니다.");
                    // 성공한 데이터를 서버로 전달
                })
            } else {
                // 결제 실패 시 처리 로직
                alert("결제가 실패했습니다. 에러 내용: " + rsp.error_msg);
            }
        });
    }
</script>
</body>
</html>