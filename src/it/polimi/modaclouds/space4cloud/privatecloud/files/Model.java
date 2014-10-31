package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Model {
	
	public boolean print(String file) {
		
		try {			
			Files.copy(Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_MODEL), Paths.get(file));
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static void print() {
		Model m = new Model();
		m.print(Configuration.RUN_MODEL);
	}
}
