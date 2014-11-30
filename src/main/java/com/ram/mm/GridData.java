
package com.ram.mm;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class GridData
{
	@Expose
    String status;
    
    @Expose
    List records;
    
    @Expose
    int total;
    
    @Expose
    boolean isScannedData;
    
    @Expose
    boolean isImportedData = false;
    
    @Expose
    boolean isDBData;
    
    @Expose
    public String error;
    
    @Expose
    public Properties properties;

    @Expose
	public Set ignoredExtensions;
    
    @Expose
    String dataLabel =  System.getProperty("user.name");
    
    public GridData()
    {
        records = new ArrayList();
        isScannedData = false;
        isDBData = false;
        
    }

    public void setRecords(List movieRecords)
    {
        records = movieRecords;
        total = records.size();
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void addRecord(Object movie)
    {
        records.add(movie);
        total = records.size();
    }

   
}
