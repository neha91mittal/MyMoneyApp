package com.navi.my.money.manager;

import com.navi.my.money.exception.InValidOperationException;
import com.navi.my.money.model.*;
import com.navi.my.money.util.DateTimeUtils;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

public class MyMoneyAssetManager implements AssetManager {

    @Getter
    public Map<AssetType, Asset> assetMap;

    public MyMoneyAssetManager() {
        assetMap=new HashMap<>();
        assetMap.put(AssetType.GOLD,new Gold());
        assetMap.put(AssetType.EQUITY,new Equity());
        assetMap.put(AssetType.DEBT, new Debt());
    }

    @Override
    public void allocateInitialAmount(@NonNull final AssetType type,
                                      @NonNull final Integer initialAmount,
                                      @NonNull final Date date)
            throws InValidOperationException {
        Asset asset=assetMap.get(type);
        if(asset.getInitialAllocation()==null) {
            asset.setInitialAllocation(initialAmount);
            asset.getOpeningAmount().put(date,initialAmount);
            asset.getPostSIPAmount().put(date,initialAmount);
        }
        else {
            throw new InValidOperationException("Allocation can only be made once");
        }

    }
    @Override
    public void allocateInitialPercentage()
            throws InValidOperationException {
        Iterator<Map.Entry<AssetType, Asset>> iterator = assetMap.entrySet().iterator();
        Integer totalInitialAmount = getTotalInitialAllocation();
        while (iterator.hasNext()) {
            Map.Entry<AssetType, Asset> entry = iterator.next();
            Asset asset = entry.getValue();
            if(asset.getInitialAllocation()==null) {
                throw new InValidOperationException();
            }
            Integer percentage = asset.getInitialAllocation()*100/totalInitialAmount;
            asset.setPercentageAllocation(percentage);
        }
    }

    private Integer getTotalInitialAllocation() throws InValidOperationException {
            Integer totalAmount = Integer.valueOf(0);
            Iterator<Map.Entry<AssetType, Asset>> iterator = assetMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<AssetType, Asset> entry = iterator.next();
                Asset asset = entry.getValue();
                if(asset.getInitialAllocation()==null) {
                    throw new InValidOperationException();
                }
                totalAmount+= asset.getInitialAllocation();
            }
            return totalAmount;

    }


    @Override
    public void addSip(@NonNull final AssetType type,
                       @NonNull final Integer sipAmount,
                       @NonNull final Date date)
            throws InValidOperationException {
        Asset asset=assetMap.get(type);
        if(!asset.getOpeningAmount().containsKey(date) || asset.getPostSIPAmount().containsKey(date)) {
            throw new InValidOperationException();
        }
        asset.getPostSIPAmount().put(date, asset.getOpeningAmount().get(date)+sipAmount);
    }

    @Override
    public void updateMarketChange(@NonNull final AssetType assetType,
                                   @NonNull final Float marketChange,
                                   @NonNull final Date timestamp) throws InValidOperationException {
        Asset asset=assetMap.get(assetType);
        Integer postSIPAmount=null;
        if(asset.getPostSIPAmount().containsKey(timestamp)) {
            postSIPAmount = asset.getPostSIPAmount().get(timestamp);
            Float postMarketChangeAmount = postSIPAmount + (postSIPAmount*marketChange)/100;
            asset.getPostMarketChangeAmount().put(timestamp,postMarketChangeAmount.intValue());
            updateClosingBalance(asset,timestamp,postMarketChangeAmount.intValue());
        }
        else {
            throw new InValidOperationException();
        }


    }

    @Override
    public void reBalance(@NonNull final Date date) throws InValidOperationException {
        Integer totalAmount = getTotalAmount(date);
        Iterator<Map.Entry<AssetType, Asset>> iterator = assetMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AssetType, Asset> entry = iterator.next();
            Asset asset = entry.getValue();
            Integer percentage = asset.getPercentageAllocation();
            Integer rebalancedAmount=null;
            if(percentage != null) {
                rebalancedAmount= totalAmount*percentage/100;
            }
            else {
                throw new InValidOperationException();
            }
            asset.getClosingAmount().put(date,rebalancedAmount);
            asset.setLastBalancedAmount(rebalancedAmount);
        }
    }

    @Override
    public Integer getLastRebalancedAmount(@NonNull final AssetType type) {
        return assetMap.get(type).lastBalancedAmount;
    }

    @Override
    public List<AssetType> getAssetList() {
        List<AssetType> assetList=new ArrayList<>();
        Iterator<Map.Entry<AssetType, Asset>> iterator = assetMap.entrySet().iterator();
        while (iterator.hasNext()) {
           assetList.add(iterator.next().getKey());
        }
        return assetList;
    }

    @Override
    public Integer getBalanceAmount(@NonNull final AssetType assetType, @NonNull final Date time) throws InValidOperationException {
        Asset asset=assetMap.get(assetType);
        if(asset.getClosingAmount().containsKey(time)) {
            return asset.getClosingAmount().get(time);
        }
        else
            throw new InValidOperationException();
    }

    private Integer getTotalAmount(@NonNull final Date date) throws InValidOperationException {
        Integer totalAmount = Integer.valueOf(0);
        Iterator<Map.Entry<AssetType, Asset>> iterator = assetMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AssetType, Asset> entry = iterator.next();
            Asset asset = entry.getValue();
            if(asset.getClosingAmount()!=null && asset.getClosingAmount().containsKey(date)) {
                totalAmount+= asset.getClosingAmount().get(date);
            } else {
                throw new InValidOperationException();
            }
        }
        return totalAmount;

    }

    private void updateClosingBalance(final Asset asset, final Date timestamp, final Integer amount) {
        asset.getClosingAmount().put(timestamp,amount);
        asset.getOpeningAmount().put(DateTimeUtils.getTimeWithNextMonth(timestamp),amount);
    }

}
