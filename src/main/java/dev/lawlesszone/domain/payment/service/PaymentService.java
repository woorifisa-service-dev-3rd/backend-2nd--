package dev.lawlesszone.domain.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.SendPaymentDTO;
import dev.lawlesszone.domain.payment.entity.Payment;
import dev.lawlesszone.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final RestTemplate restTemplate;
    private final ObjectMapper jacksonObjectMapper;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final IamportClient iamportClient;

    @Transactional
    public ModelAndView checkValid(PaymentDTO paymentDTO ,String email) throws IamportResponseException, IOException {
        ModelAndView mav= new ModelAndView();
        mav.setViewName("redirect:/member/detail");
        log.info("결제 검증 중");
        IamportResponse<com.siot.IamportRestClient.response.Payment> response= iamportClient.paymentByImpUid(paymentDTO.getImpUid());
        if (response.getCode() != 0||  response.getResponse().getAmount().compareTo(BigDecimal.valueOf(100)) != 0 ) {
            mav.addObject("result", "결제 오류");
            log.info("결제 오류");
            return mav;
        }

        Member member = memberRepository.findByEmail(email).orElseThrow();
        Payment newPayment = Payment.builder()
                .valid(true)
                .merchantUid(paymentDTO.getMerchantUid())
                .build();

        newPayment.addMember(member);
        paymentRepository.save(newPayment);
        mav.addObject("result", "결제 완료");
        log.info("결제완료");
        return mav;

    }

    public List<SendPaymentDTO> findAllByMemberEmail(String email) {
        List<Payment> paymentList = paymentRepository.findAllByMemberEmail(email);
        return paymentList.stream()
                .filter(payment -> payment.isValid())
                .map(this::DTOToEntity).collect(Collectors.toList());
    }

    @Transactional
    public ModelAndView cancelPayment(Long id) {
        ModelAndView mav = new ModelAndView();
        Payment payment = paymentRepository.findById(id).orElseThrow();
        mav.setViewName("redirect:/member/detail");
        if (!payment.isValid()) {
            mav.addObject("result", "현재 취소할게 없습니다");
            return mav;
        }
        Integer code = checkCancel(payment, getToken());
        if (code == null) {
            mav.addObject("result", "현재꺼는 기간 만료입니다");
            return mav;
        } else if (code == 1) {
            mav.addObject("result", "이미 완료됨");
            return mav;
        } else if (code == 400) {
            mav.addObject("result", "취소 거부됨");
            return mav;
        }
        mav.addObject("result", "취소가 완료되었습니다");
        return mav;
    }

    @Transactional
    public Integer checkCancel(Payment payment, String accessToken) {
        String url = "https://api.iamport.kr/payments/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", accessToken);
        String body = String.format("{\"merchant_uid\":\"%s\"}", payment.getMerchantUid());
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        Integer i = check(response.getBody());
        if (i == 200) {
            payment.invalidate();
            paymentRepository.save(payment);
        }
        return i;
    }

    private Integer check(String responseBody) {
        try {
            JsonNode jsonNode = jacksonObjectMapper.readTree(responseBody);
            JsonNode codeNode = jsonNode.get("code");
            if (codeNode.asInt() == 0) {
                return 200;
            } else if (codeNode.asInt() == 1) {
                return 1;
            } else {
                return 400;
            }
        } catch (Exception e) {
            e.printStackTrace(); // 또는 적절한 예외 처리
        }
        return null;
    }

    public String getToken() {
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = String.format("{\"imp_key\":\"%s\",\"imp_secret\":\"%s\"}", "8710522058100173",
                "Qpobt6L7MZz8PK3567wGExmjLZ0E87nmk6Z0ziuQzYmriKVI7ea9UhwaRkODYVaGA4MmHuLmmsXbyiQE");
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        // JSON 응답에서 액세스 토큰 추출
        System.out.println(response.getBody());
        return extractAccessToken(response.getBody());
    }

    private String extractAccessToken(String responseBody) {
        try {
            JsonNode jsonNode = jacksonObjectMapper.readTree(responseBody);
            JsonNode responseNode = jsonNode.get("response");
            if (responseNode != null) {
                JsonNode accessTokenNode = responseNode.get("access_token");
                if (accessTokenNode != null) {
                    return accessTokenNode.asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 또는 적절한 예외 처리
        }
        return null;
    }

    SendPaymentDTO DTOToEntity(Payment payment) {
        return SendPaymentDTO.builder()
                .id(payment.getId())
                .valid(payment.isValid())
                .build();
    }
}
