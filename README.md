selenium-mesos
========

A Mesos framework for launching Selenium instances at scale using Docker containers.
Currently, framework only supports setting up a single Selenium Grid with a single hub and multiple nodes.


Roadmap
=======

### Features
- Support complex setups with multiple grids and standalone instances.
- Non-ZooKeeper architectures.

Requirements
========

- Apache Mesos 0.23.0 and above
- JDK 6+ and Maven (optional for building source)

Configuration
========

Framework is configured using a JSON-format file. It includes ZooKeeper URL(s) and the requested Selenium instances with their resource requirements.

An example of a Grid setup which includes one hub and three nodes (two firefox + one chrome):

	{
	    "zooKeeper": "zk://172.31.0.11:2181/mesos",
	    "grid": {
	        "hub": {
	            "mem": 256,
	            "cpus": 0.5,
	            "disk": 512
	        },
	        "nodes": [
	            {
	                "browser": "firefox",
	                "mem": 512,
	                "cpus": 0.5,
	                "disk": 512,
	                "instances": 2
	            },
	            {
	                "browser": "chrome",
	                "mem": 512,
	                "cpus": 0.5,
	                "disk": 256,
	                "instances": 1
	            }
	        ]
	    }
	}

Building from Sources
========

Maven is used as a build system.
In order to produce a package, run maven command `mvn clean package -DskipTests`.
Tests can be executed using command `mvn test`. 

Running the framework
========

In order to start the framework, both JAR file and configuration JSON file should be placed in Mesos master and launched using java command.

	java -jar /path/to/mesos-selenium.jar /path/to/config.json

License
========

Copyright to Waseem Hamshawi

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
