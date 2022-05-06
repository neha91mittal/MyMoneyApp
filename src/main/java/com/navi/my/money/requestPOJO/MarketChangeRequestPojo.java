package com.navi.my.money.requestPOJO;

import com.navi.my.money.model.AssetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class MarketChangeRequestPojo {
    AssetType assetType;
    Float marketChange;
    Date timestamp;
}
