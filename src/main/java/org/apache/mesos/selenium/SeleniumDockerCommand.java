package org.apache.mesos.selenium;

import org.apache.mesos.Protos;
import org.apache.mesos.selenium.model.SeleniumGridResource;
import org.apache.mesos.selenium.model.SeleniumHub;
import org.apache.mesos.selenium.model.SeleniumNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waseemh on 14/09/2015.
 */
public class SeleniumDockerCommand {

    private final static String SELENIUM_HUB_IMAGE = "selenium/hub:2.47.1";
    private final static String SELENIUM_NODE_FIREFOX_IMAGE = "selenium/node-firefox:2.47.1";
    private final static String SELENIUM_NODE_CHROME_IMAGE = "selenium/node-chrome:2.47.1";

    public static String getImageName(SeleniumGridResource gridResource) {
        if(gridResource instanceof  SeleniumNode) {
            if(((SeleniumNode) gridResource).getBrowser().equals("firefox")) {
                return SELENIUM_NODE_FIREFOX_IMAGE;
            }
            else if (((SeleniumNode) gridResource).getBrowser().equals("chrome")) {
                return SELENIUM_NODE_CHROME_IMAGE;
            }
            else return null;
        }
        else if(gridResource instanceof  SeleniumHub) {
            return SELENIUM_HUB_IMAGE;
        }
        else return null;
    }

    public static List<Protos.Parameter> getCommandParameters(SeleniumGridResource gridResource) {
        List<Protos.Parameter> parameters = new ArrayList<Protos.Parameter>();
        if(gridResource instanceof SeleniumNode) {
            //parameters.add(org.apache.mesos.Protos.Parameter.newBuilder().setKey("link").setValue("selenium-hub:hub").build());
        }
        if(gridResource instanceof SeleniumHub) {
            parameters.add(org.apache.mesos.Protos.Parameter.newBuilder().setKey("publish").setValue("4444:4444").build());
            parameters.add(org.apache.mesos.Protos.Parameter.newBuilder().setKey("name").setValue("selenium-hub").build());
        }
        return parameters;
    }

}
