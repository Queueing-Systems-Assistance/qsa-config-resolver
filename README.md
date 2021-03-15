# QSA Config Resolver ![CircleCI](https://img.shields.io/circleci/build/github/Queueing-Systems-Assistance/qsa-config-resolver/master)

**QSA Config Resolver** resolves a config with the given conditions. You can use this by adding as a dependency and including your `pom.xml` or `build.gradle` the following configuration:

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
- First define where are the configs located: (you can use multiple configs, and different paths)
```yaml
qsa:
  config:
    uris: https://YOUR_CONFIG_URI,C://YOUR_CONFI_PATH
```
- Then in your class:
```java
class YourClass {

    @Autowired
    private ConfigResolver configResolver;

    public resolveConfig() {
        Optional<String> result = configResolver.resolve("YOUR_CONFIG", new Qualifier.Builder().put("locale", "en").build());
    }

}
```
