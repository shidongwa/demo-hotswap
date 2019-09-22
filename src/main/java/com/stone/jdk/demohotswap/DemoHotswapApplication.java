package com.stone.jdk.demohotswap;

import com.stone.jdk.demohotswap.classloader.MyClassLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@RestController
public class DemoHotswapApplication extends SpringBootServletInitializer {
    private static final String PROTOCOL_HANDLER = "java.protocol.handler.pkgs";

    public static void main(String[] args) {
        SpringApplication.run(DemoHotswapApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoHotswapApplication.class);
    }

    @RequestMapping("/")
    public String index() {
        return "Hello Mr Swap!";
    }

    @RequestMapping("/swap")
    public String swap() {
        MyClassLoader cl = new MyClassLoader();

        String jarFile = String.format("jar:file:%s/%s!/", "/tmp", "test.jar");
        try {
            URL jarUrl = new URL(jarFile);
            cl.addURL(jarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        URL[] urls = cl.getURLs();
        if(urls != null && urls.length > 0) {
            try {
                // Create a jar URL connection object
                JarURLConnection jarURLConnection = (JarURLConnection) urls[0].openConnection();
                System.out.println("jarURLConnection:" + jarURLConnection);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        String handlers = System.getProperty(PROTOCOL_HANDLER, "");
        System.out.println("handlers:" + handlers);

        String output = "NA";
        try {
            Class<?> swapClass = cl.loadClass("com.stone.jdk.demohotswap.swapClass.SwapMe");
            Object plugin = swapClass.newInstance();
            Method method = swapClass.getMethod("hello");
            output = (String)method.invoke(plugin);
            System.out.println("output:" + output);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            try {
                cl.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return String.format("%s - %s", output,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()));
    }
}
