package com.navi.my.money.manager;

import com.navi.my.money.exception.InValidOperationException;
import com.navi.my.money.model.Asset;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.util.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

public class MyMoneyAssetManagerTest{

    public final Integer DEBT_ALLOCATION= 50;

    public final Integer GOLD_ALLOCATION= 100;
    public final Integer EQUITY_ALLOCATION= 50;

    @Test
    public void allocateInitialAmountHappyCaseTest() throws InValidOperationException {
       MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
       Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);

        Assert.assertEquals(map.get(AssetType.DEBT).initialAllocation, DEBT_ALLOCATION);
    }

    @Test(expected = InValidOperationException.class)
    public void allocateInitialAmountTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
    }

    @Test(expected = InValidOperationException.class)
    public void allocateInitialPercentageTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
    }

    @Test
    public void allocateInitialPercentageHappyCaseTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        Assert.assertEquals(map.get(AssetType.GOLD).getPercentageAllocation().intValue(),50);
    }

    @Test
    public void updateMarketChangeTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        myMoneyAssetManager.updateMarketChange(AssetType.DEBT, 100F, date);

        Assert.assertEquals(map.get(AssetType.DEBT).getClosingAmount().get(date).intValue(), 100);
    }

    @Test(expected = InValidOperationException.class)
    public void addSIPTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        myMoneyAssetManager.updateMarketChange(AssetType.DEBT, 100F, date);

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyAssetManager.addSip(AssetType.EQUITY,100,dateFeb);
    }

    @Test
    public void addSIPHappyCaseTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Map<AssetType, Asset> map = myMoneyAssetManager.getAssetMap();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        myMoneyAssetManager.updateMarketChange(AssetType.DEBT, 100F, date);

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyAssetManager.addSip(AssetType.DEBT,100,dateFeb);
        Assert.assertEquals(map.get(AssetType.DEBT).getPostSIPAmount().get(dateFeb).intValue(), 200);
    }

    @Test
    public void getLastRebalancedAmountTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        myMoneyAssetManager.updateMarketChange(AssetType.DEBT, 100F, date);

        Date dateFeb =DateTimeUtils.getTime("FEBRUARY");
        myMoneyAssetManager.addSip(AssetType.DEBT,100,dateFeb);
        Assert.assertNull(myMoneyAssetManager.getLastRebalancedAmount(AssetType.DEBT));
    }


    @Test(expected = InValidOperationException.class)
    public void reBalanceTest() throws InValidOperationException {
        MyMoneyAssetManager myMoneyAssetManager = new MyMoneyAssetManager();
        Date date =DateTimeUtils.getTime("January");

        myMoneyAssetManager.allocateInitialAmount(AssetType.DEBT, DEBT_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.GOLD, GOLD_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialAmount(AssetType.EQUITY, EQUITY_ALLOCATION,date);
        myMoneyAssetManager.allocateInitialPercentage();
        myMoneyAssetManager.updateMarketChange(AssetType.DEBT, 100F, date);
        myMoneyAssetManager.reBalance(date);
    }

}
