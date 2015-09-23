package org.apache.mesos.selenium.model;

/**
 * Created by waseemh on 15/09/2015.
 */
public class SeleniumHub extends SeleniumGridResource {

    private int newSessionWaitTimeout;

    private int jettyMaxThreads;

    private int nodePolling;

    private int cleanUpCycle;

    private int timeout;

    private int browserTimeout;

    private int maxSession;

    private int unregisterIfStillDownAfter;

    public int getNewSessionWaitTimeout() {
        return newSessionWaitTimeout;
    }

    public void setNewSessionWaitTimeout(int newSessionWaitTimeout) {
        this.newSessionWaitTimeout = newSessionWaitTimeout;
    }

    public int getJettyMaxThreads() {
        return jettyMaxThreads;
    }

    public void setJettyMaxThreads(int jettyMaxThreads) {
        this.jettyMaxThreads = jettyMaxThreads;
    }

    public int getNodePolling() {
        return nodePolling;
    }

    public void setNodePolling(int nodePolling) {
        this.nodePolling = nodePolling;
    }

    public int getCleanUpCycle() {
        return cleanUpCycle;
    }

    public void setCleanUpCycle(int cleanUpCycle) {
        this.cleanUpCycle = cleanUpCycle;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getBrowserTimeout() {
        return browserTimeout;
    }

    public void setBrowserTimeout(int browserTimeout) {
        this.browserTimeout = browserTimeout;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public int getUnregisterIfStillDownAfter() {
        return unregisterIfStillDownAfter;
    }

    public void setUnregisterIfStillDownAfter(int unregisterIfStillDownAfter) {
        this.unregisterIfStillDownAfter = unregisterIfStillDownAfter;
    }
}
