/**
 * microDuke - Java Based Micro Web Server
 * Copyright (c) 2012 Anar Software LLC. < http://anars.com >
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.Socket;
import java.net.URL;

import java.util.Date;

/**
 * Handler class.
 *
 * @author Anar Software LLC
 * @version 1.0
 *
 */
public class Handler
  extends Thread
{
  private Socket _socket;
  private DataOutputStream _dataOutputStream;
  private BufferedReader _bufferedReader;
  private File _rootPath;

  Handler(Socket socket, File rootPath)
    throws Exception
  {
    super();
    _socket = socket;
    _rootPath = rootPath;
    try
      {
        _dataOutputStream = new DataOutputStream(socket.getOutputStream());
        _bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      }
    catch (Exception exception)
      {
        closeConnection();
        throw new Exception(exception);
      }
  }

  public void run()
  {
    try
      {
        int httpCode = 200;
        long responseLength = 0;
        File requestedFile = null;
        String mimeType = "application/octet-stream";
        String requestString = _bufferedReader.readLine();
        String requestPieces[] = requestString.split("\\s+");
        while (!_bufferedReader.readLine().equals(""))
          ;
        if (requestPieces.length != 3)
          httpCode = 400;
        else if (!requestPieces[0].equalsIgnoreCase("get"))
          httpCode = 501;
        else
          {
            requestedFile = new File(_rootPath, (new File("/", requestPieces[1])).getCanonicalPath());
            if (requestedFile.isDirectory())
              requestedFile = new File(requestedFile, "index.html");
            if (!requestedFile.exists())
              httpCode = 404;
            else if (!requestedFile.canRead())
              httpCode = 403;
            else
              {
                responseLength = requestedFile.length();
                try
                  {
                    mimeType = ((new URL("file://" + requestedFile.toString())).openConnection()).getContentType();
                  }
                catch (Exception exception)
                  {
                    ;
                  }
              }
          }
        String httpCodeText = httpCode + " ";
        switch (httpCode)
          {
            case 200:
              httpCodeText += "OK";
              break;
            case 400:
              httpCodeText += "Bad Request";
              break;
            case 403:
              httpCodeText += "Forbidden";
              break;
            case 404:
              httpCodeText += "Not Found";
              break;
            default:
              httpCodeText = "501 Not Implemented";
          }
        System.out.println(_socket.getInetAddress() + " - [" + (new Date()) + "] \"" + requestString + "\" " + httpCode + " " + responseLength);
        _dataOutputStream.writeBytes("HTTP/1.0 " + httpCodeText + "\nServer: microDuke (Anar Software LLC - http://anars.com)\nContent-Type: " + (mimeType == null ? "text/html" : mimeType) + "\n" +
            (responseLength > 0 ? "Content-Length: " + responseLength + "\n" : "") + "\n");
        if (httpCode == 200)
          {
            int read = 0;
            byte[] buffer = new byte[10240];
            InputStream fileInputStream = new FileInputStream(requestedFile);
            while ((read = fileInputStream.read(buffer)) > 0)
              _dataOutputStream.write(buffer, 0, read);
            fileInputStream.close();
          }
        else
          {
            _dataOutputStream.writeBytes("<html><head><title>" + httpCodeText + "</title></head><body><h1>" + httpCodeText + "</h1><hr/><address>microDuke (Anar Software LLC - <a href=\"http://anars.com\">http://anars.com</a>)</address></body></html>");
          }
      }
    catch (Exception exception)
      {
        printError(exception);
      }
    closeConnection();
  }

  private void closeConnection()
  {
    try
      {
        _bufferedReader.close();
      }
    catch (Exception exception)
      {
        printError(exception);
      }
    _bufferedReader = null;
    try
      {
        _dataOutputStream.close();
      }
    catch (Exception exception)
      {
        printError(exception);
      }
    _dataOutputStream = null;
    try
      {
        _socket.close();
      }
    catch (Exception exception)
      {
        printError(exception);
      }
    _socket = null;
  }

  private void printError(Exception exception)
  {
    System.err.println("Error : " + exception.getMessage() + " [" + (new Date()) + "]");
  }
}
