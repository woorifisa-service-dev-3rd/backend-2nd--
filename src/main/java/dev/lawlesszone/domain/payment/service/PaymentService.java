package dev.lawlesszone.domain.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.payment.dto.PaymentDTO;
import dev.lawlesszone.domain.payment.dto.PreparationRequest;
import dev.lawlesszone.domain.payment.dto.PreparationResponse;
import dev.lawlesszone.domain.payment.dto.SendPaymentDTO;
import dev.lawlesszone.domain.payment.entity.Payment;
import dev.lawlesszone.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final IamportClient iamportClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper jacksonObjectMapper;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    public List<SendPaymentDTO>findAllByMemberEmail(String email) {
        List<Payment> paymentList= paymentRepository.findAllByMemberEmail(email);
        return paymentList.stream()
                .filter(payment -> payment.isValid())
                .map(this::DTOToEntity).collect(Collectors.toList());
    }
    SendPaymentDTO DTOToEntity(Payment payment) {
        return SendPaymentDTO.builder()
                .id(payment.getId())
                .valid(payment.isValid())
                .build();
    }

    public void saveOrUpdate(PaymentDTO paymentDTO,String email) {
        Member member=memberRepository.findByEmail(email).orElseThrow();
        List<Payment> paymentList= member.getPayment();
        System.out.println("paymet"+paymentList);

        Payment newPayment= Payment.builder()
                .valid(true)
                .merchantUid(paymentDTO.getMerchantUid())
                .build();

        newPayment.setMember(member);
        paymentRepository.save(newPayment);
        member.setPremium(member.getPremium() +30);
        memberRepository.save(member);

    }
    public Payment findMerchantUid(Long id) {
        Payment payment= paymentRepository.findById(id).orElseThrow();
        return payment;
    }



    public String getToken() {
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = String.format("{\"imp_key\":\"%s\",\"imp_secret\":\"%s\"}", "8710522058100173", "Qpobt6L7MZz8PK3567wGExmjLZ0E87nmk6Z0ziuQzYmriKVI7ea9UhwaRkODYVaGA4MmHuLmmsXbyiQE");
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        // JSON 응답에서 액세스 토큰 추출
        System.out.println(response.getBody());
        return extractAccessToken(response.getBody());
    }
    public Integer checkCancel(Member member,String accessToken,Payment payment){
        String url="https://api.iamport.kr/payments/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", accessToken);
        String body= String.format("{\"merchant_uid\":\"%s\"}",payment.getMerchantUid());
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);


        Integer i= check(response.getBody());
        if(i==1){
            return i;
        }
        if(i==200){
            member.getPayment().remove(payment);
            payment.setValid(false);
            if(member.getPremium()<30){
                member.setPremium(0);
            }
            else{
                member.setPremium(member.getPremium()-30);
            }
            memberRepository.save(member);
            paymentRepository.save(payment);
        }
        return i;
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
    private Integer check(String responseBody) {
        try {
            JsonNode jsonNode = jacksonObjectMapper.readTree(responseBody);
            JsonNode codeNode = jsonNode.get("code");

            if (codeNode.asInt() == 0) {
                return 200;
            }
            else if (codeNode.asInt() == 1) {
                return 1;
            }
            else {
                return 400;
            }
        } catch (Exception e) {
            e.printStackTrace(); // 또는 적절한 예외 처리
        }
        return null;
    }

}
