package com.giraffelim.repository;

import com.giraffelim.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
