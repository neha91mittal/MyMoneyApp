package com.navi.my.money.responsePOJO;

import com.navi.my.money.model.AssetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RebalanceResponsePojo {
    AssetType type;
    Integer amount;
}
