package com.ducksofts.ghosty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataFile {

	public static class Variable {
		public String name;
		public String var;
	}
	
	public static List<Variable> loadFile(String path) {
		List<Variable> list = new ArrayList<Variable>();
		BufferedReader br = new BufferedReader(new InputStreamReader(DataFile.class.getResourceAsStream(path)));
		try {
			String line;
			while((line = br.readLine()) != null) {
				String[] s = line.split("=");
				Variable v = new Variable();
				v.name = s[0];
				v.var = s[1];
				list.add(v);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
