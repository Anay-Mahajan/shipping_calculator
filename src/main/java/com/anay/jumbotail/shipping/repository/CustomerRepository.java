package com.anay.jumbotail.shipping.repository;

import com.anay.jumbotail.shipping.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(String customerId);
    Optional<Customer> findByCustomerIdAndActiveTrue(String customerId);
}
