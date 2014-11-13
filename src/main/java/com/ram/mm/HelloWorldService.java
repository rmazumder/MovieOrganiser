// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HelloWorldService.java

package com.ram.mm;

import javax.ws.rs.core.Response;

public class HelloWorldService
{

    public HelloWorldService()
    {
    }

    public Response getMsg(String msg)
    {
        String output = (new StringBuilder("Jersey say : ")).append(msg).toString();
        return Response.status(200).entity(output).build();
    }
}
