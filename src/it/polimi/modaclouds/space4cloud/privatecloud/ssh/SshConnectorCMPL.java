package it.polimi.modaclouds.space4cloud.privatecloud.ssh;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.PrivateCloud;

public class SshConnectorCMPL extends SshConnector {

	@Override
	public void execute() throws Exception {
		exec(String.format("mkdir -p %s", Configuration.RUN_WORKING_DIRECTORY));
		
		sendFileToWorkingDir(Configuration.RUN_DATA_CMPL);
		sendFileToWorkingDir(Configuration.RUN_FILE_CMPL);
		
		sendFileToWorkingDir(Configuration.DEFAULTS_BASH_CMPL);
		sendFileToWorkingDir(Configuration.RUN_MODEL_CMPL);
		
		exec(
				String.format("bash %s/%s",
						Configuration.RUN_WORKING_DIRECTORY,
						Configuration.DEFAULTS_BASH_CMPL));

		receiveFileFromWorkingDir(Configuration.RUN_LOG_CMPL);
		receiveFileFromWorkingDir(Configuration.RUN_RES_CMPL);
		
		if (PrivateCloud.removeTempFiles)
			exec(String.format("rm -rf %s", Configuration.RUN_WORKING_DIRECTORY));
	}
	
	public static void run() throws Exception {
		SshConnectorCMPL ssh = new SshConnectorCMPL();
		ssh.execute();
	}
	
}
