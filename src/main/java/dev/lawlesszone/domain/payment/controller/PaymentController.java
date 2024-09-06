package dev.lawlesszone.domain.payment.controller;


import com.siot.IamportRestClient.IamportClient;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.service.MemberService;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.SendPaymentDTO;
import dev.lawlesszone.domain.payment.entity.Payment;
import dev.lawlesszone.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller()
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final IamportClient iamportClient;
    private final PaymentService paymentService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showPaymentList(Authentication authentication, Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<SendPaymentDTO> paymentList = paymentService.findAllByMemberEmail(email);
        if (paymentList.isEmpty()) {
            return "redirect:/member/detail";
        }
        model.addAttribute("paymentList", paymentList);

        return "payment/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/cancel")
    public String cancelPayment( Model model, @PathVariable Long id) {

        Payment payment = paymentService.findMerchantUid(id);

        if (!payment.isValid()) {
            model.addAttribute("result", "현재 취소할게 없습니다");
            return "redirect:/member/detail";
        }
        log.info(payment.getMember().toString());
        Integer code = paymentService.checkCancel(payment.getMember(), paymentService.getToken(), payment);
        log.info("여기 코드값: "+code);
        if (code == null) {
            model.addAttribute("result", "현재꺼는 기간 만료입니다");
            return "redirect:/member/detail";
        }
        if(code==1){
            model.addAttribute("result", "이미 완료됨");
            return "redirect:/member/detail";
        }
        model.addAttribute("result", "취소가 완료되었습니다");
        return "redirect:/member/detail";
    }

    //id 넣어 놓고
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/validate/{uid}")
    public String createPayment(Model model, @RequestBody PaymentDTO paymentDTO, Authentication authentication) {
        log.info("payment", paymentDTO);
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        paymentService.saveOrUpdate(paymentDTO, email);
        //이제  사용자에서 처리
        model.addAttribute("result", "결제 완료");
        return "redirect:/member/detail";
    }

    @GetMapping()
    public String showPayment(Authentication authentication, Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        Member member = memberService.findByEmail(email);
        model.addAttribute("memberId", member.getId());
        return "payment/payment";
    }
}
