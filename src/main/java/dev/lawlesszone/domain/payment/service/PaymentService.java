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

    public PreparationResponse prepareValid(PreparationRequest request) throws IamportResponseException, IOException {

        PrepareData prepareData = new PrepareData(request.getMerchantUid(), request.getTotalPrice());
        IamportResponse<Prepare> iamportResponse = iamportClient.postPrepare(prepareData);

        log.info("결과 코드 : {}", iamportResponse.getCode());
        log.info("결과 메시지 : {}", iamportResponse.getMessage());

        if (iamportResponse.getCode() != 0) {
            throw new RuntimeException(iamportResponse.getMessage());
        }
        return PreparationResponse.builder().merchantUid(request.getMerchantUid()).build();
    }

    public void saveOrUpdate(PaymentDTO paymentDTO,Long id) {
        Member member=memberRepository.findById(id).orElseThrow();
        Payment payment = member.getPayment();
        if(payment ==null){
            Payment newPayment= Payment.builder()
                    .merchantUid(paymentDTO.getMerchantUid())
                    .isPremium(30)
                    .build();
            paymentRepository.save(newPayment);
        }
        else{

            Payment newPayment= Payment.builder()
                    .merchantUid(paymentDTO.getMerchantUid())
                    .isPremium(payment.getIsPremium()+30)
                    .id(payment.getId())
                    .build();
            paymentRepository.save(newPayment);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void decreasePremiumStatus() {
        // 모든 결제 데이터에서 isPremium 값이 0보다 큰 경우 1 감소
        List<Payment> updatedPayments = paymentRepository.findAll().stream()
                .filter(payment -> payment.getIsPremium() > 0)
                .peek(payment -> payment.setIsPremium(payment.getIsPremium() - 1))
                .collect(Collectors.toList());
        paymentRepository.saveAll(updatedPayments);
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
    public String checkCancel(String accessToken,String merchantUid){
        String url="https://api.iamport.kr/payments/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", accessToken);
        String body= String.format("{\"merchant_uid\":\"%s\"}",merchantUid);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String ss= check(response.getBody());
        return ss;
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
    private String check(String responseBody) {
        try {
            JsonNode jsonNode = jacksonObjectMapper.readTree(responseBody);
            JsonNode codeNode = jsonNode.get("code");

            if (codeNode.asInt() == 0) {
                return "성공";
            }
            else {
                return "실패";
            }
        } catch (Exception e) {
            e.printStackTrace(); // 또는 적절한 예외 처리
        }
        return null;
    }

}
