package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class RunCMPL extends Run {

	@Override
	public boolean print(String file) {
//		StringWriter sw = new StringWriter();
//		PrintWriter out = new PrintWriter(sw);
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			String baseFile = ""; //new String(Files.readAllBytes(Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_FILE))); //, Charset.defaultCharset()); // StandardCharsets.UTF_8);
			
//			String baseFile = new String(Files.readAllBytes(Paths.get(this.getClass().getResource(Configuration.RUN_FILE).toURI())));
			
			Scanner sc = new Scanner(Configuration.getStream(Configuration.RUN_FILE_CMPL));
			
			while (sc.hasNextLine())
				baseFile += sc.nextLine() + "\n";
			
			sc.close();
			
			out.printf(baseFile,
					Configuration.RUN_WORKING_DIRECTORY,
					Configuration.RUN_CMPL_FOLDER,
					Configuration.RUN_MODEL_CMPL,
					Configuration.RUN_LOG_CMPL
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
		RunCMPL run = new RunCMPL();
		run.print(Configuration.RUN_FILE_CMPL);
	}
	
}
