package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

public abstract class Bash {
	
	public abstract boolean print(String file);

	public static void print() {
		switch (Configuration.MATH_SOLVER) {
		case AMPL:
			BashAMPL.print();
			break;
		case CMPL:
			BashCMPL.print();
			break;
		}
	}
	
}
