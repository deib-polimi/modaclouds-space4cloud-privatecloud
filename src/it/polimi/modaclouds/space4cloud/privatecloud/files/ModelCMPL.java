package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class ModelCMPL extends Model {

	@Override
	public boolean print(String file) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			String baseFile = "";
			
			Scanner sc = new Scanner(Configuration.getStream(Configuration.RUN_MODEL_CMPL));
			
			while (sc.hasNextLine())
				baseFile += sc.nextLine() + "\n";
			
			sc.close();
			
			int threads = Configuration.CMPL_THREADS;
			if (Configuration.isRunningLocally()) {
				threads = Runtime.getRuntime().availableProcessors() - 1;
				if (threads <= 0)
					threads = 1;
			}
			
			out.print(String.format(baseFile,
					Configuration.RUN_SOLVER_CMPL,
					Configuration.RUN_RES_CMPL,
					Configuration.RUN_DATA_CMPL,
					threads));
			
			out.flush();
			out.close();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void print() {
		ModelCMPL m = new ModelCMPL();
		m.print(Configuration.RUN_MODEL_CMPL);
	}
	
}
