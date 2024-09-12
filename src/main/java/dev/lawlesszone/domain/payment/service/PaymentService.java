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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
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
    public boolean checkValid(PaymentDTO paymentDTO, String email) throws IamportResponseException, IOException {
//        ModelAndView mav= new ModelAndView();
//        mav.setViewName("redirect:/member/detail");
        log.info("결제 검증 중");
        IamportResponse<com.siot.IamportRestClient.response.Payment> response =
                iamportClient.paymentByImpUid(paymentDTO.getImpUid());
        if (response.getCode() != 0 || response.getResponse().getAmount().compareTo(BigDecimal.valueOf(100)) != 0) {
//            mav.addObject("result", "결제 오류");
            log.info("결제 오류");
            return false;
        }

        Member member = memberRepository.findByEmail(email).orElseThrow();
        Payment newPayment = Payment.builder()
                .valid(true)
                .merchantUid(paymentDTO.getMerchantUid())
                .build();

        newPayment.addMember(member);
        paymentRepository.save(newPayment);
//        mav.addObject("result", "결제 완료");
        log.info("결제완료");
        return true;

    }

    public List<SendPaymentDTO> findValidPaymentsByMemberEmail(String email) {
        return paymentRepository
                .findAllByMemberEmail(email).stream()
                .filter(payment -> payment.isValid())
                .map(this::DTOToEntity).collect(Collectors.toList());
    }

    @Transactional
    public int cancelPayment(Long id) {

        Payment payment = paymentRepository.findById(id).orElseThrow();

        if (!payment.isValid()) {
            return 1;
        }
        Integer code = checkCancel(payment, getToken());
        if (code == null) {
            return 2;
        } else if (code == 1) {
            return 3;
        } else if (code == 400) {
            return 4;
        }
        return 5;
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
