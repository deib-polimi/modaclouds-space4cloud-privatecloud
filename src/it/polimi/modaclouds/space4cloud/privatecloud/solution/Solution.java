package it.polimi.modaclouds.space4cloud.privatecloud.solution;

import java.util.HashMap;
import java.util.Map;

public class Solution {
	public String providerName;
	public Map<String, Tier> tiers;
	
	public Solution(String providerName) {
		this.providerName = providerName;
		tiers = new HashMap<String, Tier>();
	}
}
