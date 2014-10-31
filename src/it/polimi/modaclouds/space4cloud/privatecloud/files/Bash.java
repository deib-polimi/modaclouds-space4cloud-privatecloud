package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Bash {
	
	public boolean print(String file) {
		
		try {
			Files.copy(Paths.get(Configuration.DEFAULTS_FOLDER, Configuration.DEFAULTS_BASH), Paths.get(file));
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
