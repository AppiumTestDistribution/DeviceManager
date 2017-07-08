# iOSDeviceManager
Utility to Manage Connected iOS Devices &amp; Sims

## Maven:

Add following dependency to your pom.xml

```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>
```

```
<dependency>
	    <groupId>com.github.SrinivasanTarget</groupId>
	    <artifactId>iOSDeviceManager</artifactId>
	    <version>master-SNAPSHOT</version>
</dependency>
```

## Exposed Methods:

* getAllSimulators - Gets all available or installed simulators in macOS
* getSimulatorUDID - Returns UDID of simulator needed.
* getSimulatorState - Returns state of Simulator e.g `booted`, `shutdown`, etc
* bootSimulator - Will boot the specified Simulator