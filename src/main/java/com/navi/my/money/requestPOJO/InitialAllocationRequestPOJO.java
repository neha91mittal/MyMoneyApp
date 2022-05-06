package com.navi.my.money.requestPOJO;

import com.navi.my.money.model.AssetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class InitialAllocationRequestPOJO {
    AssetType type;
    Integer amount;
    Date date;
}
