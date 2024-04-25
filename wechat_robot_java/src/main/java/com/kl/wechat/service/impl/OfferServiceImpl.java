package com.kl.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kl.wechat.entity.Offer;
import com.kl.wechat.mapper.OfferMapper;
import com.kl.wechat.service.OfferService;
import com.kl.wechat.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

    @Override
    public void createOffer(Map<String, Object> offerData, Long fromUser) {
        Offer offer = new Offer();
        offer.setCompany((String) offerData.get("company"));
        offer.setCity((String) offerData.get("city"));
        offer.setPosition((String) offerData.get("position"));
        offer.setSalary((Integer) offerData.get("salary"));
        offer.setDatetime(new Date());
        offer.setFromUser(fromUser);
        save(offer);
    }

    @Override
    public void updateOffer(Map<String, Object> offerData, Long fromUser) {
        QueryWrapper<Offer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", offerData.get("company"))
                .eq("city", offerData.get("city"))
                .eq("position", offerData.get("position"))
                .eq("from_user", fromUser);

        Offer offer = new Offer();
        offer.setCompany((String) offerData.get("company"));
        offer.setCity((String) offerData.get("city"));
        offer.setPosition((String) offerData.get("position"));
        offer.setSalary((Integer) offerData.get("salary"));
        update(offer, queryWrapper);
    }

    @Override
    public void deleteOffer(Map<String, Object> offerData, Long fromUser) {
        QueryWrapper<Offer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", offerData.get("company"))
                .eq("city", offerData.get("city"))
                .eq("position", offerData.get("position"))
                .eq("from_user", fromUser);
        remove(queryWrapper);
    }

    @Override
    public IPage<Offer> listOffersWithPage(Map<String, Object> queryParams) {
        System.out.println("进入listOffersWithPage方法");
        QueryWrapper<Offer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(queryParams.get("company") != null, "company", queryParams.get("company"))
                .like(queryParams.get("city") != null, "city", queryParams.get("city"))
                .like(queryParams.get("position") != null, "position", queryParams.get("position"));

        Integer[] salaryRange = (Integer[]) queryParams.get("salary-range");
        if (salaryRange != null) {
            queryWrapper.between("salary", salaryRange[0], salaryRange[1]);
        }

        boolean sortNew = (boolean) queryParams.getOrDefault("sort-new", false);
        boolean sortSalary = (boolean) queryParams.getOrDefault("sort-salary", false);

        if (sortNew) {
            queryWrapper.orderByDesc("datetime");
        } else if (sortSalary) {
            queryWrapper.orderByDesc("salary");
        }

        int page = (int) queryParams.getOrDefault("page", 1);
        int size = Constants.RECORDS_PER_PAGE;
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public void createOffers(List<Map<String, Object>> offerDataList, Long fromUser) {
        for (Map<String, Object> offerData : offerDataList) {
            createOffer(offerData, fromUser);
        }
    }
}