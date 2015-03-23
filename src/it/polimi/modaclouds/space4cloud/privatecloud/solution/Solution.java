package it.polimi.modaclouds.space4cloud.privatecloud.solution;

import it.polimi.modaclouds.qos_models.schema.CloudService;
import it.polimi.modaclouds.qos_models.schema.ObjectFactory;
import it.polimi.modaclouds.qos_models.schema.Replica;
import it.polimi.modaclouds.qos_models.schema.ReplicaElement;
import it.polimi.modaclouds.qos_models.schema.ResourceContainer;
import it.polimi.modaclouds.qos_models.schema.ResourceModelExtension;

import java.util.LinkedHashMap;
import java.util.Map;

public class Solution {
	public String providerName;
	public Map<String, Tier> tiers;
	
	public Solution(String providerName) {
		this.providerName = providerName;
		tiers = new LinkedHashMap<String, Tier>();
	}
	
	public ResourceModelExtension getAsExtension() {
		//Build the objects
		ObjectFactory factory = new ObjectFactory();
		ResourceModelExtension extension = factory.createResourceModelExtension();
		//initialize fields common to all hours by looking at the first one
		for(Tier t : tiers.values()){
			//build the resource container that maps the tier
			ResourceContainer container = factory.createResourceContainer(); 
			extension.getResourceContainer().add(container);
			
			container.setId(t.id);
			container.setProvider(t.providerName);
			container.setName(t.name);
			
			CloudService cs = factory.createCloudService();
			container.setCloudElement(cs);
			
			cs.setServiceCategory("IaaS");
			cs.setServiceName(t.serviceName);
			cs.setServiceType(t.serviceType);
			cs.setResourceSizeID(t.resourceName);
			
			Replica r = factory.createReplica();
			cs.setReplicas(r);
			
			for (int i = 0; i < 24; i++) {
				ReplicaElement re = factory.createReplicaElement();
				re.setHour(i);
				re.setValue(t.machines[i].replicas);
				r.getReplicaElement().add(re);
			}
		}
		
		return extension;
	}
}
