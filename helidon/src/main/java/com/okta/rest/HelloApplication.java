package com.okta.rest;

import com.okta.rest.controller.HelloResource;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.security.SecurityHttpFeature;

import java.util.concurrent.TimeUnit;

public class HelloApplication {

    public static void main(String[] args) {
        WebServerConfig.Builder builder = WebServer.builder();
        setup(builder);
        WebServer server = builder.port(8080).build();

        long t = System.nanoTime();
        server.start();
        long time = System.nanoTime() - t;

        System.out.printf("""
            Server started in %2$d ms

            Started server at http://localhost:%1$d
            """, server.port(), TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS));
    }

    static void setup(WebServerConfig.Builder server) {
        Config config = Config.create(ConfigSources.classpath("application.yml"));

        server.routing(routing -> routing
            .addFeature(SecurityHttpFeature.create(config.get("security.web-server")))
            .addFeature(new HelloResource()));
    }
}
