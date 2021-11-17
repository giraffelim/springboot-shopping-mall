package com.giraffelim.repository;

import com.giraffelim.dto.ItemSearchDto;
import com.giraffelim.dto.MainItemDto;
import com.giraffelim.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
