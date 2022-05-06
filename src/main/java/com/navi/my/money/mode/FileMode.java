package com.navi.my.money.mode;

import com.navi.my.money.command.CommandExecutorFactory;
import com.navi.my.money.exception.MyMoneyServiceException;
import com.navi.my.money.model.Command;

import java.io.*;


/**
 * Mode running in which input commands are given from a file.
 */
public class FileMode extends Mode{
    private String fileName;
    private String sipString;

    public FileMode(
            final CommandExecutorFactory commandExecutorFactory,
            final String fileName) {
        super(commandExecutorFactory);
        this.fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process() throws IOException, MyMoneyServiceException {
        final File file = new File(fileName);
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Invalid File");
            return;
        }

        try {
            String input = reader.readLine();
            while (input != null) {
                Command command = new Command(input);
                if (command.getCommandName().toLowerCase().equals("allocate")) {
                    command = new Command(input + " " + "january");
                }
                if (command.getCommandName().equals("sip")) {
                    sipString = input;
                } else {
                    String month = null;
                    if (command.getCommandName().equals("change")) {
                        month = command.getParams().get(command.getParams().size() - 1).toLowerCase();
                        if (!month.equals("january")) {
                            Command sip = new Command(sipString + " " + month);
                            processCommand(sip);
                        }
                    }
                    processCommand(command);
                    if (command.getCommandName().equals("change")) {
                        if (month.equals("june") || month.equals("december")) {
                            Command rebalance = new Command("REBALANCE" + " " + month);
                            processCommand(rebalance);
                        }
                    }

                }
                input = reader.readLine();
            }
        } catch (Exception ex) {
            System.out.println("Error while processing : " + ex.getMessage());
            return;
        }
    }

}
