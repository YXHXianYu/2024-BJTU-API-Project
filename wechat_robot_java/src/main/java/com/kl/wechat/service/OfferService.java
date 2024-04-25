package com.kl.wechat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kl.wechat.entity.Offer;

import java.util.List;
import java.util.Map;

public interface OfferService {
    void createOffer(Map<String, Object> offerData, Long fromUser);
    void updateOffer(Map<String, Object> offerData, Long fromUser);
    void deleteOffer(Map<String, Object> offerData, Long fromUser);
    IPage<Offer> listOffersWithPage(Map<String, Object> queryParams);
    void createOffers(List<Map<String, Object>> offerDataList, Long fromUser);
}