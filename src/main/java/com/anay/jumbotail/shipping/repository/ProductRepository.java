package com.anay.jumbotail.shipping.repository;

import com.anay.jumbotail.shipping.model.Product;
import com.anay.jumbotail.shipping.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);
    Optional<Product> findByProductIdAndActiveTrue(String productId);
    List<Product> findBySellerAndActiveTrue(Seller seller);
}
