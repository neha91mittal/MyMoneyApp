package com.navi.my.money.mode;



import com.navi.my.money.command.CommandExecutor;
import com.navi.my.money.command.CommandExecutorFactory;
import com.navi.my.money.exception.InvalidCommandException;
import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.Command;

import java.io.IOException;

/**
 * Interface for mode of the program in which it can be run.
 */
public abstract class Mode {
    private CommandExecutorFactory commandExecutorFactory;

    public Mode(final CommandExecutorFactory commandExecutorFactory) {
        this.commandExecutorFactory = commandExecutorFactory;
    }

    /**
     * Helper method to process a command. It basically uses {@link CommandExecutor} to run the given
     * command.
     *
     * @param command Command to be processed.
     */
    protected void processCommand(final Command command) throws MyMoneyServiceException {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        if (commandExecutor.validate(command)) {
            commandExecutor.execute(command);
        } else {
            throw new InvalidCommandException();
        }
    }

    /**
     * Abstract method to process the mode. Each mode will process in its own way.
     *
     * @throws IOException
     */
    public abstract void process() throws IOException, MyMoneyServiceException;
}
