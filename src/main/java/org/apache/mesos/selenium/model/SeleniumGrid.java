package org.apache.mesos.selenium.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waseemh on 07/09/2015.
 */
public class SeleniumGrid {

    private SeleniumHub hub;

    private List<SeleniumNode> nodes;

    public SeleniumHub getHub() {
        return hub;
    }

    public void setHub(SeleniumHub hub) {
        this.hub = hub;
    }

    public List<SeleniumNode> getNodes() {
        return nodes;
    }

    public List<SeleniumNode> getNodesAsResources() {

        final List<SeleniumNode> gridNodes = new ArrayList<SeleniumNode>();
        //adding nodes according to requested instances.
        //for each instance of same node, we add it multiple times as a grid resource.
        for(SeleniumNode node : nodes) {
            for(int i=0; i < node.getInstances(); i++) {
                gridNodes.add(node);
            }
        }
        return gridNodes;
    }

    public void setNodes(List<SeleniumNode> nodes) {
        this.nodes = nodes;
    }
}