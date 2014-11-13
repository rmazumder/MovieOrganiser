package com.ram.mm.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;  

import com.google.gson.annotations.Expose;

@Entity
@Table(name="IMDBMovie")
public class IMDBMovie implements MMEnity {

	@Expose
	@Id
	public String imdbID;
	
	@Expose
	public String Title;
	
	@Expose
	@Column(name="YearOf")
	public String Year;
	
	@Expose
	public String Rated;
	
	@Expose
	public String Released;
	
	@Expose
	public String Runtime;
	
	@Expose
	public String Genre;
	
	@Expose
	public String Director;
	
	@Expose
	public String Writer;
	
	@Expose
	public String Actors;
	
	@Expose
	@Column(name="Plot", length =2000)
	public String Plot;
	
	@Expose
	public String Language;
	
	@Expose
	public String Awards;
	
	@Expose
	public String Poster;
	
	@Expose
	public String Metascore;
	
	@Expose
	public String imdbRating;
	
	@Expose
	public String imdbVotes;
	
	@Expose
	public String Type;
	
	@Lob
	@Column(name="image",  columnDefinition="blob")
	public  byte[] image;
	
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Expose
	@Transient
	public Boolean Response;

	@Expose
	@Transient
	public String Error;
	
	@Expose
	@Transient
	public String base64Image;

	public IMDBMovie() {
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getRated() {
		return Rated;
	}

	public void setRated(String rated) {
		Rated = rated;
	}

	public String getReleased() {
		return Released;
	}

	public void setReleased(String released) {
		Released = released;
	}

	public String getRuntime() {
		return Runtime;
	}

	public void setRuntime(String runtime) {
		Runtime = runtime;
	}

	public String getGenre() {
		return Genre;
	}

	public void setGenre(String genre) {
		Genre = genre;
	}

	public String getDirector() {
		return Director;
	}

	public void setDirector(String director) {
		Director = director;
	}

	public String getWriter() {
		return Writer;
	}

	public void setWriter(String writer) {
		Writer = writer;
	}

	public String getActors() {
		return Actors;
	}

	public void setActors(String actors) {
		Actors = actors;
	}

	public String getPlot() {
		return Plot;
	}

	public void setPlot(String plot) {
		Plot = plot;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getAwards() {
		return Awards;
	}

	public void setAwards(String awards) {
		Awards = awards;
	}

	public String getPoster() {
		return Poster;
	}

	public void setPoster(String poster) {
		Poster = poster;
	}

	public String getMetascore() {
		return Metascore;
	}

	public void setMetascore(String metascore) {
		Metascore = metascore;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getImdbVotes() {
		return imdbVotes;
	}

	public void setImdbVotes(String imdbVotes) {
		this.imdbVotes = imdbVotes;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	
	

	public String getBase64Image() {
		if(this.getImage() != null)
			return new sun.misc.BASE64Encoder().encode(this.getImage());
		else
			return null;
	}

	
	public String toString() {
		return (new StringBuilder("IMDBMovie [Title=")).append(Title)
				.append(", Year=").append(Year).append(", Rated=")
				.append(Rated).append(", Released=").append(Released)
				.append(", Runtime=").append(Runtime).append(", Genre=")
				.append(Genre).append(", Director=").append(Director)
				.append(", Writer=").append(Writer).append(", Actors=")
				.append(Actors).append(", Plot=").append(Plot)
				.append(", Language=").append(Language).append(", Awards=")
				.append(Awards).append(", Poster=").append(Poster)
				.append(", Metascore=").append(Metascore)
				.append(", imdbRating=").append(imdbRating)
				.append(", imdbVotes=").append(imdbVotes).append(", imdbID=")
				.append(imdbID).append(", Type=").append(Type).append("]")
				.toString();
	}


	
	

}
