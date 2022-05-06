package com.navi.my.money.manager;

import com.navi.my.money.exception.InValidOperationException;
import com.navi.my.money.model.AssetType;

import java.util.Date;
import java.util.List;

public interface AssetManager {

    /**
     * Method to allocate initial amount to Assets.
     *
     * @param type Type of asset
     * @param initialAmount Initial amount of asset.
     * @param date Date at which allocation is made.
     * @throws InValidOperationException
     */
    void allocateInitialAmount(final AssetType type,
                               final Integer initialAmount,
                               final Date date) throws InValidOperationException;

    /**
     * Method to allocate initial initial percentage to Assets.
     *
     * @throws InValidOperationException
     */
    void allocateInitialPercentage() throws InValidOperationException;

    /**
     * Method to add SIP for given asset.
     * @param type
     * @param sipAmount
     * @param date
     * @throws InValidOperationException
     */
    void addSip(final AssetType type, final Integer sipAmount, final Date date) throws InValidOperationException;

    /**
     * Method to apply monthly rate change on portfolio.
     * @param assetType
     * @param marketChange
     * @param timestamp
     * @throws InValidOperationException
     */
    void updateMarketChange(final AssetType assetType, final Float marketChange,final Date timestamp) throws InValidOperationException;

    /**
     * Method to rebalance portfolio.
     * @param date
     * @throws InValidOperationException
     */
    void reBalance(final Date date) throws InValidOperationException;

    /**
     * Method to return last rebalanced amount.
     * @param type
     * @return
     */
    Integer getLastRebalancedAmount(final AssetType type);

    /**
     * Method to return AssetList.
     * @return
     */
    List<AssetType> getAssetList();

    /**
     * Method to return closing amount for given date.
     * @param assetType
     * @param time
     * @return
     * @throws InValidOperationException
     */
    Integer getBalanceAmount(final AssetType assetType, final Date time) throws InValidOperationException;
}
