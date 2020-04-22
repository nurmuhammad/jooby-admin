package com.smartbox.admin.model;

import com.smartbox.admin.$;
import com.smartbox.admin.Instance;
import com.typesafe.config.Config;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Nurmuhammad
 * Date: 22/04/2020 12:32
 */

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
@Getter
@Setter
public class User extends Model {
    @Column(name = "user_name", unique = true, nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "status")
    Boolean status;

    @Transient
    public void password(String password) {
        this.password = $.encode(password + Instance.env.getProperty("user.hash.salt"));
    }

    @Transient
    public boolean matches(String password) {
        return $.matches(password + Instance.env.getProperty("user.hash.salt"), this.password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        User o = (User) obj;
        if (o.getId() == null && getId() == null) return this == o;
        return this.getId().equals(o.getId());
    }
}