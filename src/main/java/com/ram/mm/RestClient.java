package com.ram.mm;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ram.mm.db.DBUtils;
import com.ram.mm.dto.IMDBMovie;
import com.ram.mm.dto.MyMovie;
 
public class RestClient {
 
	String imdbURL = "http://www.omdbapi.com/?y=&plot=full&r=json&t=";
	String imdbURL_byID = "http://www.omdbapi.com/?plot=full&r=json&i=";
	public static String RT_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=[api_key]&page_limit=1&q=";
	
	public IMDBMovie fetchIMDBData(String movieName) {
		movieName =  movieName.replace(" ", "+");
		String url ;
		if(movieName.startsWith("tt")){
			url = imdbURL_byID + movieName;
		} else {
			url =  imdbURL + movieName;
		}
		String data = processURL(url);
		if(data == null) {
			return null;
		}
		
		try {
		  Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();  
		  
		   IMDBMovie movieObject = gson.fromJson(data, IMDBMovie.class);  
		   uploadImage(movieObject);
		   return movieObject;
		}catch (Exception e){
			
		}
		
		return null;
	} 
	
	
	
	private void uploadImage(IMDBMovie movie) {
		try {
		String imageURL = movie.getPoster();
		if(imageURL != null && !imageURL.equals("") && !imageURL.equals("N/A")){
		
			URL url = new URL(imageURL);
			 BufferedImage originalImage=ImageIO.read(url);
			 ByteArrayOutputStream baos=new ByteArrayOutputStream();
			 ImageIO.write(originalImage, "jpg", baos );
			 byte[] imageInByte=baos.toByteArray();
			 movie.setImage(imageInByte);
		
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	public String processURL(String urlString) {
		HttpURLConnection conn = null;
		StringBuffer data = new StringBuffer();
		try {

			URL url = new URL(urlString);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output = null;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				data.append(output);
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			conn.disconnect();
		}
		return data.toString();
	}
 
	
	public static void main(String r[]) throws Exception{
		
		MyMovie m = new MyMovie();
		m.setName("myname11");
		IMDBMovie mov = new IMDBMovie();
		mov.setTitle("My Ttitle");
		mov.imdbID = "sss";
		m.setImdbMovie(mov);
		
		DBUtils.saveEntity(m);
		
		
		
		/*
		IMDBMovie movie = c.fetchIMDBData("Mary Kom");
		System.out.println(movie);
		String s = new Base64().encodeAsString(movie.getImage());
		movie.base64Image  = s;
		DBUtils.saveEntity(movie);
		
		
		
		List<IMDBMovie> movs = DBUtils.ListEntity("FROM IMDBMovie as movies where movies.Title='Mary Kom'");
		for (IMDBMovie mov : movs){
			 String s1 =new Base64().encodeAsString(movie.getImage());
			// System.out.println(s1);
			 System.out.println(s.equals(s1));
		}*/
		
	/*	MyMovie mov = new MyMovie();
		mov.setName("ruhul");
		mov.setRatings("1111");
		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		System.out.println(gson.toJson(mov, MyMovie.class));*/
	}
}