package dev.lawlesszone.domain.payment.repository;

import dev.lawlesszone.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByMemberEmail(String email);
}
