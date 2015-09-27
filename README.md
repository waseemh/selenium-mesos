mesos-selenium
========

A Mesos framework for launching Selenium instances at scale using Docker containers.
Currently, framework only supports setting up a single Selenium Grid with a single hub and multiple nodes.


Requirements
========

- Apache Mesos 0.23.0 and above
- JDK 6+ and Maven (optional for building source)

Usage
========

Framework configuration is defined in a JSON file which includes ...
In order to start the framework, both JAR file and configuration JSON file should be placed in Mesos master and launched using java command.

	java -jar /path/to/mesos-selenium.jar /path/to/config.json

Building from Sources
========

Maven is used as a build system.
In order to produce a package, run maven command `mvn clean package -DskipTests`.
Tests can be executed using command `mvn test`. 

License
========

Copyright 2014 Waseem Hamshawi

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
