package it.polimi.modaclouds.space4cloud.privatecloud;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void doMain(String configuration, String solution) {
		PrivateCloud.removeTempFiles = false;
		
		try {
			List<File> files = PrivateCloud.perform(configuration, solution);
			boolean done = false;
			for (File f : files) {
				logger.debug("Solution: " + f.getAbsolutePath());
				done = true;
			}
			if (!done)
				logger.error("No solution!");
		} catch (Exception e) {
			logger.error("Error while getting the solution!", e);
		}
	}

	public static void mainOfBiz(String[] args) {
		String basePath       = "C:\\Users\\Riccardo\\Desktop\\SPACE4CLOUD\\runtime-New_configuration\\OfBiz\\";
		String configuration  = basePath + "conf-private-1p.properties";
		String solution       = basePath + "initial-solution-amazon-broken.xml";
		
		doMain(configuration, solution);
	}
	
	public static void mainConstellation(String[] args) {
		String basePath       = "/Users/ft/Development/workspace-s4c-runtime/Constellation/";
		String configuration  = basePath + "OptimizationMacLocal.properties";
		String solution       = basePath + "ContainerExtensions/Computed/Solution-Conference-Amazon.xml";
		
		doMain(configuration, solution);
	}
	
	public static void main(String[] args) {
		mainConstellation(args);
	}
}
