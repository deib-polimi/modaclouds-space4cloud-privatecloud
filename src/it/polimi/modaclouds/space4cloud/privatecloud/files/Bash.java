package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bash {
	
	public boolean print(String file) {
		
		try {
			Path p = Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.DEFAULTS_BASH);
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
		Bash b = new Bash();
		b.print(Configuration.DEFAULTS_BASH);
	}
}
