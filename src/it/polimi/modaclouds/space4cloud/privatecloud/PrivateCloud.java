package it.polimi.modaclouds.space4cloud.privatecloud;

import it.polimi.modaclouds.space4cloud.privatecloud.files.Bash;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Data;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Model;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Result;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Run;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.ssh.SshConnector;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateCloud {
	
	private static final Logger logger = LoggerFactory.getLogger(PrivateCloud.class);
	
	private SolutionMulti solution;
	private List<Host> hosts;
	
	public PrivateCloud(String configurationFile, String solutionFile) throws PrivateCloudException {
		try {
			Configuration.loadConfiguration(configurationFile);
		} catch (Exception e) {
			throw new PrivateCloudException("Error while loading the configuration file!", e);
		}
		
		if (SolutionMulti.isEmpty(new File(solutionFile))) {
			throw new PrivateCloudException("The solution file doesn't exist or is empty!");
		} else {
			solution = new SolutionMulti(new File(solutionFile));
			hosts = Host.getFromFile(new File(Configuration.PRIVATE_CLOUD_HOSTS));
			
			if (solution.size() == 0)
				throw new PrivateCloudException("Error with the solution!");
			if (hosts.size() == 0)
				throw new PrivateCloudException("Error with the hosts!");
		}
	}
	
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		
		DecimalFormat f = new DecimalFormat("00");
		
		return String.format("%d%s%s-%s%s%s",
				c.get(Calendar.YEAR),
				f.format(c.get(Calendar.MONTH) + 1),
				f.format(c.get(Calendar.DAY_OF_MONTH)),
				f.format(c.get(Calendar.HOUR_OF_DAY)),
				f.format(c.get(Calendar.MINUTE)),
				f.format(c.get(Calendar.SECOND))
				);
	}
	
	private List<File> solutions = null;
	
	public List<File> getSolutions(Path path) throws PrivateCloudException {
		if (solutions != null)
			return solutions;
		
		if (path == null)
			path = Paths.get(Configuration.PROJECT_BASE_FOLDER, Configuration.WORKING_DIRECTORY);
		
		if (Configuration.usesPaaS()) {
			logger.error("PaaS not supported at the moment.");
			solutions = Result.printEmpty(solution, hosts, path);
			return solutions;
		}
		
		Configuration.setWorkingSubDirectory(getDate());
		
		try {
			Data.print(solution, hosts);
			Run.print();
			Model.print();
			Bash.print();
		} catch (Exception e) {
			throw new PrivateCloudException("Error when creating the problem files.", e);
		}
		
		try {
			SshConnector.run();
		} catch (Exception e) {
			throw new PrivateCloudException("Error when sending or receiving file or when executing the script.", e);
		}
		
		try {
			solutions = Result.parse(solution, hosts, path);
		} catch (Exception e) {
			throw new PrivateCloudException("Error when parsing the solution.", e);
		}
		
		if (removeTempFiles)
			Configuration.deleteTempFiles();
		
		return solutions;
	}
	
	public List<File> getSolutions() throws PrivateCloudException {
		return getSolutions(null);
	}
	
	public static boolean removeTempFiles = true;

	public static List<File> perform(String configurationFile, String solutionFile, String basePath) throws PrivateCloudException {
		PrivateCloud pc = new PrivateCloud(configurationFile, solutionFile);
		
		Path path = null;
		if (basePath != null && basePath.length() > 0) {
			path = Paths.get(basePath);
			if (!path.toFile().exists())
				path = null;
		}
		
		return pc.getSolutions(path);
		
	}
	
	public static List<File> perform(String configurationFile, String solutionFile) throws PrivateCloudException {
		return perform(configurationFile, solutionFile, null);
	}
}
