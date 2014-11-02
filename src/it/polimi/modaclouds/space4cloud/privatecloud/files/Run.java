package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Run {
	
//	public static final String BASE_FILE =
//			"cd %s;\n" +
//			"reset;\n" +
//			"option log_file 'log.tmp';\n" +
//			"\n" +
//			"model %s;\n" +
//			"data %s;\n" +
//			"option solver '%s';\n" +
//			"option show_stats 1;\n" +
//			"option timelimit 720;\n" +
//			"option cplex_options 'timing=1';\n" +
//			"printf \"SOLVING CLOUD BURSTING PROBLEM\\n\";\n" +
//			"solve;\n" +
//			"\n" +
//			"if (match (solve_message, \"no feasible\") > 0)|| (match (solve_message, \"infeasible\") > 0)  then {\n" +
//			"printf\"Parameters are too stringent.  No feasible solution for the problem\\n\";\n" +
//			"printf\"Analysis termination\\n\";\n" +
//			"}\n" +
//			"else printf(\"Solution Feasible!\\n\");\n" +
//			"\n" +
//			"display X > rez.out;\n" +
//			"display Y > rez.out;\n" +
//			"display Z > rez.out;\n" +
//			"display W > rez.out;\n" +
//			"display ALPHA > rez.out;\n" +
//			"display BETA > rez.out;\n" +
//			"display ActivationValue > rez.out;\n" +
//			"\n" +
//			"display {h in HOST, v in VM, t in TIME_INT: X[v,h,t] == 1}:\n" +
//			"	X[v,h,t] > rez.out;\n" +
//			"\n" +
//			"option log_file '';\n" +
//			"close rez.out;\n" +
//			"close log.tmp;";
	
	
	public boolean print(String file) {
//		StringWriter sw = new StringWriter();
//		PrintWriter out = new PrintWriter(sw);
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			String baseFile = ""; //new String(Files.readAllBytes(Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_FILE))); //, Charset.defaultCharset()); // StandardCharsets.UTF_8);
			
			Path p = Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_FILE);
			InputStream is = this.getClass().getResourceAsStream(p.toString());
			
			if (is == null) {
				is = new FileInputStream(p.toFile());
			}
			
			Scanner sc = new Scanner(is);
			
			while (sc.hasNextLine())
				baseFile += sc.nextLine() + "\n";
			
			sc.close();
			
			out.printf(baseFile,
					Configuration.RUN_WORKING_DIRECTORY,
					Configuration.RUN_MODEL,
					Configuration.RUN_DATA,
					Configuration.RUN_SOLVER
					);
			
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
