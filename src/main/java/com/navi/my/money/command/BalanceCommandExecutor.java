package com.navi.my.money.command;

import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.AssetType;
import com.navi.my.money.model.Command;
import com.navi.my.money.responsePOJO.BalanceResponsePojo;
import com.navi.my.money.service.MyMoneyService;
import com.navi.my.money.util.DateTimeUtils;

import java.util.List;


public class BalanceCommandExecutor extends CommandExecutor {
        public static String COMMAND_NAME = "balance";

    public BalanceCommandExecutor(final MyMoneyService myMoneyService) {
            super(myMoneyService);
        }

        @Override
        public boolean validate(final Command command) {
            return (command.getParams().size() == 1);
        }

        @Override
        public void execute(final Command command) throws MyMoneyServiceException {
            List<BalanceResponsePojo> response = myMoneyService.getBalanceAmount(DateTimeUtils.getTime(command.getParams().get(0)));
            if(response.size()!=3) {
                throw new IllegalStateException();
            }
            for(int i = 0; i< AssetType.values().length; i++) {
                printResponse(response, AssetType.values()[i]);
            }
            System.out.println();
        }

    private void printResponse(List<BalanceResponsePojo> response, AssetType type) {
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).getType().equals(type)) {
                System.out.print(response.get(i).getAmount().intValue() + " ");
                break;
            }
        }

    }

}
