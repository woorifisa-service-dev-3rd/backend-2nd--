package dev.lawlesszone.domain.payment.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.service.MemberService;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.SendPaymentDTO;
import dev.lawlesszone.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller()
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public ModelAndView showPaymentList(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView mav = new ModelAndView();
        List<SendPaymentDTO> paymentList=paymentService.findValidPaymentsByMemberEmail(userDetails.getUsername());

        if (paymentList.isEmpty()) {
            mav.setViewName("redirect:/member/detail");
            return mav;
        }
        mav.addObject("paymentList", paymentList);
        mav.setViewName("payment/list");
        return mav;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/cancel")
    public ModelAndView cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }

    //id 넣어 놓고
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/validate/{uid}")
    public ModelAndView createPayment(@RequestBody PaymentDTO paymentDTO,
                                @AuthenticationPrincipal UserDetails userDetails) throws IamportResponseException, IOException {
        log.info("결제 처리 진행중");
        return paymentService.checkValid(paymentDTO,userDetails.getUsername());
    }

}
