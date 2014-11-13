// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RottemTomatoesMovie.java

package com.ram.mm.dto;

import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RottemTomatoesMovie
{

    public RottemTomatoesMovie(String jsonString)
        throws ParseException
    {
        JSONObject jsonObject = (JSONObject)(new JSONParser()).parse(jsonString);
        JSONArray array = (JSONArray)jsonObject.get("movies");
        JSONObject movie = (JSONObject)array.get(0);
        id = Integer.parseInt((String)movie.get("id"));
        title = (String)movie.get("title");
        year = Integer.parseInt((String)movie.get("year"));
        genres = (String)movie.get("genres");
        title = (String)movie.get("title");
        title = (String)movie.get("title");
        title = (String)movie.get("title");
    }

    public static void main(String r[])
        throws IOException, ParseException
    {
        BufferedReader br = new BufferedReader(new FileReader("D:\\MyProjects\\Personal_projects\\MovieManager\\rt1.json"));
        JSONObject jsonObject = (JSONObject)(new JSONParser()).parse(br);
        JSONArray array = (JSONArray)jsonObject.get("movies");
        JSONObject movie = (JSONObject)array.get(0);
        System.out.println(movie.get("id"));
    }

    public int id;
    public String title;
    public int year;
    public String genres;
    public String mpaaRating;
    public int runtime;
    public String criticsConsensus;
    public String releaseDates;
    public String audience_rating;
    public String criticsRating;
    public String synopsis;
    public String posters;
    public String abridgedCast;
    public String abridgedDirectors;
    public String studio;
    public String imdbID;
    public String similar;
}
