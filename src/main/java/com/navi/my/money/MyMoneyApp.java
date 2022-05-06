package com.navi.my.money;

import com.navi.my.money.command.CommandExecutorFactory;
import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.manager.MyMoneyAssetManager;
import com.navi.my.money.mode.FileMode;
import com.navi.my.money.mode.Mode;
import com.navi.my.money.service.MyMoneyService;

import java.io.File;
import java.io.IOException;

/**
 * App for running MyMoney.
 */
public class MyMoneyApp {

    public static void main(String[] args) throws IOException, MyMoneyServiceException {

        if(args.length != 1) {
            System.out.println("Missing Arguments");
            return;
        }

        File filePath = new File(args[0]);
        if(!filePath.exists()) {
            System.out.println("File Does not exist");
            return;
        }

        MyMoneyService service = new MyMoneyService(new MyMoneyAssetManager());
        CommandExecutorFactory factory = new CommandExecutorFactory(service);
        Mode testMode = new FileMode(factory, args[0]);
        testMode.process();
    }
}
