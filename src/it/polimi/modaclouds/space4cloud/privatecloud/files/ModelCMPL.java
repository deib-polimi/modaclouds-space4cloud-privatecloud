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
			
			Scanner sc = new Scanner(this.getClass().getResourceAsStream("/" + Configuration.RUN_MODEL_CMPL));
			
			while (sc.hasNextLine())
				baseFile += sc.nextLine() + "\n";
			
			sc.close();
			
			out.print(String.format(baseFile,
					Configuration.RUN_SOLVER_CMPL,
					Configuration.RUN_RES_CMPL,
					Configuration.RUN_DATA_CMPL,
					Configuration.CMPL_THREADS));
			
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
