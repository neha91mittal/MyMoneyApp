package com.navi.my.money.command;


import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.Command;
import com.navi.my.money.service.MyMoneyService;

public abstract class CommandExecutor {

    protected MyMoneyService myMoneyService;
    public CommandExecutor(final MyMoneyService myMoneyService) {
        this.myMoneyService = myMoneyService;
    }

    /**
     * Validates that whether a command is valid to be executed or not.
     *
     * @param command Command to be validated.
     * @return Boolean indicating whether command is valid or not.
     */
    public abstract boolean validate(final Command command);

    /**
     * Executes the command.
     *
     * @param command Command to be executed.
     */
    public abstract void execute(final Command command) throws MyMoneyServiceException;
}
