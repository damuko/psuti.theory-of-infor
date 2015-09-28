package com.toi.gui;


import com.toi.generator.Configuration;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GeneratorService extends Service<String> {

    private Configuration configuration;
    private int sequenceSize;

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                if (configuration == null || sequenceSize == 0)
                    throw new ExceptionInInitializerError("Please set configuration and sequenceSize properties.");
                return new com.toi.generator.Generator(configuration).getRandomText(sequenceSize);
            }
        };
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSequenceSize(int sequenceSize) {
        this.sequenceSize = sequenceSize;
    }
}
