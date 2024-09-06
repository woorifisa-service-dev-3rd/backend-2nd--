package dev.lawlesszone.domain.payment.repository;

import dev.lawlesszone.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
