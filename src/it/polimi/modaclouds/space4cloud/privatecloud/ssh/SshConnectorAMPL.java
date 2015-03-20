package it.polimi.modaclouds.space4cloud.privatecloud.ssh;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.PrivateCloud;

public class SshConnectorAMPL extends SshConnector {

	@Override
	public void execute() throws Exception {
		exec(String.format("mkdir -p %s", Configuration.RUN_WORKING_DIRECTORY));
		
		sendFileToWorkingDir(Configuration.RUN_DATA);
		sendFileToWorkingDir(Configuration.RUN_FILE);
		
		sendFileToWorkingDir(Configuration.DEFAULTS_BASH);
		sendFileToWorkingDir(Configuration.RUN_MODEL);
		
		exec(
				String.format("bash %s/%s",
						Configuration.RUN_WORKING_DIRECTORY,
						Configuration.DEFAULTS_BASH));

		receiveFileFromWorkingDir(Configuration.RUN_LOG);
		receiveFileFromWorkingDir(Configuration.RUN_RES);
		
		if (PrivateCloud.removeTempFiles)
			exec(String.format("rm -rf %s", Configuration.RUN_WORKING_DIRECTORY));
	}
	
	public static void run() throws Exception {
		SshConnectorAMPL ssh = new SshConnectorAMPL();
		ssh.execute();
	}
	
}
