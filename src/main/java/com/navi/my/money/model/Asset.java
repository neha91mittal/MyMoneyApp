package com.navi.my.money.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Asset {
    public final AssetType type;
    public Integer initialAllocation;
    public Integer percentageAllocation;
    public Map<Date, Integer> openingAmount;
    public Map<Date, Integer> postSIPAmount;
    public Map<Date, Integer> postMarketChangeAmount;
    public Map<Date, Integer> closingAmount;
    public Integer lastBalancedAmount;
    protected Asset(AssetType type) {
        this.type = type;
        initialAllocation=null;
        percentageAllocation=null;
        lastBalancedAmount = null;
        openingAmount=new HashMap<>();
        postSIPAmount=new HashMap<>();
        postMarketChangeAmount=new HashMap<>();
        closingAmount=new HashMap<>();
    }
}
