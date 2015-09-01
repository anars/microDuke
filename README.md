#Information

**microDuke** is a simple, standalone, multi-threaded, small web server written in Java. It aims to be fast, secure, efficient, portable, and minimum necessary implementation of HTTP/1.1 protocol.

The [microDuke.jar][3] file is about 4.7 Kilobytes, exactly 4,808 Bytes! It is much smaller than this README.md file you are reading!!!

#Features:

  * 100% Pure Java Implementation, 1.2 or later compatible
  * Open source under GNU General Public License
  * Very simple, fast, reliable, robust, cross-platform
  * Very small JAR file, less than 5 kB
  * No installation, configuration is needed
  * Multi-threaded implementation
  * Support for MIME Type mappings
  * Support for multi-homed hosts
  * Directory index retrieval
  * Apache compatible logging, Common Log Format (CLF)

#Build Instructions

## Using Apache Ant

Clone the microDuke repository into a new directory

    git clone https://github.com/anars/microDuke.git

Change the current working directory to microDuke repository directory

    cd microDuke

Compile microDuke project using ant make tool.

    ant

When the compilation is completed successfully, you should see microDuke.jar file under "[release][3]" directory.

## Using standard JDK tools

Clone the microDuke repository into a new directory

    git clone https://github.com/anars/microDuke.git

Change the current working directory to microDuke repository directory

    cd microDuke

Create a directory called "classes"

    mkdir classes

Compile microDuke project using java.

    javac -sourcepath source -source 1.2 -target 1.2 -g:none -d classes source/com/anars/microduke/Listener.java source/com/anars/microduke/Handler.java

When the compilation is completed successfully, create java archive file

    jar cfe release/microDuke.jar com.anars.microduke.Listener -C classes/ .

Now, you should see microDuke.jar file under "[release][3]" directory. You may clean the working directory by typing;

    rm -rf classes

#FAQs

##How can I start microDuke?
To start microDuke with default values, in the command-line window simply type

    java -jar microDuke.jar

##Why I get '404 Not Found' at the URL microDuke runs?
You need to create default HTML page in the document root directory. Simple create a file called **index.html** and put the HTML you want to serve into it.

##How can I specify server port number?
To specify the TCP port which microDuke listens, use **-port** parameter (for more information, please check out **Command Line Parameters** section of the page). For example you want to run microDuke on TCP port 8888, in the command-line window simply type

    java -jar microDuke.jar -port=8888

##How can I specify the directory which microDuke serves my files from?
To specify the directory which microDuke serves files from, use **-path** parameter (for more information, please check out **Command Line Parameters** section of the page). For example all of your HTML pages are under /home/www/mysite/, in the command-line window simply type

    java -jar microDuke.jar -path=/home/www/mysite/

##I did not use -path parameter, where does microDuke serve files from?
If you do not pass any value with **-path** parameter, microDuke uses current working directory (wherever you start microDuke from) as document root.

##How can I make microDuke to be accessible only from local area network?
Using **-address** parameter (for more information, please check out **Command Line Parameters** section of the page). you can specify Local IP address or hostname which microDuke will only accept connect requests from. For example, your computer has 2 IP addresses, one is local area network (LAN) IP address (such as 192.168.1.2) and the other one is external IP address. If you want microDuke to be accessible only from local area network, you must specify your local area network IP address as address;

    java -jar microDuke.jar -address=192.168.1.2

##How can read the log?
When you start microDuke, it dumps all connection log to the console. The log entries in a similar format known as the Common Log Format (CLF). This standard format can be produced by many different web servers and read by many log analysis programs. The log file entries produced in CLF will look something like this:

    127.0.0.1 - [Sun Aug 01 14:08:37 EDT 2010] "GET /logo.jpg HTTP/1.1" 200 2154

Each part of this log entry is described below;

  * **127.0.0.1** This is the IP address of the client (remote host) which made the request to the server. The IP address reported here is not necessarily the address of the machine at which the user is sitting. If a proxy server exists between the user and the server, this address will be the address of the proxy, rather than the originating machine.
  * **[Sun Aug 01 14:08:37 EDT 2010]** The time that the server finished processing the request. It is in the form of **dow mon dd hh:mm:ss zzz yyyy** where ;
      * **dow** is the day of the week (Sun, Mon, Tue, Wed, Thu, Fri, Sat).
      * **mon** is the month (Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec).
      * **dd** is the day of the month (01 through 31), as two decimal digits.
      * **hh** is the hour of the day (00 through 23), as two decimal digits.
      * **mm** is the minute within the hour (00 through 59), as two decimal digits.
      * **ss** is the second within the minute (00 through 61, as two decimal digits.
      * **zzz** is the time zone (and may reflect daylight saving time).
      * **yyyy** is the year, as four decimal digits.
  * **"GET /logo.jpg HTTP/1.1"** The request line from the client is given in double quotes. The request line contains a great deal of useful information. First, the method used by the client is GET. Second, the client requested the resource /logo.jpg, and third, the client used the protocol HTTP/1.1.
  * **200** This is the status code that the server sends back to the client. This information is very valuable, because it reveals whether the request resulted in a successful response (codes beginning in 2), an error caused by the client (codes beginning in 4), or an error in the server (codes beginning in 5). The full list of possible status codes can be found in the HTTP specification (RFC2616 section 10) at http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html .
  * **2154** The last entry indicates the size of the object returned to the client, not including the response headers. If no content was returned to the client, this value will be zero.

##How can I save log to a file?
You can use redirection; the "greater than" symbol ('>') to send the log items into a file. By placing '>' after the command and following this with the name of the log file, the log output will go to the specified file instead of the screen. For example;

    java -jar microDuke.jar > weblog.txt

##How can I define new MIME Types

microDuke tries to guess the content type of a file based on filename extension. The guesses are determined by the content-types.properties file, normally located in the jre/lib directory. If you need to add new filename extension/MIME Type, you need to edit content-types.properties under $JAVA_HOME/jre/lib/. For example, you want to add filename extension/MIME Type for MP4 files, all you have to do add the lines below end of the content-types.properties file.

    video/mp4: \
    description=Video file that uses MPEG-4 compression; \
    file_extensions=.mp4

#Command-Line Parameters

All parameters are optional.

**-port=NUMBER** The TCP port on which microDuke should listen for HTTP requests. The number must be between 1 and 65535, inclusive. Default, any free local TCP port.

    -port=8080

**-path=PATH** The directory (in the real filesystem) from which microDuke will be serving most of its files. Default, current working directory.

    -path=/var/local/www/

**-address=INET-ADDRESS** Local IP address or hostname which microDuke will only accept connect requests from. Default, any/all local addresses.

    -address=192.168.1.100

**-help** Displays this help.

    -help

#Command-Line Error Codes

**-1 -port value must be between 1 and 65535.** The number you pass with port parameter must be between 1 and 65535.

**-2 Invalid -port value.** port parameter accepts only numbers. Please make sure you entered only numbers after /-port=/.

**-3 Invalid -address value.** The value you pass with address parameter must be a valid, local IP address or local hostname. Please make sure you type the IP address or hostname correctly.

**-4 Invalid -path value, "..." not exists.** The path you pass with path parameter does not exist or it is not accessible. Please make sure you type the path correctly, it exists, and it is accessible.

**-5 Invalid -path value.** The path you pass with path parameter must be a valid path. Please make sure you type the path correctly.

**-6 Unknown parameter "...".** When you pass a parameter that microDuke cannot understand, It returns this error.

**-7 Unable to bind socket - ...** You might encounter this error message for one of the following reasons:

  * If you specified TCP port below 1024, in Linux, and other UNIX-like systems, you have to be root (have superuser privileges) in order to listen to TCP ports below 1024, please specify any other port above 1024. We DO NOT recommend you to run microDuke with superuser privileges!
  * Make sure the port you specified is not occupied by any other application, server or daemon

#Bug tracker

Have a bug? Please create an [issue][1] here on GitHub!

#Twitter account

Keep up to date on announcements and more by following Anar Software LLC on Twitter, [@AnarSoft][2].

#Contributing

1. Fork it.
2. Create a branch (`git checkout -b my_microDuke`)
3. Commit your changes (`git commit -am "Added New Dukes"`)
4. Push to the branch (`git push origin my_microDuke`)
5. Create an [issue][1] with a link to your branch

#License

microDuke - Java Based Micro Web Server

Copyright (c) 2012 - 2015 Anar Software LLC.

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details at http://www.gnu.org/licenses/gpl.html

[1]: https://github.com/anars/microDuke/issues
[2]: http://twitter.com/AnarSoft
[3]: https://github.com/anars/microDuke/tree/master/release
