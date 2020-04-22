package com.smartbox.admin;

import com.smartbox.admin.model.User;
import com.smartbox.admin.repo.UserRepo;
import com.typesafe.config.Config;
import io.jooby.Environment;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Nurmuhammad
 * Date: 22/04/2020 11:40
 */
public class Instance {
    private static final Logger log = LoggerFactory.getLogger(Instance.class);

    public static Server dbServer;
    public static App app;
    public static Environment env;

    public static User SYSTEM_USER;
    public static User ADMIN_USER;

    public static void initUsers() {
        Session session = app.require(Session.class);
        try {
            session.beginTransaction();

            UserRepo userRepo = new UserRepo();
            String system = "system";
            String systemPassword = String.valueOf(System.currentTimeMillis()) + System.nanoTime();
            SYSTEM_USER = userRepo.createUserIfNotExist(system, systemPassword);

            String admin = env.getProperty("user.admin.username", "admin");
            String adminPassword = env.getProperty("user.admin.password", "admin");
            ADMIN_USER = userRepo.createUserIfNotExist(admin, adminPassword);

            session.getTransaction().commit();

        } catch (Throwable throwable) {
            session.getTransaction().rollback();
            log.error(throwable.getMessage(), throwable);
        }

    }
}
