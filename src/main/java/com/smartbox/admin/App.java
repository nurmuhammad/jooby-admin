package com.smartbox.admin;

import io.jooby.Environment;
import io.jooby.Jooby;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Jooby {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    {

        Environment env = getEnvironment();

        try {
            Instance.dbServer = Server.createWebServer("-webAllowOthers", "-webPort", env.getProperty("h2.db.server.port", "8083"));
            Instance.dbServer.start();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        mvc(new Controller());

        {
            get("/foo", ctx -> {
                return "Foo";
            });
        }
    }

    public static void main(final String[] args) throws Exception {

        runApp(args, App::new);
    }


}
