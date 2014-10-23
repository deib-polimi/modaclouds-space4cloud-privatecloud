package it.polimi.modaclouds.space4cloud.privatecloud;

import java.io.File;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\OfBiz\\";
		String configuration  = basePath + "conf-private-1p.properties";
		String solution       = basePath + "initial-solution-small.xml";
		
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
