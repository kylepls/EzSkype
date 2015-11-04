# EzSkype
A Skype for Web compatibility layer for Java

### Maven install

Add the following to your pom.xml

Add the repository
```xml
<repositories>
    <repository>
        <id>EzSkype-mvn-repo</id>
        <url>https://raw.github.com/AkHo1ic/EzSkype/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
Add the dependency

```xml
<dependencies>
    <dependency>
        <groupId>in.kyle</groupId>
        <artifactId>EzSkypeEzLife</artifactId>
        <version>1.2.6-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Usage

Basic bot usage can be found on the [wiki](https://github.com/AkHo1ic/EzSkype/wiki)

A full bot example can be found [here](https://github.com/AkHo1ic/EzSkype/blob/master/src/test/java/TestSkypeBot.java)
