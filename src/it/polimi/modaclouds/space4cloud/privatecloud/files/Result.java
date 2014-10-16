package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Result {
	private SolutionMulti solution;
	private List<Host> hosts;
	
	public Result(SolutionMulti solution, List<Host> hosts) {
		this.solution = solution;
		this.hosts = hosts;
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
	
	public void export() {
		solution.exportLight(Paths.get("solution-public.xml"));
		int i = 1;
		for (Host h : hosts)
			h.allocatedSolutions.exportLight(Paths.get("solution-private-h" + i++ + ".xml"));
	}
	
	public static void parse(SolutionMulti solution, List<Host> hosts) {
		Result result = new Result(solution, hosts);
		result.parse("rez.out");
	}
	
	public void match(String s) {
		if (Pattern.matches("X\\['v[0-9]+','h[0-9]+','t[0-9]+'\\] = 1", s)) {
			String[] ss = s.split("'");
			
			int v = Integer.parseInt(ss[1].substring(1)) - 1;
			int h = Integer.parseInt(ss[3].substring(1)) - 1;
			int t = Integer.parseInt(ss[5].substring(1)) - 1;
			
			int i = 0;
			
			for (Solution sol : solution.getAll()) {
				for (Tier tier : sol.tiers.values()) {
					for (int x = 0; x < tier.getMaxMachines(); ++x, ++i) {
						if (v == i) {
							if (tier.machines[t].replicas <= 0)
								return;
							
							--tier.machines[t].replicas;
							
							Host host = hosts.get(h);
							host.addMachine(tier, t);
							
							return;
						}
					}
				}
			}
		}
	}
}
