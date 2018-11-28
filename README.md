# DeviceManager
Utility to Manage Connected iOS &amp; Android Devices (Including iOS Simulators and Android Emulators)

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
* install - Will install the application on Simulator
* uninstall - Will remove the application from Simulator
* getSimulatorDetailsFromUDID - Retrives simulator details for given UDID
* captureScreenshot - Captures screenshot for given UDID.
* shutDownAllBootedSimulators - Shutdown all booted simulators
* getAllBootedSimulators - Retrives all booted simulators
