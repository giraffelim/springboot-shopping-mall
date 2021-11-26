package com.giraffelim.repository;

import com.giraffelim.dto.CartDetailDto;

import java.util.List;

public interface CartItemRepositoryCustom {

    List<CartDetailDto> getCartList(Long cartId);

}
