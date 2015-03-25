package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ResultCMPL extends Result {

	public ResultCMPL(SolutionMulti solution, List<Host> hosts, Path path) {
		super(solution, hosts, path);
	}

	public static List<File> parse(SolutionMulti solution, List<Host> hosts, Path path) {
		ResultCMPL result = new ResultCMPL(solution, hosts, path);
		result.parse(Configuration.RUN_RES_CMPL);
		return result.export();
	}
	
	@Override
	public void match(String s) {
		StringTokenizer st = new StringTokenizer(s, " ");
		
		if (st.countTokens() != 6)
			return;
		
		String name = st.nextToken();
		String params = name.substring(name.indexOf('[') + 1, name.length() - 1);
		
		String[] comps = params.split(",");
		
		int v = -1, h = -1, t = -1;
		
		for (String el : comps)
			switch (el.charAt(0)) {
			case 'v':
				v = Integer.parseInt(el.substring(1)) - 1;
				break;
			case 'h':
				h = Integer.parseInt(el.substring(1)) - 1;
				break;
			case 't':
				t = Integer.parseInt(el.substring(1)) - 1;
				break;
			}
		
		if (Pattern.matches("X\\[v[0-9]+,h[0-9]+,t[0-9]+\\]", name)) {
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
		} else if (Pattern.matches("W\\[v[0-9]+,t[0-9]+\\]", name)) {
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
