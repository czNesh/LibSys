/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Nesh
 */
public abstract class BaseDAO<T> implements DAO<T> {

    private Class type;
    private String typeName;
    private Transaction transaction;
    private Session session;

    public BaseDAO() {
        generateTypeData();
    }

    private void generateTypeData() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericSuperclass;
            Type genericType = pt.getActualTypeArguments()[0];

            typeName = genericType.toString().replaceAll(".*\\.([a-zA-Z0-9]+)$", "$1");
            try {
                type = Class.forName(genericType.toString().replaceAll("^class ", ""));
            } catch (ClassNotFoundException ex) {
                type = null;
            }
        }
    }

    protected void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    protected void closeSession() {
        session.close();
    }

    protected void beginTransaction() {
        transaction = session.beginTransaction();
    }

    protected void commitTransaction() {
        transaction.commit();
    }

    protected void rollBackTransaction() {
        transaction.rollback();
    }

    @Override
    public void create(T object) {
        openSession();
        beginTransaction();
        session.save(object);
        commitTransaction();
        closeSession();
    }

    @Override
    public void save(T object) {
        openSession();
        beginTransaction();
        session.saveOrUpdate(object);
        commitTransaction();
        closeSession();
    }

    @Override
    public void delete(T object) {
        openSession();
        beginTransaction();
        session.delete(object);
        commitTransaction();
        closeSession();
    }

    @Override
    public T find(Long id) {
        T object;
        openSession();

        object = (T) type.cast(session.get(type, id));

        closeSession();
        return object;
    }

    @Override
    public void update(T object) {
        openSession();
        beginTransaction();
        session.update(object);
        commitTransaction();
        closeSession();
    }
}
