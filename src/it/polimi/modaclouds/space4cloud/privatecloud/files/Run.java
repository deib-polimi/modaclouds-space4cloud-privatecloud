package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Run {
	public boolean print(String file) {
//		StringWriter sw = new StringWriter();
//		PrintWriter out = new PrintWriter(sw);
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
		
			out.printf("cd %s;\n", Configuration.RUN_WORKING_DIRECTORY);
			out.println("reset;\n" +
					    "option log_file 'log.tmp';");
			out.printf("model %s;\n", Configuration.RUN_MODEL);
			out.printf("data %s;\n", Configuration.RUN_DATA);
			out.printf("option solver '%s';\n", Configuration.RUN_SOLVER);
			out.println("option show_stats 1;\n" +
						"option timelimit 720;\n" +
						"option cplex_options 'timing=1';\n" +
						"solve;\n" +
						"display X > rez.out;\n" +
						"display Y > rez.out;\n" +
						"display Z > rez.out;\n" +
						"display W > rez.out;\n" +
						"display ALPHA > rez.out;\n" +
						"display BETA > rez.out;"); //\n" +
//						"option log_file '';\n" +
//						"close rez.out;\n" +
//						"close log.tmp;");
			
			out.println("display {h in HOST, v in VM, t in TIME_INT: X[v,h,t] == 1}:\n X[v,h,t] > rez.out;");
			
			out.println("option log_file '';\n" +
						"close rez.out;\n" +
						"close log.tmp;");
			
			out.flush();
			out.close();
		} catch (Exception e) {
			return false;
		}
		
//		System.out.println(sw.toString());
		
		return true;
	}

	public static void print() {
		Run run = new Run();
		run.print(Configuration.RUN_FILE);
	}
}
