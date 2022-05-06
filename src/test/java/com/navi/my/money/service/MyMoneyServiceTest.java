package com.navi.my.money.service;


import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.manager.MyMoneyAssetManager;
import com.navi.my.money.model.Asset;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.requestPOJO.InitialAllocationRequestPOJO;

import java.util.*;

import com.navi.my.money.requestPOJO.MarketChangeRequestPojo;
import com.navi.my.money.requestPOJO.SIPRequestPojo;
import com.navi.my.money.responsePOJO.BalanceResponsePojo;
import com.navi.my.money.responsePOJO.RebalanceResponsePojo;
import com.navi.my.money.util.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Cases for my Money Service.
 */
public class MyMoneyServiceTest {

    public final Integer DEBT_ALLOCATION= 50;
    public final Integer GOLD_ALLOCATION= 100;
    public final Integer EQUITY_ALLOCATION= 10;

    public final Integer DEBT_SIP= 50;
    public final Integer EQUITY_SIP= 50;
    public final Integer GOLD_SIP= 50;

    public final Float DEBT_CHANGE= Float.valueOf(1);
    public final Float EQUITY_CHANGE= Float.valueOf(100);
    public final Float GOLD_CHANGE= Float.valueOf(20);



    @Test
    public void setInitialAllocationTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        Date date =DateTimeUtils.getTime("January");
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        Map<AssetType, Asset> assetMap = assetManager.getAssetMap();
        Assert.assertEquals(assetMap.get(AssetType.DEBT).initialAllocation, DEBT_ALLOCATION);
        Assert.assertEquals(assetMap.get(AssetType.EQUITY).initialAllocation, EQUITY_ALLOCATION);
        Assert.assertEquals(assetMap.get(AssetType.GOLD).initialAllocation, GOLD_ALLOCATION);
        Assert.assertEquals(assetMap.get(AssetType.DEBT).getOpeningAmount().get(date), DEBT_ALLOCATION);
        Assert.assertEquals(assetMap.get(AssetType.GOLD).getPostSIPAmount().get(date), GOLD_ALLOCATION);
    }

    @Test(expected = MyMoneyServiceException.class)
    public void setInitialAllocationTwiceTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);
        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);

    }

    @Test
    public void addMarketChangeTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        Date date =DateTimeUtils.getTime("January");
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.updateMarketChange(getMarketChangeList("January"));

        Map<AssetType, Asset> assetMap = assetManager.getAssetMap();
        Integer actualClosingAmount=assetMap.get(AssetType.DEBT).getClosingAmount().get(date);
        Float expectedClosingAmount = (DEBT_ALLOCATION*DEBT_CHANGE/100) +DEBT_ALLOCATION;
        Integer expected = expectedClosingAmount.intValue();
        Assert.assertEquals(expected, actualClosingAmount);
    }

    @Test
    public void addSipTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.updateMarketChange(getMarketChangeList("January"));

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyService.addSip(getSIPRequestList());
        Map<AssetType, Asset> assetMap = assetManager.getAssetMap();
        Integer actualAmount=assetMap.get(AssetType.DEBT).getPostSIPAmount().get(dateFeb);
        Float janClosing = (DEBT_ALLOCATION*DEBT_CHANGE/100) +DEBT_ALLOCATION;
        Integer expectedAmount = janClosing.intValue()+DEBT_SIP;
        Assert.assertEquals(actualAmount,expectedAmount);
    }


    @Test
    public void getBalanceAmountTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        Date date =DateTimeUtils.getTime("January");
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.updateMarketChange(getMarketChangeList("January"));

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyService.addSip(getSIPRequestList());
        List<BalanceResponsePojo> response=myMoneyService.getBalanceAmount(date);
        Float janClosing = (DEBT_ALLOCATION*DEBT_CHANGE/100) +DEBT_ALLOCATION;
        Integer expectedAmount = janClosing.intValue();
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).getType().equals(AssetType.DEBT)) {
                Assert.assertEquals(response.get(i).getAmount(),expectedAmount);
                break;
            }
        }
    }

    @Test
    public void getLastReBalanceAmountTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        Date date =DateTimeUtils.getTime("January");
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.updateMarketChange(getMarketChangeList("January"));

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyService.addSip(getSIPRequestList());
        List<BalanceResponsePojo> response=myMoneyService.getBalanceAmount(date);
        List<RebalanceResponsePojo> rebalanceResponsePojos = myMoneyService.getLastRebalancedAmount();
        Assert.assertTrue((rebalanceResponsePojos.get(0).getAmount()==null));
    }

    @Test
    public void rebalanceTest() throws MyMoneyServiceException {
        MyMoneyAssetManager assetManager = new MyMoneyAssetManager();
        PortfolioService myMoneyService=new MyMoneyService(assetManager);

        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = getInitialAllocationList();
        Date date =DateTimeUtils.getTime("January");
        myMoneyService.setInitialAllocation(allocationRequestPOJOList);
        myMoneyService.updateMarketChange(getMarketChangeList("January"));

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyService.addSip(getSIPRequestList());
        myMoneyService.updateMarketChange(getMarketChangeList("FEBRUARY"));
        Map<AssetType, Asset> assetMap = assetManager.getAssetMap();
        int intialAmount = assetMap.get(AssetType.DEBT).getClosingAmount().get(dateFeb);
        myMoneyService.reBalance(Optional.of(dateFeb));
        int finalAmount = assetMap.get(AssetType.DEBT).getClosingAmount().get(dateFeb);
        Assert.assertNotEquals(intialAmount,finalAmount);

    }

    private List<MarketChangeRequestPojo> getMarketChangeList(String month) {
        List<MarketChangeRequestPojo> changeRequestPojos = new ArrayList<>();
        Date date =DateTimeUtils.getTime(month);
        MarketChangeRequestPojo pojo1 =    new MarketChangeRequestPojo(AssetType.GOLD, GOLD_CHANGE,date);
        MarketChangeRequestPojo pojo2 =    new MarketChangeRequestPojo(AssetType.EQUITY, EQUITY_CHANGE, date);
        MarketChangeRequestPojo pojo3 =    new MarketChangeRequestPojo(AssetType.DEBT, DEBT_CHANGE,date);
        changeRequestPojos.add(pojo1);
        changeRequestPojos.add(pojo2);
        changeRequestPojos.add(pojo3);
        return changeRequestPojos;
    }

    private List<SIPRequestPojo> getSIPRequestList() {
        List<SIPRequestPojo> sipRequestPojos = new ArrayList<>();
        Date date =DateTimeUtils.getTime("FEBRUARY");
        SIPRequestPojo pojo1 =    new SIPRequestPojo(AssetType.GOLD, GOLD_SIP,date);
        SIPRequestPojo pojo2 =    new SIPRequestPojo(AssetType.EQUITY, EQUITY_SIP, date);
        SIPRequestPojo pojo3 =    new SIPRequestPojo(AssetType.DEBT, DEBT_SIP,date);
        sipRequestPojos.add(pojo1);
        sipRequestPojos.add(pojo2);
        sipRequestPojos.add(pojo3);
        return sipRequestPojos;
    }
    private List<InitialAllocationRequestPOJO> getInitialAllocationList() {
        Date date =DateTimeUtils.getTime("January");
        List<InitialAllocationRequestPOJO> allocationRequestPOJOList = new ArrayList<>();
        InitialAllocationRequestPOJO pojo1 =    new InitialAllocationRequestPOJO(AssetType.GOLD, GOLD_ALLOCATION,date);
        InitialAllocationRequestPOJO pojo2 =    new InitialAllocationRequestPOJO(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        InitialAllocationRequestPOJO pojo3 =    new InitialAllocationRequestPOJO(AssetType.DEBT, DEBT_ALLOCATION,date);
        allocationRequestPOJOList.add(pojo1);
        allocationRequestPOJOList.add(pojo2);
        allocationRequestPOJOList.add(pojo3);
        return allocationRequestPOJOList;
    }
}
