package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ModelAMPL extends Model {

	@Override
	public boolean print(String file) {
		
		try {
			Files.copy(Configuration.getStream(Configuration.RUN_MODEL), Paths.get(Configuration.LOCAL_TEMPORARY_FOLDER, file), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public static void print() {
		ModelAMPL m = new ModelAMPL();
		m.print(Configuration.RUN_MODEL);
	}
	
}
