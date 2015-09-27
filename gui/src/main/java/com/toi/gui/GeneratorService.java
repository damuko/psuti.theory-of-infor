package com.toi.gui;


import com.toi.generator.Configuration;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GeneratorService extends Service<String> {

    private Configuration cfg;
    private int sequenceSize;

    public GeneratorService(Configuration cfg, int sequenceSize) {
        this.cfg = cfg;
        this.sequenceSize = sequenceSize;
    }


    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                return new com.toi.generator.Generator(cfg).getRandomText(sequenceSize);
            }
        };
    }
}
