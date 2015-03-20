package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;

public abstract class Model {
	
	public abstract boolean print(String file);
	
	public static void print() {
		switch (Configuration.MATH_SOLVER) {
		case AMPL:
			ModelAMPL.print();
			break;
		case CMPL:
			ModelCMPL.print();
			break;
		}
	}
	
}
