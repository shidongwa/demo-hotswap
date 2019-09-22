# demo-hotswap
URLClassLoader HotSwap
This demo check customized URLClassLoader's hotswap feature in spring boot environment.

You can run the test in this way
- git clone code to local desktop
- build code: mvn clean package
- mv demo-hotswap-0.0.1-SNAPSHOT.war demo.war 
- copy demo.war to you workspace
- build and move test.jar to /tmp
  - ` jar cvf test.jar com/stone/jdk/demohotswap/swapClass/SwapMe.class`
- run demo.war in jetty with two ways
  - `java -cp jetty-runner-9.4.14.v20181114.jar org.eclipse.jetty.runner.Runner demo.war`
  - `java -jar demo.war`
- access endpoint http://localhost:8080/swap
- update SwapMe.java, build and replace test.jar
- access endpoint http://localhost:8080/swap again

root cause is discussed at 
* https://github.com/spring-projects/spring-boot/issues/18300
* https://github.com/spring-projects/spring-boot/issues/15964
