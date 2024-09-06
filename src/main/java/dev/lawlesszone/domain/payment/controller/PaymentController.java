package dev.lawlesszone.domain.payment.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import dev.lawlesszone.domain.payment.dto.CancelPaymentDTO;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.PreparationRequest;
import dev.lawlesszone.domain.payment.dto.PreparationResponse;
import dev.lawlesszone.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller()
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final IamportClient iamportClient;
    private final PaymentService paymentService;

    @GetMapping("/{id}/cancel")
    public void cancelPayment(@PathVariable Long id, @RequestBody CancelPaymentDTO cancelPaymentDTO) {

        // 멤버가 가지고 있는 정보에서 빼내서
        paymentService.checkCancel(paymentService.getToken(), "ORD1725600473066");
    }

    //id 넣어 놓고
    @PostMapping("{id}/validate/{uid}")
    public String createPayment(@RequestBody PaymentDTO paymentDTO, @PathVariable Long id, HttpServletRequest request) {
        log.info("payment", paymentDTO);
        paymentService.saveOrUpdate(paymentDTO, id);
        //이제  사용자에서 처리
        return "payment/payment";
    }

    @GetMapping("/test")
    public String test() {
        return "payment/payment";
    }

    @GetMapping("/test2")
    public String test2() {

        return "payment/";
    }
    @GetMapping("/user")
    public String user() {
        return "payment/userDetail";
    }
}
