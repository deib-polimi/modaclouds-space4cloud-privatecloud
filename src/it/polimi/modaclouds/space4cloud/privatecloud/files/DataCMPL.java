package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Solution;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.Tier;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

public class DataCMPL extends Data {

	public DataCMPL(SolutionMulti solution, List<Host> hosts) {
		super(solution, hosts);
	}

	@Override
	public boolean print(String file) {
//		StringWriter sw = new StringWriter();
//		PrintWriter out = new PrintWriter(sw);
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			DecimalFormat doubleFormatter = doubleFormatter();
		
			int i = 0, h = 0, v = 0, w = 0;
			
			DecimalFormat timeIntFormatter = intFormatter(24);
			out.print("%TIME_INT set <");
			for (i = 1; i <= 24; ++i)
				out.printf(" t%s", timeIntFormatter.format(i));
			out.println(" >");
			
			DecimalFormat vmFormatter = intFormatter(solution.getTotalMachines());
			out.print("%VM set <");
			for (i = 1; i <= solution.getTotalMachines() ; ++i)
				out.printf(" v%s", vmFormatter.format(i));
			out.println(" >");
			
			DecimalFormat hostFormatter = intFormatter(hosts.size());
			out.print("%HOST set <");
			for (i = 1; i <= hosts.size() ; ++i)
				out.printf(" h%s", hostFormatter.format(i));
			out.println(" >");
			
			DecimalFormat tierFormatter = intFormatter(solution.getTotalTiers());
			out.print("%TIER <");
			for (i = 1; i <= solution.getTotalTiers() ; ++i)
				out.printf(" r%s", tierFormatter.format(i));
			out.println(" >");
			
			out.println();
			
			out.print("%RAMHost[HOST] default 0 <");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf(" %d", hosts.get(i).ram);
			out.println(" >");
			
			out.print("%CPUCoreHost[HOST] default 0 <");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf(" %d", hosts.get(i).cpu_cores);
			out.println(" >");
			
			out.print("%DensityHost[HOST] default 0 <");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf(" %s", doubleFormatter.format(hosts.get(i).density));
			out.println(" >");
			
			out.print("%CPUSpeedHost[HOST] default 0 <");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf(" %s", doubleFormatter.format(hosts.get(i).cpu_speed));
			out.println(" >");
			
			out.print("%StorageHost[HOST] default 0 <");
			for (i = 0; i < hosts.size() ; ++i)
				out.printf(" %d", hosts.get(i).storage);
			out.println(" >");
			
			out.print("%CostHost[HOST,TIME_INT] default 0 <");
			for (i = 0; i < hosts.size(); ++i) {
				for (h = 0; h < hosts.get(0).hourlyCosts.length; ++h)
					out.printf(" %s", doubleFormatter.format(hosts.get(i).hourlyCosts[h]));
			}
			out.println(" >");
			
			out.println();
			
			out.print("%RAMVm[VM] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf(" %d", t.machines[0].ram);
					}
				}
			}
			out.println(" >");
			
			out.print("%CPUCoreVm[VM] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf(" %d", t.machines[0].cpu_cores);
					}
				}
			}
			out.println(" >");
			
			out.print("%CPUSpeedVm[VM] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf(" %s", doubleFormatter.format(t.machines[0].cpu_speed));
					}
				}
			}
			out.println(" >");
			
			out.print("%StorageVm[VM] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf(" %d", t.machines[0].storage);
					}
				}
			}
			out.println(" >");
			
			out.print("%CostVm[VM,TIME_INT] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						for (h = 0; h < t.machines.length; ++h)
							out.printf(" %s", doubleFormatter.format(t.machines[h].cost));
					}
				}
			}
			out.println(" >");
			
			out.println();
			
			out.print("%ActivationValue[VM,TIME_INT] default 0 <");
			v = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						for (h = 0; h < t.machines.length; ++h)
							out.printf(" %d", t.machines[h].replicas >= i ? 1 : 0);
					}
				}
			}
			out.println(" >");
			
			out.print("%BelongsVm[VM,TIER] default 0 <");
			v = 1;
			w = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t : s.tiers.values()) {
					for (i = 1; i <= t.getMaxMachines(); ++i, ++v) {
						out.printf(" %d", tierFormatter.format(w), 1);
					}
					w++;
				}
			}
			out.println(" >");
			
			out.print("param TierRatio default 0 :=");
			int w1 = 1, w2 = 1;
			for (Solution s : solution.getAll()) {
				for (Tier t1 : s.tiers.values()) {
					w2 = 1;
					for (Solution s2 : solution.getAll()) {
						for (Tier t2 : s2.tiers.values()) {
							double ratio;
							for (h = 0; h < t1.machines.length; ++h) {
								ratio = 0;
								if (w1 == w2)
									ratio = 1;
								else if (t1.providerName.equals(t2.providerName)) {
									ratio = ((double)t2.machines[h].replicas) / t1.machines[h].replicas;
								}
								
								int iRatio = (int)Math.round(ratio);
								if (iRatio == 0)
									iRatio = 1;
								
//								out.printf("\nr%s r%s t%s %s", tierFormatter.format(w1), tierFormatter.format(w2), timeIntFormatter.format(h+1), doubleFormatter.format(ratio));
								out.printf("\nr%s r%s t%s %d", tierFormatter.format(w1), tierFormatter.format(w2), timeIntFormatter.format(h+1), iRatio);
							}
							w2++;
						}
					}
					w1++;
				}
			}
			out.println(";");
			
			
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
		DataCMPL data = new DataCMPL(solution, hosts);
		data.print(Configuration.RUN_DATA_CMPL);
	}
	
}
