package it.polimi.modaclouds.space4cloud.privatecloud.solution;

import it.polimi.modaclouds.qos_models.schema.Location;
import it.polimi.modaclouds.qos_models.schema.ObjectFactory;
import it.polimi.modaclouds.qos_models.schema.Replica;
import it.polimi.modaclouds.qos_models.schema.ReplicaElement;
import it.polimi.modaclouds.qos_models.schema.ResourceContainer;
import it.polimi.modaclouds.qos_models.schema.ResourceModelExtension;
import it.polimi.modaclouds.qos_models.util.XMLHelper;
import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.db.DataHandler;
import it.polimi.modaclouds.space4cloud.privatecloud.db.DataHandlerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class should handle a multi-provider solution, or also, in the
 * particular case, that of a single solution.
 * 
 */
public class SolutionMulti implements Cloneable, Serializable {

	private static final long serialVersionUID = -9050926347950168327L;
	private static final Logger logger = LoggerFactory.getLogger(SolutionMulti.class);
	
	/** The Cost. */
	private int cost = 0;
	
	/**
	 * if the solution has been evaluated or not.
	 */
	private boolean evaluated = false;

	/** if the solution is feasible or not. */
	private boolean feasible = false;

	public int getCost() {
		return cost;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public boolean isFeasible() {
		return feasible;
	}
	
	// The keys are the providers name, the allocations object are the allocations for each hour.
	private Map<String, Solution> solutions = new LinkedHashMap<String, Solution>();
	
	public int size() {
		return solutions.keySet().size();
	}
	
	public Solution get(String providerName) {
		return solutions.get(providerName);
	}
	
	public Solution add(String providerName, String tierId, String name, 
				String resourceName, String serviceName, String serviceType) {
		Solution sol;
		if (solutions.containsKey(providerName))
			sol = solutions.get(providerName);
		else {
			sol = new Solution(providerName);
			solutions.put(providerName, sol);
		}
		
		if (tierId != null && !sol.tiers.containsKey(tierId)) {
			Tier tier = new Tier(providerName, tierId, name, resourceName, serviceName, serviceType);
			sol.tiers.put(tierId, tier);
		}
		
		return sol;
	}

	public static boolean isEmpty(File solution) {
		if (isResourceModelExtension(solution))
			return isEmptyResourceModelExtension(solution);
		else
			return isEmptyFileSolution(solution);
	}
	
	private static boolean isResourceModelExtension(File f) {
		try {
			XMLHelper.deserialize(f.toURI().toURL(), ResourceModelExtension.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static boolean isEmptyResourceModelExtension(File solution) {
		try {
			ResourceModelExtension rme = XMLHelper.deserialize(solution
					.toURI().toURL(), ResourceModelExtension.class);
			
			for (ResourceContainer rc : rme.getResourceContainer()) {
				it.polimi.modaclouds.qos_models.schema.CloudService cs = rc.getCloudElement();
				if (cs != null) {
					Replica r = cs.getReplicas();
					if (r != null) {
						List<ReplicaElement> re = r.getReplicaElement();
						if (re.size() > 0)
							return false;
					}
				}
			}
			
		} catch (MalformedURLException | JAXBException | SAXException e) {
			logger.error("Error in checking if the solution is empty",e);
		}
		return true;
	}

	private static boolean isEmptyFileSolution(File solution) {
		if (solution != null && solution.exists())
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(solution);
				doc.getDocumentElement().normalize();

				{
					NodeList nl = doc.getElementsByTagName("HourAllocation");
					return (nl.getLength() == 0);
				}
			} catch (Exception e) {
				logger.error("Error in checking if the solution is empty",e);
			}
		return true;
	}
	
	public SolutionMulti() { }
	
	public SolutionMulti(File solution) {
		setFrom(solution);
	}
	
	public String getRegion(String providerName, String tierId) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ResourceModelExtension.class);
			 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ResourceModelExtension rme = (ResourceModelExtension) jaxbUnmarshaller.unmarshal(new File(Configuration.RESOURCE_ENVIRONMENT_EXTENSION));
			
			List<ResourceContainer> rcs = rme.getResourceContainer();
			for (ResourceContainer rc : rcs) {
				if (rc.getId().equals(tierId) && rc.getProvider().equals(providerName)) {
					Location l = rc.getCloudElement().getLocation();
					if (l == null)
						return null;
					else
						return l.getRegion();
				}
			}
		} catch (Exception e) {
			logger.error("Error while getting the region from the resource environment extension.", e);
		}
		
		return null;
	}
	
	public boolean setFrom(File initialSolution) {
		if (isResourceModelExtension(initialSolution))
			return setFromResourceModelExtension(initialSolution);
		else
			return setFromFileSolution(initialSolution);
	}
	
	private boolean setFromResourceModelExtension(File initialSolution) {
		boolean res = false;
		
		if (initialSolution != null) {
			try {
				ResourceModelExtension rme = XMLHelper.deserialize(initialSolution
						.toURI().toURL(), ResourceModelExtension.class);
				
				cost = Integer.MAX_VALUE;
				
				for (ResourceContainer rc : rme.getResourceContainer()) {
					it.polimi.modaclouds.qos_models.schema.CloudService cs = rc.getCloudElement();
					
					String provider = rc.getProvider();
					String tierId = rc.getId();
					String name = rc.getName();
					String resourceName = cs.getResourceSizeID();
					String serviceName = cs.getServiceName();
					String serviceType = cs.getServiceType();
					
					if (!serviceType.equals("Compute"))
						continue;
					
					String region = null;
					
					Location l = cs.getLocation();
					if (l != null)
						 region = l.getRegion();
					
					Solution sol = add(provider, tierId, name, resourceName, serviceName, serviceType);
					
					Tier t = sol.tiers.get(tierId);

					Solution solution = get(provider);
					if (solution == null)
						continue;

					DataHandler dataHandler = DataHandlerFactory.getHandler();

					double speed = dataHandler.getProcessingRate(provider,
							serviceName, resourceName);
					int ram = dataHandler.getAmountMemory(provider, serviceName,
							resourceName);
					int numberOfCores = dataHandler.getNumberOfReplicas(provider,
							serviceName, resourceName);
					int storage = dataHandler.getStorage(provider,
							serviceName, resourceName);
					double cost = dataHandler.getCost(provider, serviceName, resourceName, region);

					Replica r = cs.getReplicas();
					if (r == null)
						continue;
					List<ReplicaElement> hourAllocations = r.getReplicaElement();
					
					for (ReplicaElement m : hourAllocations) {
						int hour = m.getHour();
						int allocation = m.getValue();
						
						t.machines[hour].cpu_speed = speed;
						t.machines[hour].ram = ram;
						t.machines[hour].cpu_cores = numberOfCores;
						t.machines[hour].storage = storage;
						t.machines[hour].replicas = allocation;
						t.machines[hour].cost = cost;
					}
				}
				
				res = true;
				
			} catch (Exception e) {
				logger.error("Error while importing data from a solution file.", e);
				return false;
			}
		}
		
		return res;
	}

	@Deprecated
	private boolean setFromFileSolution(File initialSolution) {
		
		boolean res = false;
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(initialSolution);
			doc.getDocumentElement().normalize();

			{
				Element root = (Element) doc.getElementsByTagName(
						"SolutionMultiResult").item(0);

				cost = (int) Math.round(Double.parseDouble(root
						.getAttribute("cost")));
			}

			NodeList tiers = doc.getElementsByTagName("Tier");

			for (int i = 0; i < tiers.getLength(); ++i) {
				Node n = tiers.item(i);

				if (n.getNodeType() != Node.ELEMENT_NODE)
					continue;

				Element tier = (Element) n;
				String provider = tier.getAttribute("providerName");
				String tierId = tier.getAttribute("id");
				String name = tier.getAttribute("name");
				String resourceName = tier.getAttribute("resourceName");
				String serviceName = tier.getAttribute("serviceName");
				String serviceType = tier.getAttribute("serviceType");
				
				if (!serviceType.equals("Compute"))
					continue;
				
				String region = getRegion(provider, tierId);
				
				Solution sol = add(provider, tierId, name, resourceName, serviceName, serviceType);
				
				Tier t = sol.tiers.get(tierId);

				Solution solution = get(provider);
				if (solution == null)
					continue;

				DataHandler dataHandler = DataHandlerFactory.getHandler();

				double speed = dataHandler.getProcessingRate(provider,
						serviceName, resourceName);
				int ram = dataHandler.getAmountMemory(provider, serviceName,
						resourceName);
				int numberOfCores = dataHandler.getNumberOfReplicas(provider,
						serviceName, resourceName);
				int storage = dataHandler.getStorage(provider,
						serviceName, resourceName);
				double cost = dataHandler.getCost(provider, serviceName, resourceName, region);

				NodeList hourAllocations = tier
						.getElementsByTagName("HourAllocation");

				for (int j = 0; j < hourAllocations.getLength(); ++j) {
					Node m = hourAllocations.item(j);

					if (m.getNodeType() != Node.ELEMENT_NODE)
						continue;

					Element hourAllocation = (Element) m;
					int hour = Integer.parseInt(hourAllocation
							.getAttribute("hour"));
					int allocation = Integer.parseInt(hourAllocation
							.getAttribute("allocation"));
					
					t.machines[hour].cpu_speed = speed;
					t.machines[hour].ram = ram;
					t.machines[hour].cpu_cores = numberOfCores;
					t.machines[hour].storage = storage;
					t.machines[hour].replicas = allocation;
					t.machines[hour].cost = cost;
				}
			}
			
			res = true;
			
		} catch (Exception e) {
			logger.error("Error while getting the information from the file solution.", e);
			return false;
		}
		
		return res;
	}

	/**
	 * Show status.
	 */
	public String showStatus() {
		String result = "SolutionMulti Status\n";
		result += "Total cost: " + getCost();
		result += "\tEvaluated: " + isEvaluated();
		result += "\tFeasible: " + isFeasible();
		result += "\tProviders: " + size();
		
		return result;
	}

	@Override
	public String toString() {
		String result = "SolutionMulti@"
				+ Integer.toHexString(super.hashCode());
		result += "[Cost: " + getCost();
		result += ", Providers: " + size();
		result += ", Evaluated: " + isEvaluated();
		result += ", Feasible: " + isFeasible();
		result += "]";
		return result;
	}
	
	public int getTotalMachines() {
		int res = 0;
		for (Solution s : solutions.values()) {
			for (Tier t : s.tiers.values()) {
				res += t.getMaxMachines();
			}
		}
		return res;
	}
	
//	@SuppressWarnings("unused")
	public int getTotalTiers() {
		int res = 0;
		for (Solution s : solutions.values()) {
			res += s.tiers.size();
//			for (Tier t : s.tiers.values()) {
//				res++;
//			}
		}
		return res;
	}
	
	public Collection<Solution> getAll() {
		return solutions.values();
	}

	public Solution get(int i) {
		if (i < 0 || i >= solutions.size())
			return null;
			
		int tmp = 0;
		for (String s : solutions.keySet()) {
			if (tmp == i)
				return solutions.get(s);
			++tmp;
		}
		return null;
	}

	
	public void exportLight(Path filePath) {
//		try {
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory
//					.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			// root elements
//			Document doc = docBuilder.newDocument();
//			Element rootElement = doc.createElement("SolutionMultiResult");
//			doc.appendChild(rootElement);
//
//			// set cost
//			rootElement.setAttribute("cost", "" + getCost());
//			// set evaluationtime
//			rootElement.setAttribute("time", "" + 0);
//			// set feasibility
//			rootElement.setAttribute("feasibility", "" + isFeasible());
//
//			for (Solution sol : getAll()) {
//				Element solution = doc.createElement("Solution");
//				rootElement.appendChild(solution);
//
//				// set cost
//				solution.setAttribute("cost", "" + 0);
//				// set generation time
//				solution.setAttribute("time", "" + 0);
//				// set generation iteration
//				solution.setAttribute("iteration", "" + 0);
//				// set feasibility
//				solution.setAttribute("feasibility", "" + false);
//
//				// create tier container element
//				Element tiers = doc.createElement("Tiers");
//				solution.appendChild(tiers);
//				
//				for (Tier t : sol.tiers.values()) {
//					// create the tier
//					Element tier = doc.createElement("Tier");
//					tiers.appendChild(tier);
//
//					// set id, name, provider name, service name, resource name,
//					// service type
//					tier.setAttribute("id", t.id);
//					tier.setAttribute("name", t.name);
//
//					tier.setAttribute("providerName", t.providerName);
//					tier.setAttribute("serviceName", t.serviceName);
//					tier.setAttribute("resourceName", t.resourceName);
//					tier.setAttribute("serviceType", t.serviceType);
//
//					for (int i = 0; i < 24; i++) {
//						// create the allocation element
//						Element hourAllocation = doc.createElement("HourAllocation");
//						tier.appendChild(hourAllocation);
//						hourAllocation.setAttribute("hour", "" + i);
//						hourAllocation.setAttribute("allocation", "" + t.machines[i].replicas);
//					}
//				}
//
//			}
//
//			TransformerFactory transformerFactory = TransformerFactory
//					.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			File file = filePath.toFile();
//			StreamResult result = new StreamResult(file);
//			logger.info("Exported in: " + file.getAbsolutePath());
//
//			// Output to console for testing
//			// StreamResult result = new StreamResult(System.out);
//
//			transformer.transform(source, result);
//
//		} catch (ParserConfigurationException | TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		exportAsExtension(filePath);
	}
	
	@Deprecated
	public void exportAsFileSolution(Path filePath) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("SolutionMultiResult");
			doc.appendChild(rootElement);

			// set cost
			rootElement.setAttribute("cost", "" + getCost());
			// set evaluationtime
			rootElement.setAttribute("time", "" + 0);
			// set feasibility
			rootElement.setAttribute("feasibility", "" + isFeasible());

			for (Solution sol : getAll()) {
				Element solution = doc.createElement("Solution");
				rootElement.appendChild(solution);

				// set cost
				solution.setAttribute("cost", "" + 0);
				// set generation time
				solution.setAttribute("time", "" + 0);
				// set generation iteration
				solution.setAttribute("iteration", "" + 0);
				// set feasibility
				solution.setAttribute("feasibility", "" + false);

				// create tier container element
				Element tiers = doc.createElement("Tiers");
				solution.appendChild(tiers);
				
				for (Tier t : sol.tiers.values()) {
					// create the tier
					Element tier = doc.createElement("Tier");
					tiers.appendChild(tier);

					// set id, name, provider name, service name, resource name,
					// service type
					tier.setAttribute("id", t.id);
					tier.setAttribute("name", t.name);

					tier.setAttribute("providerName", t.providerName);
					tier.setAttribute("serviceName", t.serviceName);
					tier.setAttribute("resourceName", t.resourceName);
					tier.setAttribute("serviceType", t.serviceType);

					for (int i = 0; i < 24; i++) {
						// create the allocation element
						Element hourAllocation = doc.createElement("HourAllocation");
						tier.appendChild(hourAllocation);
						hourAllocation.setAttribute("hour", "" + i);
						hourAllocation.setAttribute("allocation", "" + t.machines[i].replicas);
					}
				}

			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File file = filePath.toFile();
			StreamResult result = new StreamResult(file);
			logger.info("Exported in: " + file.getAbsolutePath());

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException | TransformerException e) {
			logger.error("Error while saving the file solution.", e);
		}
	}
	
	public ResourceModelExtension getAsExtension() {
		//Build the objects
		ObjectFactory factory = new ObjectFactory();
		ResourceModelExtension extension = factory.createResourceModelExtension();
		List<ResourceContainer> resourceContainers = extension.getResourceContainer();
		
		for (Solution s : getAll()) {
			ResourceModelExtension ext = s.getAsExtension();
			List<ResourceContainer> rcs = ext.getResourceContainer();
			
			for (ResourceContainer rc : rcs)
				resourceContainers.add(rc);
		}
		
		return extension;
	}
	
	/**
	 * Export the solution in the format of the extension used as input for space4cloud
	 */
	public void exportAsExtension(Path fileName){
		ResourceModelExtension extension = getAsExtension();
		//serialize them		
		try {			
			XMLHelper.serialize(extension, ResourceModelExtension.class, new FileOutputStream(fileName.toFile()));
		} catch (JAXBException e) {
			logger.error("The generated solution is not valid",e);
		} catch (FileNotFoundException e) {
			logger.error("Error exporting the solution",e);
		}
		
	}
	
}
