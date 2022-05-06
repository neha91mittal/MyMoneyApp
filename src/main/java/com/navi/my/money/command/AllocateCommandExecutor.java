package com.navi.my.money.command;

import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.model.Command;
import com.navi.my.money.requestPOJO.InitialAllocationRequestPOJO;
import com.navi.my.money.service.MyMoneyService;
import com.navi.my.money.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllocateCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "allocate";

    public AllocateCommandExecutor(final MyMoneyService myMoneyService) {
            super(myMoneyService);
    }

    @Override
    public boolean validate(final Command command) {
        return (command.getParams().size()==4);

    }


    @Override
    public void execute(final Command command) throws MyMoneyServiceException {
        Date date = DateTimeUtils.getTime(command.getParams().get(command.getParams().size()-1));
        List<InitialAllocationRequestPOJO> allocationList = new ArrayList<>();
        for(int i=0;i<command.getParams().size()-1;i++) {
            allocationList.add(i,new InitialAllocationRequestPOJO(AssetType.values()[i],
                    Integer.valueOf(command.getParams().get(i)),date));
        }
        myMoneyService.setInitialAllocation(allocationList);
     }
}
