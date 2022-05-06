package com.navi.my.money.command;

import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.model.Command;
import com.navi.my.money.requestPOJO.MarketChangeRequestPojo;
import com.navi.my.money.service.MyMoneyService;
import com.navi.my.money.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeCommandExecutor extends CommandExecutor {
    public static String COMMAND_NAME = "change";

    public ChangeCommandExecutor(final MyMoneyService myMoneyService) {
        super(myMoneyService);
    }

    @Override
    public boolean validate(final Command command) {
        return (command.getParams().size() == 4);
    }


    @Override
    public void execute(final Command command) throws MyMoneyServiceException {
        Date date = DateTimeUtils.getTime(command.getParams().get(command.getParams().size()-1));
        List<MarketChangeRequestPojo> changeList = new ArrayList<>();
        for(int i=0;i<command.getParams().size()-1;i++) {
            changeList.add(i,new MarketChangeRequestPojo(AssetType.values()[i],
                    Float.valueOf(command.getParams().get(i).split("%")[0])
                    ,date));
        }
        myMoneyService.updateMarketChange(changeList);

    }

}

