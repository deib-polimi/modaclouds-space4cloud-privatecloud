package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Data {
	private SolutionMulti solution;
	private List<Host> hosts;
	
	public Data(SolutionMulti solution, List<Host> hosts) {
		this.solution = solution;
		this.hosts = hosts;
	}
	
	private static DecimalFormat doubleFormatter() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat myFormatter = new DecimalFormat("#.000#######", otherSymbols);
		return myFormatter;
	}
	
	private static DecimalFormat intFormatter(int maxValue) {
		String pattern = "0";
	    while (maxValue >= 10) {
		    maxValue = maxValue / 10;
		    pattern += "0";
	    }
		
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		return myFormatter;
	}
	
	public boolean print(String file) {
//		StringWriter sw = new StringWriter();
//		PrintWriter out = new PrintWriter(sw);
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			DecimalFormat doubleFormatter = doubleFormatter();
		
			int i = 0, h = 0, v = 0, w = 0;
			
			DecimalFormat timeIntFormatter = intFormatter(24);
			out.print("set TIME_INT :=");
			for (i = 1; i <= 24; ++i)
				out.printf(" t%s", timeIntFormatter.format(i));
			out.println(";");
			
			DecimalFormat vmFormatter = intFormatter(solution.getTotalMachines());
			out.print("set VM :=");
			for (i = 1; i <= solution.getTotalMachines() ; ++i)
				out.printf(" v%s", vmFormatter.format(i));
			out.println(";");
			
			DecimalFormat hostFormatter = intFormatter(hosts.size());
			out.print("set HOST :=");
			for (i = 1; i <= hosts.size() ; ++i)
				out.printf(" h%s", hostFormatter.format(i));
			out.println(";");
			
			DecimalFormat tierFormatter = intFormatter(solution.getTotalTiers());
			out.print("set TIER :=");
			for (i = 1; i <= solution.getTotalTiers() ; ++i)
				out.printf(" r%s", tierFormatter.format(i));
			out.println(";");
			
			out.println();
			
			out.print("param RAMHost default 0 :=");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf("\nh%s %d", hostFormatter.format(i+1), hosts.get(i).ram);
			out.println(";");
			
			out.print("param CPUCoreHost default 0 :=");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf("\nh%s %d", hostFormatter.format(i+1), hosts.get(i).cpu_cores);
			out.println(";");
			
			out.print("param DensityHost default 0 :=");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf("\nh%s %s", hostFormatter.format(i+1), doubleFormatter.format(hosts.get(i).density));
			out.println(";");
			
			out.print("param CPUSpeedHost default 0 :=");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf("\nh%s %s", hostFormatter.format(i+1), doubleFormatter.format(hosts.get(i).cpu_speed));
			out.println(";");
			
			out.print("param StorageHost default 0 :=");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf("\nh%s %d", hostFormatter.format(i+1), hosts.get(i).storage);
			out.println(";");
			
			out.print("param CostHost default 0 :=");
			for (i = 0; i < hosts.size(); ++i) {
				for (h = 0; h < hosts.get(0).hourlyCosts.length; ++h)
					out.printf("\nh%s t%s %s", hostFormatter.format(i+1), timeIntFormatter.format(h+1), doubleFormatter.format(hosts.get(i).hourlyCosts[h]));
			}
			out.println(";");
			
			out.println();
			
			out.print("param RAMVm default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf("\nv%s %d", vmFormatter.format(v), t.machines[0].ram);
					}
				}
			}
			out.println(";");
			
			out.print("param CPUCoreVm default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf("\nv%s %d", vmFormatter.format(v), t.machines[0].cpu_cores);
					}
				}
			}
			out.println(";");
			
			out.print("param CPUSpeedVm default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf("\nv%s %s", vmFormatter.format(v), doubleFormatter.format(t.machines[0].cpu_speed));
					}
				}
			}
			out.println(";");
			
			out.print("param StorageVm default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf("\nv%s %d", vmFormatter.format(v), t.machines[0].storage);
					}
				}
			}
			out.println(";");
			
			out.print("param CostVm default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						for (h = 0; h < t.machines.length; ++h)
							out.printf("\nv%s t%s %s", vmFormatter.format(v), timeIntFormatter.format(h+1), doubleFormatter.format(t.machines[h].cost));
					}
				}
			}
			out.println(";");
			
			out.println();
			
			out.print("param ActivationValue default 0 :=");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						for (h = 0; h < t.machines.length; ++h)
							out.printf("\nv%s t%s %d", vmFormatter.format(v), timeIntFormatter.format(h+1), t.machines[h].replicas >= i ? 1 : 0);
					}
				}
			}
			out.println(";");
			
			out.print("param BelongsVm default 0 :=");
			v = 1;
			w = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf("\nv%s r%s %d", vmFormatter.format(v), tierFormatter.format(w), 1);
					}
					w++;
				}
			}
			out.println(";");
			
			// TODO: variabile rho (tier ratio)
			
			
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
//		System.out.println(sw.toString());
		
		return true;
	}

	public static void print(SolutionMulti solution, List<Host> hosts) {
		Data data = new Data(solution, hosts);
		data.print(Configuration.RUN_DATA);
	}
}
