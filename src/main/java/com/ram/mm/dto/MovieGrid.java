
package com.ram.mm.dto;

import com.google.gson.annotations.Expose;




public class MovieGrid
{

    public MovieGrid()
    {
    }

    @Expose
    private  MyMovie myMovie;
    
    @Expose
    private  IMDBMovie imdbMovie;
    
	public MyMovie getMyMovie() {
		return myMovie;
	}
	public void setMyMovie(MyMovie myMovie) {
		this.myMovie = myMovie;
		
	}
	public IMDBMovie getImdbMovie() {
		return imdbMovie;
	}
	public void setImdbMovie(IMDBMovie imdbMovie) {
		this.imdbMovie = imdbMovie;
	}
    
    
}
