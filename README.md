# MODAClouds PrivateCloud

## Description

Private Cloud optimizer used for cloudbursting in the SPACE4Cloud tool.

## Usage

The main class of this project is called `PrivateCloud`. Here is an example of how it is used:

```java
import it.polimi.modaclouds.space4clouds.privatecloud.PrivateCloud;
public class Example {
    public static void main(String[] args) {
        String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\";
        String configuration  = basePath + "conf-optimization-private.properties";
        String solution       = basePath + "OfBiz\\solution.xml";
        
        File f = PrivateCloud.perform(configuration, solution);
        if (f != null && f.exists())
            System.out.println("Solution: " + f.getAbsolutePath());
        else
            System.out.println("No solution!");
    }
}
```

## Installation

To use this tool you need to add it as a maven dependency:

* Group Id: it.polimi.modaclouds.space4cloud
* Artifact Id: privatecloud
* Version: 0.0.1-SNAPSHOT
* Type: jar
* Scope: compile.

You must add then the reference to the downloaded jar to the manifest of the project.