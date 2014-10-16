package it.polimi.modaclouds.space4cloud.privatecloud;

import it.polimi.modaclouds.space4cloud.privatecloud.files.Data;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Result;
import it.polimi.modaclouds.space4cloud.privatecloud.files.Run;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.ssh.SshConnector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	
	public void compute() {
		Data.print(solution, hosts);
		Run.print();
		
		SshConnector.run();
		
		Result.parse(solution, hosts);
		
//		cleanFiles();
	}
	
	public void cleanFiles() {
		try {
			Files.deleteIfExists(Paths.get(Configuration.RUN_FILE));
			Files.deleteIfExists(Paths.get(Configuration.RUN_DATA));
			Files.deleteIfExists(Paths.get("log.tmp"));
			Files.deleteIfExists(Paths.get("rez.out"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getPrivateSolution() {
		File f = null;
		return f;
	}

	public static File perform(String configurationFile, String solutionFile) {
		PrivateCloud pc = new PrivateCloud(configurationFile, solutionFile);
		
		pc.compute();
		
		return pc.getPrivateSolution();
	}
}
