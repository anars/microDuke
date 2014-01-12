/**
 * microDuke - Java Based Micro Web Server
 * Copyright (c) 2012 - 2014 Anar Software LLC. < http://anars.com >
 * 
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program.  If not, see < http://www.gnu.org/licenses/ >
 * 
 */
package com.anars.microduke;

import java.io.File;

import java.net.InetAddress;
import java.net.ServerSocket;

import java.util.Calendar;
import java.util.Date;

/**
 * Listener class.
 *
 * @author Kay Anar
 * @version 1.0
 *
 */
public class Listener
{
  public static void main(String[] args)
  {
    int listenPort = 0;
    InetAddress bindAddress = null;
    File rootPath = new File(System.getProperty("user.dir"));
    for (int index = 0; index < args.length; index++)
      {
        String values[] = args[index].split("=");
        if (values[0].startsWith("-port"))
          {
            try
              {
                listenPort = Integer.parseInt(values[1]);
                if (listenPort < 0 || listenPort > 65535)
                  errorExit("-port value must be between 1 and 65535", -1);
              }
            catch (Exception exception)
              {
                errorExit("Invalid -port value", -2);
              }
          }
        else if (values[0].startsWith("-address"))
          {
            try
              {
                bindAddress = InetAddress.getByName(values[1]);
              }
            catch (Exception exception)
              {
                errorExit("Invalid -address value", -3);
              }
          }
        else if (values[0].startsWith("-path"))
          {
            try
              {
                rootPath = new File(values[1]);
                if (!rootPath.exists())
                  errorExit("Invalid -path value, \"" + rootPath.toString() + "\" not exists", -4);
              }
            catch (Exception exception)
              {
                errorExit("Invalid -path value", -5);
              }
          }
        else if (values[0].startsWith("-help"))
          {
            System.out.println("\nmicroDuke version 1.0\n" +
                "Copyright (c) " + Calendar.getInstance().get(Calendar.YEAR) + " Anar Software LLC. < http://anars.com >\n\n" +
                "This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n\n" +
                "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n" +
                "See the GNU General Public License for more details at http://www.gnu.org/licenses\n\n" +
                "USAGE:\n" +
                "\tjava -jar microDuke.jar [OPTIONS]...\n\n" +
                "DESCRIPTION:\n" +
                "\tAll parameters are optional.\n\n" +
                "-port=[NUMBER]\n" +
                "\tThe TCP port on which microDuke should listen for HTTP requests. The number must be between 1 and 65535, inclusive. Default, any free local TCP port.\n" +
                "\tE.g. -port=8080\n\n" +
                "-path=[PATH]\n" +
                "\tThe directory (in the real filesystem) from which microDuke will be serving most of its files. Default, current working directory.\n" +
                "\tE.g. -path=/var/local/wwww/\n\n" +
                "-address=[INET-ADDRESS]\n" +
                "\tLocal IP address or hostname which microDuke will only accept connect requests from. Default, any/all local addresses.\n" +
                "\tE.g. -address=192.168.1.100\n\n" +
                "-help\n" +
                "\tDisplays this help\n");
            System.exit(0);
          }
        else
          errorExit("Unknown parameter \"" + args[index] + "\"", -6);
      }
    ServerSocket serverSocket = null;
    try
      {
        serverSocket = new ServerSocket(listenPort, 0, bindAddress);
      }
    catch (Exception exception)
      {
        errorExit("Unable to bind socket - " + exception.getMessage(), -7);
      }
    System.out.println("microDuke stated at http://" + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort() + " in " + rootPath);
    while (true)
      {
        try
          {
            (new Handler(serverSocket.accept(), rootPath)).start();
          }
        catch (Exception exception)
          {
            System.err.println(exception.getMessage() + " [" + (new Date()) + "]");
          }
      }
  }

  private static void errorExit(String message, int errorCode)
  {
    System.err.println(message + ". Please type -help for details.");
    System.exit(errorCode);
  }
}
