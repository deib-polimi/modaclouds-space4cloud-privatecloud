package it.polimi.modaclouds.space4cloud.privatecloud;

import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Host {
	public final static double DEFAULT_HOURLY_COST = 10.0;
	
	public String name;
	
	public int cpu_cores; 		// number
	public double cpu_speed; 	// MHz
	public int ram; 			// MB
	public int storage; 		// GB
	public double density;		
	
	public String toString() {
		return name + " (CPU: " + cpu_cores + "x" + cpu_speed + " MHz, RAM: " + ram + " MB, Disk: " + storage + " GB, Density: " + density + ")";
	}
	
	public double[] hourlyCosts;
	
	public Host(String name, int cpu_cores, double cpu_speed, int ram, int storage, double density, double[] hourlyCosts) {
		this.name = name;
		
		this.cpu_cores = cpu_cores;
		this.cpu_speed = cpu_speed;
		this.ram = ram;
		this.storage = storage;
		this.density = density;
		
		this.hourlyCosts = hourlyCosts;
	}
	
	public static List<Host> getFromFile(File f) {
		List<Host> hosts = new ArrayList<Host>();
		
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			prop.load(fis);
		} catch (Exception e) {
			return hosts;
		}
		
		boolean goOn = true;
		int i = 0;
		
		while (goOn) {
			try {
				String name = prop.getProperty(i + "name");
				int cpu_cores = Integer.parseInt(prop.getProperty(i + "cpu_cores"));
				double cpu_speed = Double.parseDouble(prop.getProperty(i + "cpu_speed"));
				int ram = Integer.parseInt(prop.getProperty(i + "ram"));
				int storage = Integer.parseInt(prop.getProperty(i + "storage"));
				double density = Double.parseDouble(prop.getProperty(i + "density"));
				
				double hourlyCosts[] = new double[24];
				for (int h = 0; h < hourlyCosts.length; ++h) {
					hourlyCosts[h] = Double.parseDouble(prop.getProperty(i + "hourlyCost-" + h));
				}
				Host host = new Host(name, cpu_cores, cpu_speed, ram, storage, density, hourlyCosts);
				hosts.add(host);
				
				i++;
			} catch (Exception e) {
				goOn = false;
			}
		}
		
		try {
			fis.close();
		} catch (IOException e) { }
		return hosts;
	}
	
	public static void writeToFile(File f, List<Host> hosts) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
		} catch (Exception e) {
			return;
		}
		Properties prop = new Properties();
		
		for (int i = 0; i < hosts.size(); ++i) {
			Host host = hosts.get(i);
			
			prop.put(i + "name", host.name);
			prop.put(i + "cpu_cores", Integer.toString(host.cpu_cores));
			prop.put(i + "cpu_speed", Double.toString(host.cpu_speed));
			prop.put(i + "ram", Integer.toString(host.ram));
			prop.put(i + "storage", Integer.toString(host.storage));
			prop.put(i + "density", Double.toString(host.density));
			
			int h = 0;
			for (; h < host.hourlyCosts.length; ++h)
				prop.put(i + "hourlyCost-" + h, Double.toString(host.hourlyCosts[h]));
			for (; h < 24; ++h)
				prop.put(i + "hourlyCost-" + h, Double.toString(DEFAULT_HOURLY_COST));
		}
		
		try {
			prop.store(fos, "Private Cloud configuration properties");
			fos.flush();
			fos.close();
		} catch (IOException e) { }
	}
	
	public SolutionMulti allocatedSolutions = new SolutionMulti();
	
	public void addMachine(Tier t, int h) {
		Solution sol = allocatedSolutions.add(t.providerName, t.id, t.name, t.resourceName, t.serviceName, t.serviceType);
		++sol.tiers.get(t.id).machines[h].replicas;
	}
	
}
