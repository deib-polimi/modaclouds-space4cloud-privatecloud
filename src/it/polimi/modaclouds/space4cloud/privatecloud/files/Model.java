package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Model {
	
	public boolean print(String file) {
		
		try {
			Path p = Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.RUN_MODEL);
			InputStream is = this.getClass().getResourceAsStream(p.toString());
			
			if (is == null) {
				is = new FileInputStream(p.toFile());
			}
			
			Files.copy(is, Paths.get(file));
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
