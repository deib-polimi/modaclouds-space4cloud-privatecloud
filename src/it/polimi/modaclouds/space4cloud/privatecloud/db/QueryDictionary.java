package it.polimi.modaclouds.space4cloud.privatecloud.db;

public final class QueryDictionary {
	// 3 parameters: provider name, service name and resource name
	public static final String CpuSpeedCores =
			"SELECT virtualhwresource.processingRate,virtualhwresource.numberOfReplicas " +
			"FROM cloudprovider,cloudresource,cloudresource_allocation,virtualhwresource,iaas_service,iaas_service_composedof " +
			"WHERE cloudprovider.id=iaas_service.CloudProvider_id AND cloudresource_allocation.VirtualHWResource_id=virtualhwresource.id AND virtualhwresource.type='cpu' AND cloudresource.id=cloudresource_allocation.CloudResource_id AND cloudresource_allocation.CloudResource_id=iaas_service_composedof.CloudResource_id AND iaas_service_composedof.IaaS_id=iaas_service.id " +
			"AND cloudprovider.name='%s' AND iaas_service.name = '%s' AND cloudresource.name = '%s' "+
			"LIMIT 1";
	public static final String Ram =
			"SELECT virtualhwresource.size,virtualhwresource.numberOfReplicas " +
			"FROM cloudprovider,cloudresource,cloudresource_allocation,virtualhwresource,iaas_service,iaas_service_composedof " +
			"WHERE cloudprovider.id=iaas_service.CloudProvider_id AND cloudresource_allocation.VirtualHWResource_id=virtualhwresource.id AND virtualhwresource.type='memory' AND cloudresource.id=cloudresource_allocation.CloudResource_id AND cloudresource_allocation.CloudResource_id=iaas_service_composedof.CloudResource_id AND iaas_service_composedof.IaaS_id=iaas_service.id " +
			"AND cloudprovider.name='%s' AND iaas_service.name = '%s' AND cloudresource.name = '%s' " +
			"LIMIT 1";
	public static final String Storage =
			"SELECT virtualhwresource.size,virtualhwresource.numberOfReplicas " +
			"FROM cloudprovider,cloudresource,cloudresource_allocation,virtualhwresource,iaas_service,iaas_service_composedof " +
			"WHERE cloudprovider.id=iaas_service.CloudProvider_id AND cloudresource_allocation.VirtualHWResource_id=virtualhwresource.id AND virtualhwresource.type='storage' AND cloudresource.id=cloudresource_allocation.CloudResource_id AND cloudresource_allocation.CloudResource_id=iaas_service_composedof.CloudResource_id AND iaas_service_composedof.IaaS_id=iaas_service.id " +
			"AND cloudprovider.name='%s' AND iaas_service.name = '%s' AND cloudresource.name = '%s' " +
			"LIMIT 1";
	public static final String CostRegion =
			"SELECT AVG(cost.value) FROM iaas_service, cloudprovider, iaas_service_composedof, cloudresource, cloudresource_cost, cost " +
			"WHERE iaas_service.CloudProvider_id = cloudprovider.id AND iaas_service_composedof.IaaS_id = iaas_service.id AND iaas_service_composedof.CloudResource_id = cloudresource.id AND cloudresource_cost.CloudResource_id = cloudresource.id AND cloudresource_cost.Cost_id = cost.id AND cost.description NOT LIKE 'Reserved%%' " +
			"AND cloudprovider.name = '%s' AND iaas_service.name = '%s' AND cloudresource.name = '%s' " +
			"AND cost.region = '%s'";
	public static final String CostNoRegion =
			"SELECT AVG(cost.value) FROM iaas_service, cloudprovider, iaas_service_composedof, cloudresource, cloudresource_cost, cost " +
			"WHERE iaas_service.CloudProvider_id = cloudprovider.id AND iaas_service_composedof.IaaS_id = iaas_service.id AND iaas_service_composedof.CloudResource_id = cloudresource.id AND cloudresource_cost.CloudResource_id = cloudresource.id AND cloudresource_cost.Cost_id = cost.id AND cost.description NOT LIKE 'Reserved%%' " +
			"AND cloudprovider.name = '%s' AND iaas_service.name = '%s' AND cloudresource.name = '%s'";
}
