package it.polimi.modaclouds.space4cloud.privatecloud;

import java.io.File;

public class Main {

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
