This program needs Java SE Development Kit 8 and Apache Maven installed.

Install MongoDB Community Server (version 4.0.12) from https://www.mongodb.com/download-center/community.

How to build the app:
Go to folder \recipefinder.
Type 'cmd' in pathline.
Type 'mvn clean install' in the command line, press Enter.
Close wndow when build is complete.
Go to folder \reciperfinderAPI.
Type 'cmd' in pathline.
Type 'mvn clean package' in the command line, press Enter.
When build is complete, type 'mvn jetty:run' in the command line, press Enter.
Leave this window open and start your browser.
Go to 'http://localhost/recipefinder/'
The app should work from here.