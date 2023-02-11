# QSA Config Resolver ![CircleCI](https://img.shields.io/circleci/build/github/Queueing-Systems-Assistance/qsa-config-resolver/master)

**QSA Config Resolver** resolves a config with the given conditions. You can use this by adding it as a dependency and including your `pom.xml` or `build.gradle` the following configuration:

1. Add the following dependencies to your project:
- Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.unideb.qsa</groupId>
        <artifactId>qsa-config-resolver</artifactId>
        <version>LATEST_VERSION</version>
    </dependency>
</dependencies>
```
- Gradle:
```groovy
dependencies {
    implementation group: 'com.unideb.qsa', name: 'qsa-config-resolver', version: 'LATEST_VERSION'
}
```
2. In your application:
- First, define where the configs are (you can use multiple configs and different paths):
```java
public class YourClass {

    public static void main(String[] args) {
        ConfigResolver resolver = new ConfigPackResolverBuilder()
                .withRefreshInMinutes(refreshRate)
                .withLocalPaths(List.of("src/resources/config-pack.json", "/p/configs/config-pack.json"))
                .withUrls(httpClient, List.of("http://your-web-server/config-pack.json"))
                .withAwsLambda(lambdaClient, List.of("your-lambda-function"))
                .build();  
    }
}
```
- Then in your class:
```java
class YourClass {

    public resolveConfig(ConfigResolver resolver) {
        Optional<String> result = resolver.resolve("YOUR_CONFIG", new Qualifier.Builder().put("locale", "en").build());
    }

}
```