package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public abstract class Data {
	protected SolutionMulti solution;
	protected List<Host> hosts;
	
	public Data(SolutionMulti solution, List<Host> hosts) {
		this.solution = solution;
		this.hosts = hosts;
	}
	
	protected static DecimalFormat doubleFormatter() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat myFormatter = new DecimalFormat("0.000#######", otherSymbols);
		return myFormatter;
	}
	
	protected static DecimalFormat intFormatter(int maxValue) {
		String pattern = "0";
	    while (maxValue >= 10) {
		    maxValue = maxValue / 10;
		    pattern += "0";
	    }
		
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		return myFormatter;
	}
	
	public abstract boolean print(String file);

	public static void print(SolutionMulti solution, List<Host> hosts) {
		switch (Configuration.MATH_SOLVER) {
		case AMPL:
			DataAMPL.print(solution, hosts);
			break;
		case CMPL:
			DataCMPL.print(solution, hosts);
			break;
		}
	}
	
}
