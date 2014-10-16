package it.polimi.modaclouds.space4cloud.privatecloud.db;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataHandler {
	//	private static final Logger logger = LoggerHelper.getLogger(DataHandler.class);

	private static final Logger logger=LoggerFactory.getLogger(DataHandler.class);
	
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
	
	
	public Integer getAmountMemory(String provider, String serviceName, String resourceName) {
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.Ram, provider, serviceName, resourceName));
			
			if (rs.next()) {
				return rs.getInt(1) * rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;

	}

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
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.CpuSpeedCores, provider, serviceName, resourceName));
			
			if (rs.next()) {
				return rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;

	}

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
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.CpuSpeedCores, provider, serviceName, resourceName));
			
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;

	}
	
	public Integer getStorage(String provider, String serviceName, String resourceName) {
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(String.format(QueryDictionary.Storage, provider, serviceName, resourceName));
			
			if (rs.next()) {
				return rs.getInt(1) * rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;

	}
	
	public Double getCost(String provider, String serviceName, String resourceName, String region) {
		String query = "";
		if (region == null)
			query = String.format(QueryDictionary.CostNoRegion, provider, serviceName, resourceName);
		else
			query = String.format(QueryDictionary.CostRegion, provider, serviceName, resourceName, region);
		
		try {
			Connection db = DatabaseConnector.getConnection();
			ResultSet rs = db.createStatement().executeQuery(query);
			
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1.0;

	}
	
}
