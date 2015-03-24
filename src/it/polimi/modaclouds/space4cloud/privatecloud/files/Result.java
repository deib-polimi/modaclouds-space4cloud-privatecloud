package it.polimi.modaclouds.space4cloud.privatecloud.files;

import it.polimi.modaclouds.space4cloud.privatecloud.Configuration;
import it.polimi.modaclouds.space4cloud.privatecloud.Host;
import it.polimi.modaclouds.space4cloud.privatecloud.solution.SolutionMulti;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public abstract class Result {
	protected SolutionMulti solution;
	protected List<Host> hosts;
	private Path path;
	
	public Result(SolutionMulti solution, List<Host> hosts, Path path) {
		this.solution = solution;
		this.hosts = hosts;
		this.path = path;
	}
	
	public void parse(String file) {
		try {
			Scanner in = new Scanner(new FileReader(file));
			
			while (in.hasNextLine()) {
				String s = in.nextLine();
				match(s);
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<File> export() {
		ArrayList<File> files = new ArrayList<File>();
		
		Path p = Paths.get(path.toString(), "solution-public.xml");
		solution.exportLight(p);
		files.add(p.toFile());
		
		int i = 1;
		for (Host h : hosts) {
			p = Paths.get(path.toString(), "solution-private-h" + i++ + ".xml");
			h.allocatedSolutions.exportLight(p);
			files.add(p.toFile());
		}
		
		return files;
	}
	
	public static List<File> parse(SolutionMulti solution, List<Host> hosts, Path path) {
		List<File> f = null;
		switch (Configuration.MATH_SOLVER) {
		case AMPL:
			f = ResultAMPL.parse(solution, hosts, path);
			break;
		case CMPL:
			f = ResultCMPL.parse(solution, hosts, path);
			break;
		}
		return f;
	}
	
	public static List<File> printEmpty(SolutionMulti solution, List<Host> hosts, Path path) {
		List<File> f = null;
		switch (Configuration.MATH_SOLVER) {
		case AMPL:
			f = new ResultAMPL(solution, hosts, path).export();
			break;
		case CMPL:
			f = new ResultCMPL(solution, hosts, path).export();
			break;
		}
		return f;
	}
	
	protected HashMap<String, Integer> maxMachinesMap = new HashMap<String, Integer>();
	
	public abstract void match(String s);
}
