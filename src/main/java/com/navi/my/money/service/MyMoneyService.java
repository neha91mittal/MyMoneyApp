package com.navi.my.money.service;

import com.navi.my.money.exception.InValidOperationException;
import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.manager.AssetManager;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.requestPOJO.InitialAllocationRequestPOJO;
import com.navi.my.money.requestPOJO.MarketChangeRequestPojo;
import com.navi.my.money.requestPOJO.SIPRequestPojo;
import com.navi.my.money.responsePOJO.BalanceResponsePojo;
import com.navi.my.money.responsePOJO.RebalanceResponsePojo;
import lombok.NonNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MyMoneyService implements PortfolioService{

    private AssetManager manager;

    public MyMoneyService(AssetManager manager) {
        this.manager=manager;
    }

    @Override
    public void setInitialAllocation(@NonNull final List<InitialAllocationRequestPOJO> allocationList)
            throws MyMoneyServiceException {
        for (InitialAllocationRequestPOJO initialAllocationRequestPOJO : allocationList) {
            if (initialAllocationRequestPOJO.getAmount() == null ||
                    initialAllocationRequestPOJO.getType() == null ||
                    initialAllocationRequestPOJO.getDate() == null) {
                throw new InvalidParameterException("AssetType and Amount can not be null");
            }
            try {
                manager.allocateInitialAmount(initialAllocationRequestPOJO.getType(),
                        initialAllocationRequestPOJO.getAmount(),
                        initialAllocationRequestPOJO.getDate());
            } catch (InValidOperationException e) {
                throw new MyMoneyServiceException(e);
            }
        }

        try {
            manager.allocateInitialPercentage();
        } catch (InValidOperationException e) {
            throw new MyMoneyServiceException(e);
        }

    }

    @Override
    public void addSip(@NonNull final List<SIPRequestPojo> sipList) throws MyMoneyServiceException {
        for (SIPRequestPojo sipRequestPojo : sipList) {
            try {
                manager.addSip(sipRequestPojo.getAssetType(), sipRequestPojo.getSipAmount(), sipRequestPojo.getTimestamp());
            } catch (InValidOperationException e) {
                throw new MyMoneyServiceException(e);
            }
        }
    }

   @Override
    public void updateMarketChange(@NonNull final List<MarketChangeRequestPojo> changeList)
            throws MyMoneyServiceException {
       for (MarketChangeRequestPojo marketChangeRequestPojo : changeList) {
           try {
               manager.updateMarketChange(marketChangeRequestPojo.getAssetType(),
                       marketChangeRequestPojo.getMarketChange(),
                       marketChangeRequestPojo.getTimestamp());
           } catch (InValidOperationException e) {
               throw new MyMoneyServiceException(e);
           }
       }
    }

    @Override
    public void reBalance(Optional<Date> date) throws MyMoneyServiceException {
        if(date.isPresent()) {
            try {
                manager.reBalance(date.get());
            } catch (InValidOperationException e) {
                throw new MyMoneyServiceException(e);
            }
        }

    }

    @Override
    public List<RebalanceResponsePojo> getLastRebalancedAmount() {
        List<AssetType> assetList = manager.getAssetList();
        List<RebalanceResponsePojo> rebalanceList = new ArrayList<>();
        for (AssetType assetType : assetList) {
            RebalanceResponsePojo rebalanceResponsePojo = new RebalanceResponsePojo(assetType,
                    manager.getLastRebalancedAmount(assetType));
            rebalanceList.add(rebalanceResponsePojo);
        }
        return rebalanceList;
    }

    @Override
    public List<BalanceResponsePojo> getBalanceAmount(@NonNull final Date time) throws MyMoneyServiceException {
        List<AssetType> assetList = manager.getAssetList();
        List<BalanceResponsePojo> balanceList = new ArrayList<>();
        for (AssetType assetType : assetList) {
            BalanceResponsePojo rebalanceResponsePojo = null;
            try {
                rebalanceResponsePojo = new BalanceResponsePojo(assetType,
                        manager.getBalanceAmount(assetType, time));
            } catch (InValidOperationException e) {
                throw new MyMoneyServiceException(e);
            }
            balanceList.add(rebalanceResponsePojo);
        }
        return balanceList;
    }


}
