

package com.ram.mm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ram.mm.db.DBUtils;
import com.ram.mm.dto.IMDBMovie;
import com.ram.mm.dto.MovieGrid;
import com.ram.mm.dto.MyMovie;


@Path("/mmapi")
public class MovieManager
{
	
	public static Properties appProperties = null;
	
	
	public MovieManager(){
		appProperties = new Properties();
		try {
			appProperties.load(new FileInputStream(new File("mm.properties")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			appProperties = new Properties();
			appProperties.put("media_ext", ".avi,.mpg,.mkv,.mov,.dat");
			appProperties.put("rt_api_key", "");
			appProperties.put("media_fileExtractor_regex1", "(.*?)(dvdrip|xvid| cd[0-9]|dvdscr|brrip|divx|[\\{\\(\\[]?[0-9]{4}).*");
			appProperties.put("media_fileExtractor_regex2","(.*?)\\(.*\\)(.*)");
			
		}
	}
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")
    public Response loadData()
    {
        GridData gData = new GridData();
		try {
			List movies = DBUtils.ListEntity("FROM MyMovie");
			MovieGrid grid;
			for (Iterator iterator = movies.iterator(); iterator.hasNext(); gData.addRecord(grid)) {
				MyMovie movie = (MyMovie) iterator.next();
				grid = new MovieGrid();
				movie.imdbMovie.base64Image = movie.imdbMovie.getBase64Image();
				grid.setMyMovie(movie);
			}

			gData.isDBData = true;
			gData.properties = appProperties;
			gData.setStatus("success");
		}catch(Exception e){
            gData.setStatus("failure");
            gData.error =  e.getMessage();

        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return Response.status(200).entity(gson.toJson(gData)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/scan")
    public Response scanData(@QueryParam("location") String  location)
    {
        HarddiskScanner scan = new HarddiskScanner();
        scan.mediaExtension = appProperties.getProperty("media_ext");
        scan.mediaLocation = location;
        HashMap<String,String> movies = new HashMap();
        try
        {
            movies = scan.scanForMovies();
        }
        catch(MMException e)
        {
            e.printStackTrace();
        }
        GridData gdata = new GridData();
        List<MyMovie> dbMovies = DBUtils.ListEntity("FROM MyMovie");
        for(Entry<String,String> entry : movies.entrySet())
        {
            MovieGrid movieGrid = new MovieGrid();
            if(movieExistInDB(entry.getKey(), dbMovies)){
            	continue;
            }
            MyMovie movie = new MyMovie();
            String movieName =  applyPatternMatcher((String)entry.getValue());
            movieName = movieName.replace(".", " ");
            movie.name = movieName;
            movie.files = (String)entry.getKey();
            movieGrid.setMyMovie(movie);
            gdata.addRecord(movieGrid);
        }
        gdata.ignoredExtensions =  scan.ignoredList;
        gdata.isScannedData = true;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        gdata.setStatus("success");
        return Response.status(200).entity(gson.toJson(gdata)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lookup")
    public Response lookup(@QueryParam("movieName") String movieName, @Context HttpServletRequest httpRequest)
    {
        GridData gdata = new GridData();

    	RestClient restClient = new RestClient();
    	IMDBMovie movie = null;
    	List<IMDBMovie> imdbMovies = DBUtils.ListEntity("FROM IMDBMovie as movies where movies.Title='"+movieName+"'");
    	if(imdbMovies.size() != 0){
    		System.out.println("movie fetched from cache database");
    		movie =  imdbMovies.get(0);
    		movie.Response = true;
    	} else {    	
    		System.out.println("movie not found in cache database. Fetching from IMDb database online");
    		movie = restClient.fetchIMDBData(movieName);
    	}
        if(movie !=null) {
			if (movie.Response) {
				MovieGrid grid = new MovieGrid();
				movie.base64Image =  movie.getBase64Image();
				httpRequest.getSession().setAttribute(movie.getTitle(), movie.base64Image);
				grid.setImdbMovie(movie);
				gdata.addRecord(grid);
		        gdata.setStatus("success");
				
			} else {
				gdata.status ="failure";
				gdata.error = movie.Error;
			}
        }
        gdata.isScannedData = true;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return Response.status(200).entity(gson.toJson(gdata)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update/{mode}")
    
    public Response updateData(@Context HttpServletRequest httpRequest, @PathParam("mode") String mode)
    {
        GridData gdata = new GridData();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try
        {
            InputStream is = httpRequest.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte buf[] = new byte[32];
            for(int r = 0; r >= 0;)
            {
                r = is.read(buf);
                if(r >= 0)
                    os.write(buf, 0, r);
            }

            String s = new String(os.toByteArray(), "UTF-8");
            String decoded = URLDecoder.decode(s, "UTF-8");
            JSONObject requestObj = (JSONObject)(new JSONParser()).parse(decoded);
            JSONObject mymovieJson = (JSONObject)requestObj.get("myMovie");
            MyMovie myMovie = (MyMovie)gson.fromJson(mymovieJson.toJSONString(), MyMovie.class);
            IMDBMovie imbdMovie =  myMovie.getImdbMovie();
            imbdMovie.base64Image = (String)httpRequest.getSession().getAttribute(imbdMovie.getTitle());
            if(mode.equals("update")){
            	List<IMDBMovie> imdbMovies = DBUtils.ListEntity("FROM IMDBMovie as movies where movies.Title='"+imbdMovie.getTitle()+"'");
            	if(imdbMovies.size() != 0) {
            		myMovie.imdbMovie =  imdbMovies.get(0);
            	}
            }
            DBUtils.saveEntity(myMovie);
            gdata = new GridData();
            gdata.status = "success";
        }
        catch(Exception e)
        {
            gdata.status = "failure";
            gdata.error = e.getMessage();
        }
        return Response.status(200).entity(gson.toJson(gdata)).build();
    }

 
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{name}")
    
    public Response delete(@PathParam("name") String name)
    {
    	 GridData gData = new GridData();
    	try {
			DBUtils.deleteMyMovie(name);
			gData.status = "success";
		} catch (MMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gData.status = "failure";
			gData.error = e.getMessage();
			
		}
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return Response.status(200).entity(gson.toJson(gData)).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/savesetting")
    
    public Response saveSetting(@Context HttpServletRequest httpRequest)
    {
        GridData gdata = new GridData();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try
        {
            InputStream is = httpRequest.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte buf[] = new byte[32];
            for(int r = 0; r >= 0;)
            {
                r = is.read(buf);
                if(r >= 0)
                    os.write(buf, 0, r);
            }

            String s = new String(os.toByteArray(), "UTF-8");
            String decoded = URLDecoder.decode(s, "UTF-8");
            System.out.println(decoded);
            JSONObject requestObj = (JSONObject)(new JSONParser()).parse(decoded);
            for ( Object key : requestObj.keySet()){
            	String value = (String)requestObj.get(key);
            	appProperties.put((String)key, value);
            	
            }
            
            appProperties.store(new FileOutputStream(new File("mm.properties")), "");
            
            
           
            gdata = new GridData();
            gdata.status = "success";
        }
        catch(Exception e)
        {
            gdata.status = "failure";
            gdata.error = e.getMessage();
        }
        return Response.status(200).entity(gson.toJson(gdata)).build();
    }
    
    private boolean movieExistInDB(String key, List<MyMovie> dbMovies) {
		for(MyMovie movie : dbMovies){
			String filesS = movie.getFiles();
			if(filesS!=null){
			String files[] = movie.getFiles().split(",");
			for (String file : files){
				if(file.equals(key)){
					return true;
				}
			}
			}
		}
		
		return false;
	}
    
    private String applyPatternMatcher(String data){
    	String mydata = data.toLowerCase();
		ArrayList<String> patterns = new ArrayList<String>();
		//patterns.add("(.*?)(dvdrip|xvid| cd[0-9]|dvdscr|brrip|divx|[\\{\\(\\[]?[0-9]{4}).*");
		//patterns.add("(.*?)\\(.*\\)(.*)");
		patterns.add(appProperties.getProperty("media_fileExtractor_regex1"));
		patterns.add(appProperties.getProperty("media_fileExtractor_regex2"));

		
		
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
