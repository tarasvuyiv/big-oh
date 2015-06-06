# Big-Oh Software's Google Code Project #

## Overview ##
[Big-Oh Software](http://www.big-oh.net/) publishes open-source software [Frameworks & Tools](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/index.html) to be used freely by the Java development community.

## Sub-Projects ##
This Big-Oh Software project is comprised of multiple sub-projects that can be used in isolation or in concert with one another.
  * [Common Framework](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareCommon/index.html) -- Common classes and static utility methods.
  * [CommonWeb Framework](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareCommonWeb/index.html) -- Reusable Servlets, Filters & Liteners.
  * [Data Structures & Algorithms Library](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareDataStructuresAndAlgorithms/index.html) -- Implementations of useful and well-know data structures & algorithms.
  * [HibernateExtension Library](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareHibernate/index.html) -- Convenient extensions to the [Hibernate](http://docs.jboss.org/hibernate/stable/core/reference/en/html_single/) ORM framework.
  * [JDBCProxy Framework](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareJDBCProxy/index.html) -- A framework for unobtrusively observing JDBC events.
  * [PostOffice Framework](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwarePostOffice/index.html) -- An uber-thin wrapper around the [Apache Commons Email](http://commons.apache.org/email/) library.
  * [ResourceRequestStats Tool](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwareResourceRequestStats/index.html) -- A tool for tracking Web application resource requests.

## Usage ##
Interested in using one or more of the sub-projects described above?  Check out the [Downloads](Downloads.md) page to find out how you can use Maven to include [Big-Oh Software](http://www.big-oh.net/) sub-projects in your code.

## Building ##
This project uses a [Maven](http://maven.apache.org/)-based build.  As a result, if you want to build this project from [source](http://code.google.com/p/big-oh/source/browse/), you'll need to define some build credentials in your [settings.xml](http://maven.apache.org/settings.html) file.

### SCM Release Credentials ###
```
	<servers>
		...
		<server>
			<id>big-oh.code.google</id>
			<username>YourUserName</username>
			<password>[super_secret_google_code_passsword]</password>
		</server>
		...
	</servers>
```

### Integration Test Profile ###
```
	<profiles>
		<profile>
                  	<id>BigOhSoftware</id>
            		<activation>
                		<activeByDefault>true</activeByDefault>
            		</activation>
                	<properties>
                		<postoffice.smtpPassword>[super_secret_smtp_passsword]</postoffice.smtpPassword>
                	</properties>
            	</profile>
	</profiles>
```