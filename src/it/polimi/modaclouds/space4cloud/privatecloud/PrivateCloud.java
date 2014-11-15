package it.polimi.modaclouds.space4cloud.privatecloud;

import it.polimi.modaclouds.space4cloud.privatecloud.files.Bash;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Data;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Model;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Result;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Run;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.ssh.SshConnector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class PrivateCloud {
	
	private SolutionMulti solution;
	private List<Host> hosts;
	
	public PrivateCloud(String configurationFile, String solutionFile) {
		try {
			Configuration.loadConfiguration(configurationFile);
			
			if (SolutionMulti.isEmpty(new File(solutionFile))) {
				throw new Exception("The solution file doesn't exist or is empty!");
			} else {
				solution = new SolutionMulti(new File(solutionFile));
				hosts = Host.getFromFile(new File(Configuration.PRIVATE_CLOUD_HOSTS));
				
				if (solution.size() == 0)
					throw new Exception("Error with the solution!");
				if (hosts.size() == 0)
					throw new Exception("Error with the hosts!");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public List<File> getSolutions(Path path) {
		if (solutions != null)
			return solutions;
		
		Configuration.RUN_WORKING_DIRECTORY += "/" + getDate();
		
		Data.print(solution, hosts);
		Run.print();
		Model.print();
		Bash.print();
		
		SshConnector.run();
		
		if (path == null)
			path = Paths.get(Configuration.PROJECT_BASE_FOLDER, Configuration.WORKING_DIRECTORY);
		
		solutions = Result.parse(solution, hosts, path);
		
		if (removeTempFiles)
			cleanFiles();
		
		return solutions;
	}
	
	public List<File> getSolutions() {
		return getSolutions(null);
	}
	
	public static boolean removeTempFiles = true;
	
	public void cleanFiles() {
		try {
			Files.deleteIfExists(Paths.get(Configuration.RUN_FILE));
			Files.deleteIfExists(Paths.get(Configuration.RUN_DATA));
			Files.deleteIfExists(Paths.get(Configuration.DEFAULTS_BASH));
			Files.deleteIfExists(Paths.get(Configuration.RUN_MODEL));
			Files.deleteIfExists(Paths.get(Configuration.RUN_LOG));
			Files.deleteIfExists(Paths.get(Configuration.RUN_RES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<File> perform(String configurationFile, String solutionFile, String basePath) {
		PrivateCloud pc = new PrivateCloud(configurationFile, solutionFile);
		
		Path path = null;
		if (basePath != null && basePath.length() > 0) {
			path = Paths.get(basePath);
			if (!path.toFile().exists())
				path = null;
		}
		
		return pc.getSolutions(path);
		
	}
	
	public static List<File> perform(String configurationFile, String solutionFile) {
		return perform(configurationFile, solutionFile, null);
	}
}
