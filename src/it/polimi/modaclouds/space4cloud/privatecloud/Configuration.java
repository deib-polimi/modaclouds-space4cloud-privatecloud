package it.polimi.modaclouds.space4cloud.privatecloud;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
	
	// Information about the application
	public static String WORKING_DIRECTORY = "space4cloud";
	public static String PROJECT_BASE_FOLDER;
	public static String PALLADIO_RESOURCE_MODEL;
	public static String PALLADIO_USAGE_MODEL;
	public static String PALLADIO_ALLOCATION_MODEL;
	public static String PALLADIO_REPOSITORY_MODEL;
	public static String PALLADIO_SYSTEM_MODEL;
	
	public static String CONSTRAINTS;
	public static String USAGE_MODEL_EXTENSION;
	public static String RESOURCE_ENVIRONMENT_EXTENSION;

	// Information about the DB
	public static String DB_CONNECTION_FILE;

	// Information about the machine with AMPL
	public static String SSH_HOST = "ch14r4.dei.polimi.it";
	public static String SSH_USER_NAME;
	public static String SSH_PASSWORD;
	
	// Information about the private cloud
	public static boolean USE_PRIVATE_CLOUD = true;
	public static String PRIVATE_CLOUD_HOSTS;
	
	// Information used in the AMPL.run file
	public static String RUN_WORKING_DIRECTORY = "/home/s4c/new64";
	public static String RUN_FILE = "AMPL.run";
	public static String RUN_MODEL = "modelbursting.mod";
	public static String RUN_DATA = "data.dat";
	public static String RUN_SOLVER = "/usr/optimization/CPLEX_Studio_Preview126/cplex/bin/x86-64_linux/cplexamp";
	
	public static String DEFAULTS_BASH = "bash.run";
	public static String DEFAULTS_FOLDER = "files";
	
	public static void saveConfiguration(String filePath) throws IOException{
		FileOutputStream fos = new FileOutputStream(filePath);
		Properties prop = new Properties();
		prop.put("PALLADIO_REPOSITORY_MODEL", PALLADIO_REPOSITORY_MODEL);
		prop.put("PALLADIO_SYSTEM_MODEL", PALLADIO_SYSTEM_MODEL);
		prop.put("PALLADIO_ALLOCATION_MODEL", PALLADIO_ALLOCATION_MODEL);
		prop.put("PALLADIO_USAGE_MODEL", PALLADIO_USAGE_MODEL);
		prop.put("PALLADIO_RESOURCE_MODEL", PALLADIO_RESOURCE_MODEL);
		prop.put("USAGE_MODEL_EXTENSION", USAGE_MODEL_EXTENSION);
		prop.put("RESOURCE_ENVIRONMENT_EXTENSION", RESOURCE_ENVIRONMENT_EXTENSION);
		prop.put("CONSTRAINTS", CONSTRAINTS);
		prop.put("PROJECT_BASE_FOLDER", PROJECT_BASE_FOLDER);
		prop.put("DB_CONNECTION_FILE", DB_CONNECTION_FILE);
		
		prop.put("SSH_HOST", SSH_HOST);
		prop.put("SSH_USER_NAME", SSH_USER_NAME);
		prop.put("SSH_PASSWORD", SSH_PASSWORD);
		
		prop.put("USE_PRIVATE_CLOUD", Boolean.toString(USE_PRIVATE_CLOUD));
		prop.put("PRIVATE_CLOUD_HOSTS", PRIVATE_CLOUD_HOSTS);
		
		prop.put("RUN_WORKING_DIRECTORY", RUN_WORKING_DIRECTORY);
		prop.put("RUN_MODEL", RUN_MODEL);
		prop.put("RUN_DATA", RUN_DATA);
		prop.put("RUN_SOLVER", RUN_SOLVER);
		prop.put("RUN_FILE", RUN_FILE);
		
		prop.put("DEFAULTS_BASH", DEFAULTS_BASH);
		prop.put("DEFAULTS_FOLDER", DEFAULTS_FOLDER);
		
		prop.store(fos, "S4C-PrivateCloud configuration properties");
		fos.flush();
	}
	
	public static void loadConfiguration(String filePath) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(filePath);
		prop.load(fis);
		PALLADIO_REPOSITORY_MODEL = prop.getProperty("PALLADIO_REPOSITORY_MODEL", PALLADIO_REPOSITORY_MODEL);
		PALLADIO_SYSTEM_MODEL = prop.getProperty("PALLADIO_SYSTEM_MODEL", PALLADIO_SYSTEM_MODEL);
		PALLADIO_ALLOCATION_MODEL = prop.getProperty("PALLADIO_ALLOCATION_MODEL", PALLADIO_ALLOCATION_MODEL);
		PALLADIO_USAGE_MODEL = prop.getProperty("PALLADIO_USAGE_MODEL", PALLADIO_USAGE_MODEL);
		PALLADIO_RESOURCE_MODEL = prop.getProperty("PALLADIO_RESOURCE_MODEL", PALLADIO_RESOURCE_MODEL);
		USAGE_MODEL_EXTENSION = prop.getProperty("USAGE_MODEL_EXTENSION", USAGE_MODEL_EXTENSION);
		RESOURCE_ENVIRONMENT_EXTENSION = prop.getProperty("RESOURCE_ENVIRONMENT_EXTENSION", RESOURCE_ENVIRONMENT_EXTENSION);
		CONSTRAINTS = prop.getProperty("CONSTRAINTS", CONSTRAINTS);
		PROJECT_BASE_FOLDER = prop.getProperty("PROJECT_BASE_FOLDER", PROJECT_BASE_FOLDER);
		DB_CONNECTION_FILE= prop.getProperty("DB_CONNECTION_FILE", DB_CONNECTION_FILE);
		SSH_PASSWORD = prop.getProperty("SSH_PASSWORD", SSH_PASSWORD);
		SSH_USER_NAME = prop.getProperty("SSH_USER_NAME", SSH_USER_NAME);
		SSH_HOST = prop.getProperty("SSH_HOST", SSH_HOST);
		
		USE_PRIVATE_CLOUD = Boolean.parseBoolean(prop.getProperty("USE_PRIVATE_CLOUD", String.valueOf(USE_PRIVATE_CLOUD)));
		PRIVATE_CLOUD_HOSTS = prop.getProperty("PRIVATE_CLOUD_HOSTS", PRIVATE_CLOUD_HOSTS);
		
		RUN_WORKING_DIRECTORY = prop.getProperty("RUN_WORKING_DIRECTORY", RUN_WORKING_DIRECTORY);
		RUN_MODEL = prop.getProperty("RUN_MODEL", RUN_MODEL);
		RUN_DATA = prop.getProperty("RUN_DATA", RUN_DATA);
		RUN_SOLVER = prop.getProperty("RUN_SOLVER", RUN_SOLVER);
		RUN_FILE = prop.getProperty("RUN_FILE", RUN_FILE);
		
		DEFAULTS_BASH = prop.getProperty("DEFAULTS_BASH", DEFAULTS_BASH);
		DEFAULTS_FOLDER = prop.getProperty("DEFAULTS_FOLDER", DEFAULTS_FOLDER);
	}
}
