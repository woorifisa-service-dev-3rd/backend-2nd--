package dev.lawlesszone.domain.payment.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller()
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final IamportClient iamportClient;

    @PostMapping("/verify/{id}")
    public String payment(@PathVariable Long id, Model model) throws IamportResponseException, IOException {
        log.info(String.valueOf(iamportClient.paymentByImpUid(String.valueOf(id))));
        return "payment";
    }
}
