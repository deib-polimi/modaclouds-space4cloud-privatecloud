package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class ResultAMPL extends Result {

	public ResultAMPL(SolutionMulti solution, List<Host> hosts, Path path) {
		super(solution, hosts, path);
	}

	public static List<File> parse(SolutionMulti solution, List<Host> hosts, Path path) {
		ResultAMPL result = new ResultAMPL(solution, hosts, path);
		result.parse(Paths.get(Configuration.LOCAL_TEMPORARY_FOLDER, Configuration.RUN_RES).toString());
		return result.export();
	}
	
	@Override
	public void match(String s) {
		if (Pattern.matches("X\\['v[0-9]+','h[0-9]+','t[0-9]+'\\] = 1", s)) {
			String[] ss = s.split("'");
			
			int v = Integer.parseInt(ss[1].substring(1)) - 1;
			int h = Integer.parseInt(ss[3].substring(1)) - 1;
			int t = Integer.parseInt(ss[5].substring(1)) - 1;
			
			int i = 0;
			
			for (Solution sol : solution.getAll()) {
				for (Tier tier : sol.tiers.values()) {
					Integer maxMachines = maxMachinesMap.get(tier.id + "@" + sol.providerName);
					if (maxMachines == null) {
						maxMachines = tier.getMaxMachines();
						maxMachinesMap.put(tier.id + "@" + sol.providerName, maxMachines);
					}
					for (int x = 0; x < maxMachines; ++x, ++i) {
						if (v == i) {
							if (tier.machines[t].replicas <= 0)
								return;
							
							tier.machines[t].replicas--;
							
							Host host = hosts.get(h);
							host.addMachine(tier, t);
							
							return;
						}
					}
				}
			}
		} else if (Pattern.matches("W\\['v[0-9]+','t[0-9]+'\\] = 1", s)) {
			String[] ss = s.split("'");
			
			int v = Integer.parseInt(ss[1].substring(1)) - 1;
			int t = Integer.parseInt(ss[3].substring(1)) - 1;
			
			int i = 0;
			
			for (Solution sol : solution.getAll()) {
				for (Tier tier : sol.tiers.values()) {
					Integer maxMachines = maxMachinesMap.get(tier.id + "@" + sol.providerName);
					if (maxMachines == null) {
						maxMachines = tier.getMaxMachines();
						maxMachinesMap.put(tier.id + "@" + sol.providerName, maxMachines);
					}
					for (int x = 0; x < maxMachines; ++x, ++i) {
						if (v == i) {
							tier.machines[t].replicas++;
							
							return;
						}
					}
				}
			}
		}
	}
	
}
