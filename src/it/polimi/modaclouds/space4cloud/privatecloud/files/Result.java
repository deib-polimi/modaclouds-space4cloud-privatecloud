package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Result {
	private SolutionMulti solution;
	private List<Host> hosts;
	private Path path;
	
	public Result(SolutionMulti solution, List<Host> hosts, Path path) {
		this.solution = solution;
		this.hosts = hosts;
		this.path = path;
	}
	
	public void parse(String file) {
		try {
			Scanner in = new Scanner(new FileReader(file));
			
			while (in.hasNextLine()) {
				String s = in.nextLine();
				match(s);
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<File> export() {
		ArrayList<File> files = new ArrayList<File>();
		
		Path p = Paths.get(path.toString(), "solution-public.xml");
		solution.exportLight(p);
		files.add(p.toFile());
		
		int i = 1;
		for (Host h : hosts) {
			p = Paths.get(path.toString(), "solution-private-h" + i++ + ".xml");
			h.allocatedSolutions.exportLight(p);
			files.add(p.toFile());
		}
		
		return files;
	}
	
	public static List<File> parse(SolutionMulti solution, List<Host> hosts, Path path) {
		Result result = new Result(solution, hosts, path);
		result.parse("rez.out");
		return result.export();
	}
	
	private HashMap<String, Integer> maxMachinesMap = new HashMap<String, Integer>();
	
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
