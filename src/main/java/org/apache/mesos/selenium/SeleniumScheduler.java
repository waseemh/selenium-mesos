package org.apache.mesos.selenium;

import org.apache.mesos.Protos;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.apache.mesos.selenium.model.SeleniumGridResource;
import org.apache.mesos.selenium.model.SeleniumHub;
import org.apache.mesos.selenium.model.SeleniumNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by waseemh on 15/06/2015.
 */
public class SeleniumScheduler implements Scheduler {

    private final FrameworkConfiguration config;

    /** List of pending instances. */
    private final Queue<SeleniumNode> pendingNodes;

    private Map<Protos.TaskID,SeleniumGridResource> runningTasks;

    private final SeleniumHub hub;

    private boolean isHubInit;

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumScheduler.class);

    private static final AtomicInteger TASK_ID_GENERATOR = new AtomicInteger(); // used to generate task numbers

    public SeleniumScheduler(FrameworkConfiguration frameworkConfiguration) {
        this.config = frameworkConfiguration;
        this.pendingNodes = new LinkedList<SeleniumNode>(config.getGrid().getNodesAsResources());
        this.runningTasks = new HashMap<Protos.TaskID,SeleniumGridResource>();
        this.hub = config.getGrid().getHub();
        this.isHubInit = false;
    }

    @Override
    public void registered(SchedulerDriver schedulerDriver, Protos.FrameworkID frameworkID, Protos.MasterInfo masterInfo) {
        LOGGER.info("Framework registered! ID: " + frameworkID.getValue());
        config.getState().setFrameworkId(frameworkID);
    }

    @Override
    public void reregistered(SchedulerDriver schedulerDriver, Protos.MasterInfo masterInfo) {
        config.getState().removeFrameworkId();
        System.out.println("Re-registered");
    }

    @Override
    public void resourceOffers(SchedulerDriver schedulerDriver, List<Protos.Offer> offers) {

        boolean matched = false;

        for (Protos.Offer offer : offers) {
            if(!isHubInit) {
                if (matches(offer,hub)) {
                    matched = true;
                    Protos.TaskID taskID = launchTask(schedulerDriver,offer,hub);
                    runningTasks.put(taskID, hub);
                    isHubInit = true;
                }
            }
            else {
                for (SeleniumNode node : pendingNodes) {
                    if (matches(offer, node)) {
                        matched = true;
                        Protos.TaskID taskID = launchTask(schedulerDriver, offer, node);
                        runningTasks.put(taskID, node);
                        pendingNodes.remove(node);
                        break;
                    }
                }
            }

            if (!matched) {
                schedulerDriver.declineOffer(offer.getId());
            }
        }

    }

    private Protos.TaskID launchTask(SchedulerDriver schedulerDriver, Protos.Offer offer, SeleniumGridResource gridResource) {

        Protos.TaskID taskId = Protos.TaskID.newBuilder().setValue(FrameworkConfiguration.getFrameworkName()+"-"+TASK_ID_GENERATOR.getAndIncrement()).build();

        // docker image info
        Protos.ContainerInfo.DockerInfo.Builder dockerInfoBuilder = Protos.ContainerInfo.DockerInfo.newBuilder();
        dockerInfoBuilder.setNetwork(Protos.ContainerInfo.DockerInfo.Network.BRIDGE);
        dockerInfoBuilder.setImage(SeleniumDockerCommand.getImageName(gridResource));
        dockerInfoBuilder.addAllParameters(SeleniumDockerCommand.getCommandParameters(gridResource));

        // container info
        Protos.ContainerInfo.Builder containerInfoBuilder = Protos.ContainerInfo.newBuilder();
        containerInfoBuilder.setType(Protos.ContainerInfo.Type.DOCKER);
        containerInfoBuilder.setDocker(dockerInfoBuilder.build());

        Protos.TaskInfo task = Protos.TaskInfo
                .newBuilder()
                .setName("task " + taskId.getValue())
                .setTaskId(taskId)
                .setSlaveId(offer.getSlaveId())
                .addResources(
                        Protos.Resource
                                .newBuilder()
                                .setName("cpus")
                                .setType(Protos.Value.Type.SCALAR)
                                .setScalar(
                                        Protos.Value.Scalar.newBuilder()
                                                .setValue(gridResource.getCpus()).
                                                build()).build())
                .addResources(
                        Protos.Resource
                                .newBuilder()
                                .setName("mem")
                                .setType(Protos.Value.Type.SCALAR)
                                .setScalar(
                                        Protos.Value.Scalar
                                                .newBuilder()
                                                .setValue(gridResource.getMem())
                                                .build()).build())
                .setContainer(containerInfoBuilder)
                .setCommand(Protos.CommandInfo.newBuilder().setShell(false)).build();

        schedulerDriver.launchTasks(Arrays.asList(offer.getId()), Arrays.asList(task));

        return taskId;
    }

    private static boolean matches(Protos.Offer offer, SeleniumGridResource gridResource) {

        double cpus = -1;
        double mem = -1;

        for (Protos.Resource resource : offer.getResourcesList()) {
            if (resource.getName().equals("cpus")) {
                if (resource.getType().equals(Protos.Value.Type.SCALAR)) {
                    cpus = resource.getScalar().getValue();
                } else {
                    LOGGER.error("Cpus resource was not a scalar: {0}", resource.getType().toString());
                }
            } else if (resource.getName().equals("mem")) {
                if (resource.getType().equals(Protos.Value.Type.SCALAR)) {
                    mem = resource.getScalar().getValue();
                } else {
                    LOGGER.error("Mem resource was not a scalar: {0}", resource.getType().toString());
                }
            } else if (resource.getName().equals("disk")) {
                LOGGER.warn("Ignoring disk resources from offer");
            } else {
                LOGGER.warn("Ignoring unknown resource type: {0}", resource.getName());
            }
        }

        if (cpus < 0) {
            LOGGER.error("No cpus resource present");
        }
        if (mem < 0) {
            LOGGER.error("No mem resource present");
        }

        // Check for sufficient cpu and memory resources in the offer.
        double requestedCpus = gridResource.getCpus();
        double requestedMem = gridResource.getMem();

        if (requestedCpus <= cpus && requestedMem <= mem) {
            return true;
        } else {
            LOGGER.info(
                    "Offer not sufficient for slave request:\n"
                            + offer.getResourcesList().toString()
                            + "  cpus: " + requestedCpus + "\n"
                            + "  mem:  " + requestedMem);
            return false;
        }
    }

    @Override
    public void offerRescinded(SchedulerDriver schedulerDriver, Protos.OfferID offerID) {
        System.out.println("This offer's been rescinded. Tough luck, cowboy.");
    }

    @Override
    public void statusUpdate(SchedulerDriver schedulerDriver, Protos.TaskStatus taskStatus) {

        Protos.TaskID taskId = taskStatus.getTaskId();
        LOGGER.info("Task {} is in state {}", taskId.getValue(), taskStatus.getState());

        switch (taskStatus.getState()) {
            case TASK_FAILED:
                SeleniumGridResource gridResource = runningTasks.get(taskId);
                if(gridResource instanceof SeleniumNode) {
                    pendingNodes.add((SeleniumNode) gridResource);
                }
                else if (gridResource instanceof SeleniumHub) {
                    isHubInit = false;
                }
                break;
            case TASK_FINISHED:
                LOGGER.info("Unregistering task {} due to state: {}", taskId.getValue(), taskStatus.getState());
                runningTasks.remove(taskId);
                break;
        }
    }

    @Override
    public void frameworkMessage(SchedulerDriver schedulerDriver, Protos.ExecutorID executorID, Protos.SlaveID slaveID, byte[] bytes) {
        System.out.println("Received message (scheduler): " + new String(bytes)
                + " from " + executorID.getValue());
    }

    @Override
    public void disconnected(SchedulerDriver schedulerDriver) {
        System.out.println("We got disconnected yo");
    }

    @Override
    public void slaveLost(SchedulerDriver schedulerDriver, Protos.SlaveID slaveID) {
        System.out.println("Lost slave: " + slaveID);
    }

    @Override
    public void executorLost(SchedulerDriver schedulerDriver, Protos.ExecutorID executorID, Protos.SlaveID slaveID, int i) {
        System.out.println("Lost executor on slave " + slaveID);
    }

    @Override
    public void error(SchedulerDriver schedulerDriver, String s) {
        System.out.println("We've got errors, man: " + s);
    }
}