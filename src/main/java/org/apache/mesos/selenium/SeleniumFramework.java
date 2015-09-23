package org.apache.mesos.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos;
import org.apache.mesos.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * CLI Params
 *
 * Created by waseemh on 08/09/2015.
 */
public class SeleniumFramework {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumFramework.class);

    public static void main(String[] args) {

        FrameworkConfiguration frameworkConfiguration = null;
        try {
            frameworkConfiguration = initConfiguration(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Setting up the Framework.");
        Protos.FrameworkInfo.Builder framework = Protos.FrameworkInfo.newBuilder()
                .setName("Selenium Grid")
                .setUser("")
                .setCheckpoint(true) //DCOS-04 Scheduler MUST enable checkpointing.
                .setFailoverTimeout(86400D); //DCOS-01 Scheduler MUST register with a failover timeout.

        LOGGER.info("Setting up the State.");
        State state = new State(frameworkConfiguration.getZooKeeper());
        state.removeFrameworkId();
        frameworkConfiguration.setState(state);
        Protos.FrameworkID frameworkId = state.getFrameworkId();
        if(frameworkId != null){
            framework.setId(frameworkId); //DCOS-02 Scheduler MUST persist their FrameworkID for failover.
        }

        LOGGER.info("Setting up the Scheduler");
        final Scheduler scheduler = new SeleniumScheduler(frameworkConfiguration);
        final MesosSchedulerDriver schedulerDriver = new MesosSchedulerDriver(scheduler, framework.build(), frameworkConfiguration.getZooKeeper());

        int status = schedulerDriver.run() == Protos.Status.DRIVER_STOPPED ? 0 : 1;
        schedulerDriver.stop();
        System.exit(status);

    }

    private static FrameworkConfiguration initConfiguration(String jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(jsonFile), FrameworkConfiguration.class);
    }

}