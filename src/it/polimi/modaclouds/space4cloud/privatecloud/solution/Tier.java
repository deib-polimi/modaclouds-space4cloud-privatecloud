package it.polimi.modaclouds.space4cloud.privatecloud.solution;


public class Tier {
	public String id;
	public String name;
	public String providerName;
	public String resourceName;
	public String serviceName;
	public String serviceType;
	
	public Machine[] machines;
	
	public Tier(String providerName, String id, String name, String resourceName, String serviceName, String serviceType) {
		this.id = id;
		this.name = name;
		this.providerName = providerName;
		this.resourceName = resourceName;
		this.serviceName = serviceName;
		this.serviceType = serviceType;
		
		machines = new Machine[24];
		
		for (int h = 0; h < machines.length; ++h)
			machines[h] = new Machine();
	}
	
	public int getMaxMachines() {
		int maxTier = 0;
		for (int i = 0; i < machines.length; ++i) {
			if (machines[i].replicas > maxTier)
				maxTier = machines[i].replicas;
		}
		return maxTier;
	}
	
}
