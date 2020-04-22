package com.smartbox.admin.repo;

import com.smartbox.admin.$;
import com.smartbox.admin.App;
import com.smartbox.admin.Instance;
import com.smartbox.admin.model.Model;
import com.sun.istack.NotNull;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * User: Nurmuhammad
 * Date: 22/04/2020 13:09
 */
public abstract class EntityRepo<E extends Model> {
    private static final Logger log = LoggerFactory.getLogger(EntityRepo.class);

    Class<E> eClass;

    public EntityRepo(Class<E> eClass) {
        this.eClass = eClass;
    }

    public EntityRepo() {
        try {
            eClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Throwable e) {
            log.error("Error to detect crud class.", e);
        }
    }

    public Class<E> getEClass() {
        Objects.requireNonNull(eClass);
        return eClass;
    }

    protected EntityManager getEntityManager() {
        return Instance.app.require(EntityManager.class);
    }

    protected Session getSession() {
        return Instance.app.require(Session.class);
    }

    public E save(E object) {
        getSession().save(object);
        return object;
    }

    public E update(E object) {
        getSession().update(object);
        return object;
    }

    public E delete(E object) {
        getSession().delete(object);
        return object;
    }

    public E findById(Long id) {
        return getSession().get(getEClass(), id);
    }

    public List<E> findByIds(List<Long> ids) {
        return getSession().createQuery("select e from " + getEClass().getSimpleName() + " e where e.id in :ids", getEClass())
                .setParameter("ids", ids)
                .list();
    }

    public List<E> findByIds(String ids) {
        if ($.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<Long> list = new ArrayList<>();
        for (String id : ids.split("[,]")) {
            try {
                list.add(Long.valueOf(id.trim()));
            } catch (Exception ignored) {
            }
        }
        return findByIds(list);
    }

    public E loadById(Long id) {
        return getSession().load(getEClass(), id);
    }

    public List<E> loadByIds(List<Long> ids) {
        List<E> list = new ArrayList<>(ids.size());
        for (Long id : ids) {
            list.add(loadById(id));
        }
        return list;
    }

    public List<E> loadByIds(String ids) throws Throwable {
        if ($.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<Long> list = new ArrayList<>();
        for (String id : ids.split("[,]")) {
            try {
                list.add(Long.valueOf(id.trim()));
            } catch (Exception ignored) {
            }
        }
        return loadByIds(list);
    }

    public void deleteById(Long id) throws Throwable {
        Object e = getSession().load(getEClass(), id);
        getSession().delete(e);
    }

    public List<E> findAll() {
        return getSession().createQuery("select e from " + getEClass().getSimpleName() + " e order by e.id", getEClass())
                .getResultList();
    }

    public E findFirst(String where, Object... params) {
        Query query = getSession().createQuery("from " + getEClass().getSimpleName() + " where " + where);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        query.setMaxResults(1);
        List<E> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public long count() {
        return getSession().createQuery("select count(*) from " + getEClass().getSimpleName(), Long.class).uniqueResult();
    }

    public long count(String where, Object... params) {
        org.hibernate.query.Query<Long> query = getSession().createQuery("select count(*) from " + getEClass().getSimpleName() + " where " + where, Long.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.uniqueResult();
    }

    public List<E> where(String where, Object... params) {
        org.hibernate.query.Query<E> query = getSession().createQuery("from " + getEClass().getSimpleName() + " where " + where, getEClass());
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    public void update(Long id, Object... params) {
        StringBuilder builder = new StringBuilder();

        builder.append("update ").append(getEClass().getSimpleName()).append(" set ");
        for (int i = 0; i < params.length; i = i + 2) {
            Object o = params[i];
            builder.append(o).append("=:").append(o);
            if (i < params.length - 2)
                builder.append(", ");
        }
        builder.append(" where id = :id");
        Query query = getSession().createQuery(builder.toString());

        for (int i = 1; i < params.length + 1; i += 2) {
            Object o = params[i];
            Object o2 = params[i + 1];
            query.setParameter(o.toString(), o2);
        }
        query.setParameter("id", id);
        query.executeUpdate();

    }

}
