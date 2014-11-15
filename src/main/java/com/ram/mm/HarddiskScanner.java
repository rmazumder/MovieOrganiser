// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HarddiskScanner.java

package com.ram.mm;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.ram.mm:
//            MMException

public class HarddiskScanner
{

    public HarddiskScanner()
    {
        mediaLocation = "";
        mediaExtension = "";
        movies = new HashMap();
        ignoredList = new TreeSet();
    }

    public HashMap scanForMovies()
        throws MMException
    {
        File mediaFile = new File(mediaLocation);
        if(!mediaFile.exists())
        {
            throw new MMException("Media Location doesnot exists");
        } else
        {
            listFiles(mediaFile);
            return movies;
        }
    }

    private void listFiles(File file)
    {
        if(file.isDirectory())
        {
            File afile[];
            int k = (afile = file.listFiles()).length;
            for(int j = 0; j < k; j++)
            {
                File f = afile[j];
                listFiles(f);
            }

        } else
        {
            boolean added = false;
            String as[];
            int i1 = (as = mediaExtension.split(",")).length;
            for(int l = 0; l < i1; l++)
            {
                String extension = as[l];
                if(file.getName().toLowerCase().endsWith(extension.toLowerCase()))
                {
                    movies.put(file.getAbsolutePath(), file.getName());
                    added = true;
                }
            }

            if(!added)
            {
                String extension = "";
                String fileName = file.getName();
                int i = fileName.lastIndexOf('.');
                if(i > 0)
                {
                    extension = fileName.substring(i + 1);
                    ignoredList.add(extension);
                }
            }
        }
    }

    public static void main(String r[])
        throws MMException
    {
        HarddiskScanner scan = new HarddiskScanner();
        scan.mediaExtension = ".avi,.mpg,.mkv";
        scan.mediaLocation = "D:/ruhul/m";
        HashMap movies = scan.scanForMovies();
        System.out.println((new StringBuilder(String.valueOf(movies.size()))).append(" found ").toString());
        System.out.println(scan.ignoredList);
        System.out.println(movies);
    }

    String mediaLocation;
    String mediaExtension;
    HashMap movies;
    Set ignoredList;
}
