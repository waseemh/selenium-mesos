package org.apache.mesos.selenium.model;

/**
 * Created by waseemh on 14/09/2015.
 */
public class SeleniumNode extends SeleniumGridResource {

    private String browser;

    private int instances;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof SeleniumNode)) {
            return false;
        }
        else {
           return ((SeleniumNode) object).getBrowser().equals(this.getBrowser());
        }
    }
}