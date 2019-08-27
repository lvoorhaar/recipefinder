RecipeFinder is a search engine for recipes. The recipes are stored in a MongoDB database.

Install Java SE Development Kit 8 from https://www.oracle.com/technetwork/java/javase/downloads/index.html.

Install Apache Maven 3.6.1 from https://maven.apache.org/download.cgi and set path variable (https://maven.apache.org/install.html).

Install MongoDB Community Server (version 4.0.12) from https://www.mongodb.com/download-center/community.

How to build the app:
Go to folder \recipefinder.
Type 'cmd' in pathline.
Type 'mvn clean install' in the command line, press Enter.
Close window when build is complete.
Go to folder \reciperfinderAPI.
Type 'cmd' in pathline.
Type 'mvn clean package' in the command line, press Enter.
When build is complete, type 'mvn jetty:run' in the command line, press Enter.
Leave this window open and start your browser.
Go to 'http://localhost/recipefinder/'
The app should work from here. You won't see any recipes if the database is empty. New recipes can be added manually via the app, or by using the import feature.