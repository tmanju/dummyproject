package com.thirdpillar.codify.loanpath.util;

import java.util.HashMap;
import java.util.Map;

public class ScoreFactorMapper {
	private static Map<String, Integer> equifaxMapper = new HashMap<String, Integer>();
	private static Map<String, Integer> experianMapper = new HashMap<String, Integer>();

	public static Map<String, Integer> getEquifaxMapper() {
		return equifaxMapper;
	}

	public static Map<String, Integer> getExperianMapper() {
		return experianMapper;
	}

	static {
		/**
		 * 
		 * Experian Mapper
		 * 
		 */
		experianMapper.put("202", 14);
		experianMapper.put("204", 14);
		experianMapper.put("207", 18);
		experianMapper.put("210", 12);
		experianMapper.put("211", 19);
		experianMapper.put("223", 12);
		experianMapper.put("224", 12);
		experianMapper.put("229", 12);
		experianMapper.put("232", 14);
		experianMapper.put("236", 20);
		experianMapper.put("239", 20);
		experianMapper.put("240", 12);
		experianMapper.put("241", 12);
		experianMapper.put("242", 16);
		experianMapper.put("243", 12);
		experianMapper.put("245", 12);
		experianMapper.put("246", 12);
		experianMapper.put("247", 14);
		experianMapper.put("251", 14);
		experianMapper.put("254", 32);
		experianMapper.put("255", 16);
		experianMapper.put("256", 16);
		experianMapper.put("257", 12);
		experianMapper.put("258", 12);
		experianMapper.put("259", 18);
		experianMapper.put("260", 16);
		experianMapper.put("263", 12);
		experianMapper.put("265", 12);
		experianMapper.put("266", 18);
		experianMapper.put("267", 12);
		experianMapper.put("268", 19);
		experianMapper.put("269", 14);
		experianMapper.put("270", 14);
		experianMapper.put("271", 14);
		experianMapper.put("273", 18);
		/**
		 * 
		 * EquiFax Mapper
		 * 
		 */
		equifaxMapper.put("C0025", 18);
		equifaxMapper.put("C0036", 18);
		equifaxMapper.put("C0358", 19);
		equifaxMapper.put("C0363", 19);
		equifaxMapper.put("C0394", 19);
		equifaxMapper.put("C0414", 19);
		equifaxMapper.put("C0954", 20);
		equifaxMapper.put("C0985", 12);
		equifaxMapper.put("C1084", 12);
		equifaxMapper.put("C1085", 12);
		equifaxMapper.put("C1151", 12);
		equifaxMapper.put("C1174", 12);
		equifaxMapper.put("C1175", 12);
		equifaxMapper.put("C1183", 14);
		equifaxMapper.put("C1191", 14);
		equifaxMapper.put("C1206", 24);
		equifaxMapper.put("C1232", 12);
		equifaxMapper.put("C1244", 12);
		equifaxMapper.put("C1254", 12);
		equifaxMapper.put("C1275", 12);
		equifaxMapper.put("C1288", 12);
		equifaxMapper.put("C1301", 12);
		equifaxMapper.put("C1330", 12);
		equifaxMapper.put("C1340", 12);
		equifaxMapper.put("C1341", 12);
		equifaxMapper.put("C1342", 12);
		equifaxMapper.put("C1375", 12);
		equifaxMapper.put("C1380", 12);
		equifaxMapper.put("C1415", 16);
		equifaxMapper.put("C1416", 16);
		equifaxMapper.put("C1417", 17);
		equifaxMapper.put("G0017", 18);
		equifaxMapper.put("G0073", 18);
		equifaxMapper.put("H0001", 15);
		equifaxMapper.put("H0509", 12);
		equifaxMapper.put("H0511", 12);
		equifaxMapper.put("H0705", 12);
		equifaxMapper.put("H1179", 14);
		equifaxMapper.put("H1187", 14);
		equifaxMapper.put("H1196", 14);
		
	}
}
