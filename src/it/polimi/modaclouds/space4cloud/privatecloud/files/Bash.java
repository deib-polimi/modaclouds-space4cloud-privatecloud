package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bash {
	
	public boolean print(String file) {
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			String baseFile = ""; //new String(Files.readAllBytes(Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_FILE))); //, Charset.defaultCharset()); // StandardCharsets.UTF_8);
			
//			String baseFile = new String(Files.readAllBytes(Paths.get(this.getClass().getResource(Configuration.RUN_FILE).toURI())));
			
			Scanner sc = new Scanner(this.getClass().getResourceAsStream(Configuration.DEFAULTS_BASH));
			
			while (sc.hasNextLine())
				baseFile += sc.nextLine() + "\n";
			
			sc.close();
			
			out.printf(baseFile,
					Configuration.RUN_WORKING_DIRECTORY,
					Configuration.RUN_LOG,
					Configuration.RUN_RES,
					Configuration.RUN_AMPL_FOLDER,
					Configuration.RUN_FILE);
			
			out.flush();
			out.close();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static void print() {
		Bash b = new Bash();
		b.print(Configuration.DEFAULTS_BASH);
	}
}
