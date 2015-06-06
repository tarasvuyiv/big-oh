# Downloads #

You can manually download a jar, war or pom file by browsing the project's source controlled micro [repository](http://code.google.com/p/big-oh/source/browse/#svn/repos/releases).


# Maven #

If you're using Maven to manage your build process, it's easy to make this project's repository available to your pom.

```
<repositories>
	<repository>
		<id>net.big_oh</id>
		<name>Big-Oh Software's Maven Repository</name>
		<url>https://big-oh.googlecode.com/svn/repos/releases</url>
	</repository>
</repositories>
```

Once you've added the repository above, you're free to include any of the Big-Oh Software [frameworks and tools](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/index.html) in your project.  For example, you might include the [PostOffice Framework](http://big-oh.googlecode.com/svn/site/1.BigOhSoftwareFrameworksAndTools/BigOhSoftwarePostOffice/index.html) by adding this content to your pom.xml file;


```
<dependency>
	<groupId>net.big_oh</groupId>
	<artifactId>BigOhSoftwarePostOffice</artifactId>
	<version>2.2</version>
	<type>jar</type>
	<scope>compile</scope>
</dependency>
```