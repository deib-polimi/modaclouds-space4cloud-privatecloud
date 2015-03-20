package it.polimi.modaclouds.space4cloud.privatecloud;

import java.io.File;
import java.util.List;

public class Main {

	public static void mainOfBiz(String[] args) {
		String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\OfBiz\\";
		String configuration  = basePath + "conf-private-1p.properties";
//		String solution       = basePath + "initial-solution-amazon.xml";
		String solution       = basePath + "initial-solution-amazon-broken.xml";

		
		PrivateCloud.removeTempFiles = false;
		
		try {
			List<File> files = PrivateCloud.perform(configuration, solution);
			boolean done = false;
			for (File f : files) {
				System.out.println("Solution: " + f.getAbsolutePath());
				done = true;
			}
			if (!done)
				System.out.println("No solution!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void mainConstellation(String[] args) {
		String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\Constellation\\";
		String configuration  = basePath + "test.properties";
		String solution       = basePath + "testSolution.xml";

		
		PrivateCloud.removeTempFiles = false;
		
		try {
			List<File> files = PrivateCloud.perform(configuration, solution);
			boolean done = false;
			for (File f : files) {
				System.out.println("Solution: " + f.getAbsolutePath());
				done = true;
			}
			if (!done)
				System.out.println("No solution!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		mainConstellation(args);
	}
}
