package com.ram.mm.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="MyMovie")
public class MyMovie implements MMEnity {

	@Expose
	@Id
	@Column(name="name")
	public String name;
	
	
	@Column(name="rtID")
	public String rtID;
	
	@Expose
	@Column(name="watched")
	public boolean watched;
	
	@Expose
	@Column(name="ratings")
	public String ratings = "N/A";
	
	@Expose
	@Column(name="review", length = 1000)
	public String review;
	
	@Expose
	@Column(name="files", length = 1000)
	public String files;

	@Expose
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="imdbmovie")	
	public IMDBMovie imdbMovie;

	public MyMovie() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRtID() {
		return rtID;
	}

	public void setRtID(String rtID) {
		this.rtID = rtID;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public String getRatings() {
		return ratings;
	}

	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public IMDBMovie getImdbMovie() {
		return imdbMovie;
	}

	public void setImdbMovie(IMDBMovie imdbMovie) {
		this.imdbMovie = imdbMovie;
	}

	public String toString() {
		return (new StringBuilder("MyMovie [ name=")).append(name)
				.append(", rtID=").append(rtID).append(", watched=")
				.append(watched).append(", ratings=").append(ratings)
				.append(", review=").append(review).append(", files=")
				.append(files).append("]").toString();
	}

}
