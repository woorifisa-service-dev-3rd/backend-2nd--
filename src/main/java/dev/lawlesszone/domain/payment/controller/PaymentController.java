package dev.lawlesszone.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import dev.lawlesszone.domain.Member.dto.CustomUserDetail;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.SendPaymentDTO;
import dev.lawlesszone.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public ResponseEntity<List<SendPaymentDTO>> showPaymentList(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        log.info(customUserDetail.toString());
        List<SendPaymentDTO> paymentList = paymentService.findValidPaymentsByMemberEmail(customUserDetail.getEmail());
        log.info(paymentList.toString());
        return ResponseEntity.ok(paymentList);
    }

    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long id) {
        log.info("여기 왔음");
        int result = paymentService.cancelPayment(id);
        if (result == 1) {
            return ResponseEntity.badRequest().body("현재 취소할게 없습니다");
        } else if (result == 2) {
            return ResponseEntity.badRequest().body("현재꺼는 기간 만료입니다");
        } else if (result == 3) {
            return ResponseEntity.badRequest().body("이미 완료됨");
        } else if (result == 4) {
            return ResponseEntity.badRequest().body("취소 거부됨");
        } else if (result == 5){
            return ResponseEntity.ok("취소가 완료되었습니다");
        }
        else
            return ResponseEntity.badRequest().body("무언가 오류남");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/validate")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO,
                                           @AuthenticationPrincipal CustomUserDetail customUserDetail) throws IamportResponseException, IOException {
        log.info(paymentDTO.getImpUid());
        log.info("결제 처리 진행중");
        log.info("하이"+customUserDetail.getEmail());
        if (paymentService.checkValid(paymentDTO,customUserDetail.getEmail())) {
            return ResponseEntity.ok("결제완료");
        }
        return ResponseEntity.badRequest().body("결제 실패");
    }
    @GetMapping()
    public void pay(){

    }
}
