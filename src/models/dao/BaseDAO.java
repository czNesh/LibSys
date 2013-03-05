/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
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
    private boolean customSessionMode = false;
    private boolean customTransactionMode = false;
    private String condition;
    private final Map<String, Object> parameters = new HashMap<>();
    private List<T> list;
    private String filter;
    int limit = 50;
    int start = 0;

    public BaseDAO() {
        generateTypeData();
    }

    public BaseDAO(Session session) {
        this();
        setSession(session);
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

    @Override
    public List<T> getList() {
        openSession();

        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder.append("SELECT t FROM ");
        queryStringBuilder.append(typeName).append(" t");
        if (condition != null) {
            queryStringBuilder.append(" WHERE ");
            queryStringBuilder.append("(").append(condition).append(")");
        }

        if (filter != null) {
            queryStringBuilder.append(" ").append(filter).append(" ORDER BY t.id ASC");
            resetFilter();
        }

        Query query = session.createQuery(queryStringBuilder.toString());

        if (start != 0) {
            query.setFirstResult(start);
        }

        if (limit != -1) {
            query.setMaxResults(limit);
        }

        // Add parameters
        for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        list = (List<T>) query.list();

        // RESET
        parameters.clear();
        condition = null;

        closeSession();
        return list;
    }

    /**
     * Finds out unique result with special condition
     *
     * @param condition Condition for the unique result
     * @return Unique result if found
     */
    @Override
    public T getUnique(String condition) {

        if (condition == null) {
            throw new IllegalArgumentException("condition");
        }
        openSession();

        Query query = session.createQuery("FROM " + typeName + " WHERE " + condition);
        // Add parameters
        for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        parameters.clear();
        T unique = (T) type.cast(query.uniqueResult());

        closeSession();
        return unique;
    }

    /**
     * Sets list to null so it will be recreated after next access
     */
    public void resetList() {
        list = null;
    }

    /**
     * @param session the session to set
     */
    @Override
    public final void setSession(Session session) {
        this.session = session;
        customSessionMode = session != null && session.isOpen();
    }

    /**
     * @return the session
     */
    @Override
    public Session getSession() {
        return session;
    }

    /**
     * @param session the transaction to set
     */
    @Override
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        customTransactionMode = transaction != null && !transaction.wasCommitted();
    }

    /**
     * @return the transaction
     */
    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @return the customSessionMode
     */
    public boolean isCustomSessionMode() {
        return customSessionMode;
    }

    /**
     * @return the customTransactionMode
     */
    public boolean isCustomTransactionMode() {
        return customTransactionMode;
    }

    /**
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void resetFilter() {
        filter = null;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotalCount() {
        openSession();
        StringBuilder query = new StringBuilder("SELECT t.id FROM ");
        query.append(typeName).append(" t");

        if (filter != null) {
            query.append(filter);
        }
        int count = session.createQuery(query.toString()).list().size();
        closeSession();
        return count;
    }
}
