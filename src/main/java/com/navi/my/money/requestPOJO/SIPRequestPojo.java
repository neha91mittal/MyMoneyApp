package com.navi.my.money.requestPOJO;

import com.navi.my.money.model.AssetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class SIPRequestPojo {
    AssetType assetType;
    Integer sipAmount;
    Date timestamp;
}
