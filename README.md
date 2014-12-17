plsql-from-java-maven-plugin
============================

Java source code generation for calling plsql.

Installation
------------

1. Download ojdbc6.jar JDBC Driver from Oracle 

2. Install the driver via the following command:

        mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 \
                                 -Dpackaging=jar -Dfile=ojdbc6.jar -DgeneratePom=true

3. Clone this repo
4. Run `mvn install` from the repository's root directory

Usage
-----

If you want to use the generator in any maven project, the minimal thing is to set the JDBC URL for the (development) Oracle Database, in `pom.xml`:

    <project>
        <properties>
            <pfj.jdbc.url>jdbc:oracle:thin:USER/PASSWORD@localhost:1521:xe</pfj.jdbc.url>
        </properties>
    </project>

There are more options, but the only required is only the JDBC URL.
  * pfj.jdbc.url
  * pfj.project.outputDirectory
  * pfj.project.package
  * pfj.package.entity
  * pfj.package.converter
  * pfj.package.util
  * pfj.package.procedure
  * pfj.package.service

If the configuration is successfull, you can run the generator via

    mvn pfj:generate
    
The generator creates the classes for calling the stored procedures.