package com.smartbox.admin;

import ch.qos.logback.classic.Level;
import io.jooby.Environment;
import io.jooby.Jooby;
import io.jooby.hibernate.HibernateModule;
import io.jooby.hibernate.TransactionalRequest;
import io.jooby.hikari.HikariModule;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class App extends Jooby {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    {
        Environment env = Instance.env = getEnvironment();

        try {
            log.info("Starting a h2 database...");
            Instance.dbServer = Server.createWebServer("-webAllowOthers", "-webPort", env.getProperty("h2.db.server.port", "8083"));
            Instance.dbServer.start();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        install(new HikariModule());

        install(new HibernateModule());

        decorator(new TransactionalRequest());

        mvc(new Controller());

        {
            get("/foo", ctx -> {
                return "Foo";
            });
        }

        onStarted(() -> {
            logOff();
            log.info("The App is started");
            Instance.initUsers();
        });
    }

    public static void main(final String[] args) throws Exception {
        logOff();
        log.info("Starting a Jooby app...");

        runApp(args, () -> Instance.app = new App());

    }

    public static void logOff() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate")).setLevel(Level.INFO);
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.zaxxer.hikari")).setLevel(Level.INFO);
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.jooby")).setLevel(Level.INFO);
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.quartz")).setLevel(Level.INFO);
//        ((Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
    }

}
