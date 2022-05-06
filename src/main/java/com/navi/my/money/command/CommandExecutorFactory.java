package com.navi.my.money.command;

import com.navi.my.money.exception.InvalidCommandException;
import com.navi.my.money.model.Command;
import com.navi.my.money.service.MyMoneyService;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to get correct {@link CommandExecutor} from a given command.
 */
public class CommandExecutorFactory {
    private Map<String, CommandExecutor> commands = new HashMap<>();
    public CommandExecutorFactory(final MyMoneyService myMoneyService) {
        commands.put(
                AllocateCommandExecutor.COMMAND_NAME,
                new AllocateCommandExecutor(myMoneyService));
        commands.put(
                SipCommandExecutor.COMMAND_NAME,
                new SipCommandExecutor(myMoneyService));
        commands.put(
                ChangeCommandExecutor.COMMAND_NAME,
                new ChangeCommandExecutor(myMoneyService));
        commands.put(
                RebalanceCommandExecutor.COMMAND_NAME,
                new RebalanceCommandExecutor(myMoneyService));
        commands.put(
                BalanceCommandExecutor.COMMAND_NAME,
                new BalanceCommandExecutor(myMoneyService));
    }

    /**
     * Gets {@link CommandExecutor} for a particular command. It basically uses name of command to
     * fetch its corresponding executor.
     *
     * @param command Command for which executor has to be fetched.
     * @return Command executor.
     */
    public CommandExecutor getCommandExecutor(final Command command) {
        final CommandExecutor commandExecutor = commands.get(command.getCommandName());
        if (commandExecutor == null) {
            throw new InvalidCommandException();
        }
        return commandExecutor;
    }
}
