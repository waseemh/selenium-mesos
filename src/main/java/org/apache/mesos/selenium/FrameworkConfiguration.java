package org.apache.mesos.selenium;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.mesos.selenium.model.SeleniumGrid;

/**
 * Created by waseemh on 14/09/2015.
 */
public class FrameworkConfiguration {

    private SeleniumGrid grid;

    private String zooKeeper;

    private static String frameworkName = "selenium-grid";

    @JsonIgnore
    private State state;

    public static String getFrameworkName() {
        return frameworkName;
    }

    public static void setFrameworkName(String frameworkName) {
        FrameworkConfiguration.frameworkName = frameworkName;
    }

    public SeleniumGrid getGrid() {
        return grid;
    }

    public void setGrid(SeleniumGrid grid) {
        this.grid = grid;
    }

    public String getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(String zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}