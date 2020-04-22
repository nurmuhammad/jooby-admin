package com.smartbox.admin.model;

import com.smartbox.admin.App;
import com.smartbox.admin.Instance;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Nurmuhammad
 * Date: 22/04/2020 12:31
 */

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
@Slf4j
public abstract class Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "created")
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date created;

    @Column(name = "changed")
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date changed;

    @Column(name = "comment", length = 512)
    protected String comment = new String();

    public String sid() {
        return String.valueOf(id);
    }

    @PrePersist
    @PreUpdate
    void pre() {
        if (this.created == null) {
            this.created = new Date();
        }
        this.changed = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Model o = (Model) obj;
        if (o.getId() == null && getId() == null) return this == o;
        return this.getId().equals(o.getId());
    }

    public void persist() {
        Instance.app.require(EntityManager.class).persist(this);
    }

    public<T> T merge() {
        return (T) Instance.app.require(EntityManager.class).merge(this);
    }

    public void remove() {
        Instance.app.require(EntityManager.class).remove(this);
    }


}