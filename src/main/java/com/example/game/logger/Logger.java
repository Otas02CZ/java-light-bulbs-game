package com.example.game.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private PrintWriter writer;
    private boolean enabled;

    private static Logger logger = null;

    private static final String FILE_NAME_DEFAULT = "game.log";

    private Logger(PrintWriter writer) {
        this.writer = writer;
        this.enabled = true;
    }

    public static Logger init(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(fileName, true));
        logger = new Logger(writer);
        return logger;
    }

    public static Logger init() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME_DEFAULT, true));
        logger = new Logger(writer);
        return logger;
    }

    public static Logger obtain() {
        return logger;
    }

    public synchronized void log(String text) {
        if (this.enabled) {
            this.writer.println(text);
            this.writer.flush();
        }
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
