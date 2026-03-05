package com.anay.jumbotail.shipping.repository;

import com.anay.jumbotail.shipping.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findBySellerId(String sellerId);
    Optional<Seller> findBySellerIdAndActiveTrue(String sellerId);
}
