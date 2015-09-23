package org.apache.mesos.selenium.model;

/**
 * Created by waseemh on 15/09/2015.
 */
public abstract class SeleniumGridResource {

    private int cpus;

    private int mem;

    private int disk;

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public int getMem() {
        return mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }

    public int getDisk() {
        return disk;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }
}