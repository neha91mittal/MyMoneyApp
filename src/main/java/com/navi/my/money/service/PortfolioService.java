package com.navi.my.money.service;

import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.requestPOJO.InitialAllocationRequestPOJO;
import com.navi.my.money.requestPOJO.MarketChangeRequestPojo;
import com.navi.my.money.requestPOJO.SIPRequestPojo;
import com.navi.my.money.responsePOJO.BalanceResponsePojo;
import com.navi.my.money.responsePOJO.RebalanceResponsePojo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PortfolioService {

    /**
     * API to set Initial Allocation.
     *
     * @param allocationList allocation list for various assets.
     * @throws MyMoneyServiceException
     */
     void setInitialAllocation(final List<InitialAllocationRequestPOJO> allocationList) throws MyMoneyServiceException;

    /**
     * API to add SIPs for various assets
     *
     * @param sipList SIP amount allocated to different Assets.
     * @throws MyMoneyServiceException
     */
     void addSip(final List<SIPRequestPojo> sipList) throws MyMoneyServiceException;

    /**
     * API to set Market rate change.
     *
     * @param changeList marketChange for different assets.
     * @throws MyMoneyServiceException
     */
     void updateMarketChange(final List<MarketChangeRequestPojo> changeList) throws MyMoneyServiceException;

    /**
     * API for rebalancing portfolio.
     *
     * @param date Date from which portfolio has to be rebalanced.
     * @throws MyMoneyServiceException
     */
     void reBalance(Optional<Date> date) throws MyMoneyServiceException;

    /**
     * API for returning last rebalanced amount.
     * @return last rebalanced amount.
     */
    List<RebalanceResponsePojo> getLastRebalancedAmount();

    /**
     * API to return balance for particular month.
     *
     * @param time Time for which balance is required to be returned.
     * @return List of balances for differe assets
     * @throws MyMoneyServiceException
     */
    List<BalanceResponsePojo> getBalanceAmount(final Date time) throws MyMoneyServiceException;
}

