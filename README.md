# MODAClouds PrivateCloud

## Description

Private Cloud optimizer used for cloudbursting in the SPACE4Cloud tool.

## Usage

The main class of this project is called `PrivateCloud`. Here is an example of how it is used:

```java
import it.polimi.modaclouds.space4clouds.privatecloud.PrivateCloud;
public class Example {
    public static void main(String[] args) {
        String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\OfBiz\\";
        String configuration  = basePath + "configuration.properties";
        String solution       = basePath + "initial-solution.xml";
        
        PrivateCloud.removeTempFiles = false;
        
        List<File> files = PrivateCloud.perform(configuration, solution);
        boolean done = false;
        for (File f : files) {
            System.out.println("Solution: " + f.getAbsolutePath());
            done = true;
        }
        if (!done)
            System.out.println("No solution!");
    }
}
```

## Installation

To use this tool you need to add it as a maven dependency:

* Group Id: it.polimi.modaclouds.space4cloud
* Artifact Id: privatecloud
* Version: 0.1.1
* Type: jar
* Scope: compile.

You must add then the reference to the downloaded jar to the manifest of the project.