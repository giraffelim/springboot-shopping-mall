package com.giraffelim.service;

import com.giraffelim.entity.ItemImg;
import com.giraffelim.repository.ItemImgRepository;
import com.giraffelim.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = FileUtils.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;

            // 상품 이미지 정보 저장
            itemImg.updateItemImg(oriImgName, imgName, imgUrl);
            itemImgRepository.save(itemImg);
        }

    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg findItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            if (!StringUtils.isEmpty(findItemImg.getImgName())) {
                FileUtils.deleteFile(itemImgLocation + findItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = FileUtils.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            findItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
