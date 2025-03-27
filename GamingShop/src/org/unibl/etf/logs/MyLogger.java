package org.unibl.etf.logs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class MyLogger {
    private Logger logger;
    private FileHandler fileHandler;
    private ConsoleHandler consoleHandler;
    private String logFile;

    public MyLogger(String className, String logFileName) {
        // Initialize the logger with the class name
        logger = Logger.getLogger(className);

        setup( logFileName);
    }

    private void setup(String logFileName) {
        try {
            // Set the logger level to ALL
            logger.setLevel(Level.ALL);

            // Create a file handler that writes log messages to a file
            Path logsPath = Paths.get("src/org/unibl/etf/logs/" + logFileName);
            if (!Files.exists(logsPath)) {
                Files.createDirectories(logsPath);
            }

            fileHandler = new FileHandler(logsPath.resolve("app.log").toString(), true);
            fileHandler.setLevel(Level.ALL);

            // Create a console handler that writes log messages to the console
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);

            // Define a simple formatter for the file handler
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Add handlers to the logger
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);

            // Prevent the logger from using the default console handler
            logger.setUseParentHandlers(false);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while setting up logger", e);
        }
    }

    private void createLogFolder(String folderName){
        Path logsPath = Paths.get("src/org/unibl/etf/logs/" + folderName);
        if (!Files.exists(logsPath)) {
            try {
                Files.createDirectories(logsPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public void logSevere(String message) {
        logger.log(Level.SEVERE, message);
    }

    public void logDebug(String message) {
        logger.log(Level.FINE, message);
    }
}
