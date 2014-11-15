package com.ram.mm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;


public class Test {
	public static void main(String r[]) throws MMException{
		 HarddiskScanner scan = new HarddiskScanner();
	        scan.mediaExtension = ".avi,.mpg,.mkv";
	        scan.mediaLocation = "D:/ruhul/m";
	      HashMap<String, String> movies =   scan.scanForMovies();
	      for (Entry<String, String> entry: movies.entrySet()){
	    	  
	    	  System.out.println(apply(entry.getValue()).toLowerCase());
	      }
		
		//System.out.println(apply("ENEMY_OF_THE_STATE..DVDrip(vice)"));
	      
	}
	
	public static String apply(String data){
		String mydata = data.toLowerCase();
		ArrayList<String> patterns = new ArrayList<String>();
		patterns.add("(.*?)(dvdrip|xvid| cd[0-9]|dvdscr|brrip|divx|[\\{\\(\\[]?[0-9]{4}).*");
		patterns.add("(.*?)\\(.*\\)(.*)");
		
		mydata = FilenameUtils.removeExtension(mydata);
		
		
		for(String patternString : patterns) {
			if(mydata.equals("")) {
				break;
			}
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(mydata);
				if (matcher.find())
				{
					mydata =  matcher.group(1);
				}
		}
		
		if(!mydata.equals("")){
			mydata = mydata.replaceAll("_", " ").toLowerCase();
			mydata = mydata.replaceAll("~", " ").toLowerCase();
			mydata = mydata.replaceAll("-", " ").toLowerCase();
			mydata = mydata.replaceAll("\\.", " ").toLowerCase();
			return mydata;
		}
		else
			return data;
	}

}
