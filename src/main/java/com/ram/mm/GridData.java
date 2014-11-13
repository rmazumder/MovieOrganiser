// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GridData.java

package com.ram.mm;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GridData
{

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

    @Expose
    String status;
    
    @Expose
    List records;
    
    @Expose
    int total;
    
    @Expose
    boolean isScannedData;
    
    @Expose
    boolean isDBData;
    
    @Expose
    public String error;
}
