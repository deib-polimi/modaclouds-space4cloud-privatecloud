package it.polimi.modaclouds.space4cloud.privatecloud.db;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataHandler {
	
	/**
	 * Instantiates a new data handler. it also charges data from the database
	 * 
	 * @param provider
	 *            the provider
	 * @throws SQLException
	 */
	public DataHandler() throws SQLException {
		try {
			FileInputStream fis = new FileInputStream(Configuration.DB_CONNECTION_FILE);
			DatabaseConnector.initConnection(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Integer> amountMemories = new HashMap<String, Integer>();
	
	public Integer getAmountMemory(String provider, String serviceName, String resourceName) {
		String key = provider + "@" + serviceName + "@" + resourceName;
		if (amountMemories.containsKey(key))
			return amountMemories.get(key);
		
		Integer memory = 0; // -1
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.Ram, provider, serviceName, resourceName));
			
			if (rs.next()) {
				memory = rs.getInt(1) * rs.getInt(2);
				amountMemories.put(key, memory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return memory;
	}
	
	private Map<String, Integer> numbersOfReplicas = new HashMap<String, Integer>();

	/**
	 * Gets the number of replicas.
	 * 
	 * @param provider
	 *            the provider
	 * @param iassServiceName
	 *            the iass service name
	 * @param resourceName
	 *            the resource name
	 * @return the number of replicas
	 */
	public Integer getNumberOfReplicas(String provider, String serviceName, String resourceName) {
		String key = provider + "@" + serviceName + "@" + resourceName;
		if (numbersOfReplicas.containsKey(key))
			return numbersOfReplicas.get(key);
		
		Integer numberOfReplicas = 0; // -1
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.CpuSpeedCores, provider, serviceName, resourceName));
			
			if (rs.next()) {
				numberOfReplicas = rs.getInt(2);
				numbersOfReplicas.put(key, numberOfReplicas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return numberOfReplicas;

	}
	
	private Map<String, Integer> processingRates = new HashMap<String, Integer>();

	/**
	 * Gets the processing rate of the cpus.
	 * 
	 * @param provider
	 *            the id provider
	 * @param serviceName
	 *            the id iass service
	 * @param resourceName
	 *            the id resource
	 * @return the speed
	 */
	public Integer getProcessingRate(String provider, String serviceName, String resourceName) {
		String key = provider + "@" + serviceName + "@" + resourceName;
		if (processingRates.containsKey(key))
			return processingRates.get(key);
		
		Integer processingRate = 0; // -1
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.CpuSpeedCores, provider, serviceName, resourceName));
			
			if (rs.next()) {
				processingRate = rs.getInt(1);
				processingRates.put(key, processingRate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return processingRate;

	}
	
	private Map<String, Integer> storages = new HashMap<String, Integer>();
	
	public Integer getStorage(String provider, String serviceName, String resourceName) {
		String key = provider + "@" + serviceName + "@" + resourceName;
		if (storages.containsKey(key))
			return storages.get(key);
		
		Integer storage = 0; // -1
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.Storage, provider, serviceName, resourceName));
			
			if (rs.next()) {
				storage = rs.getInt(1) * rs.getInt(2);
				storages.put(key, storage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return storage;

	}
	
	private Map<String, Double> costs = new HashMap<String, Double>();
	
	public Double getCost(String provider, String serviceName, String resourceName, String region) {
		String key = provider + "@" + serviceName + "@" + resourceName;
		if (costs.containsKey(key))
			return costs.get(key);
		
		Double cost = Double.MAX_VALUE; // -1.0
		
		String query = "";
		if (region == null)
			query = String.format(QueryDictionary.CostNoRegion, provider, serviceName, resourceName);
		else
			query = String.format(QueryDictionary.CostRegion, provider, serviceName, resourceName, region);
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if (rs.next()) {
				cost = rs.getDouble(1);
				costs.put(key, cost);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cost;

	}
	
}
