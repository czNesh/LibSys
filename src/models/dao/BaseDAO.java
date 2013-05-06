/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import io.Configuration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

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
    private String groupBy;
    int limit = -1;
    int start = -1;
    private String orderBy = "id";
    private String orderType = "ASC";

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

        if (groupBy != null) {
            queryStringBuilder.append(" ").append("GROUP BY ").append(groupBy);
        }

        if (orderType.equals("ASC")) {
            queryStringBuilder.append(" ").append("ORDER BY t.").append(orderBy).append(" ASC");
        } else {
            queryStringBuilder.append(" ").append("ORDER BY t.").append(orderBy).append(" DESC");
        }

        Query query = session.createQuery(queryStringBuilder.toString());

        if (start != -1) {
            query.setFirstResult(start);
        }
        if (limit != -1) {
            query.setMaxResults(limit);
        }

        // Add parameters
        for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
            if (entry.getValue() instanceof List) {
                System.out.println("je list");
                query.setParameterList(entry.getKey(), (List) entry.getValue());
            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        System.out.println(query.getQueryString());
        List<T> list = (List<T>) query.list();
        closeSession();
        clear();
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

        T unique = (T) type.cast(query.uniqueResult());

        closeSession();
        clear();
        return unique;
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
        if (this.condition == null) {
            this.condition = condition;
        } else {
            this.condition = "(" + this.condition + ") AND " + "(" + condition + ")";
        }
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

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderType(String ascending) {
        if (ascending.equals("ASC") || ascending.equals("ASCENDING")) {
            this.orderType = "ASC";
        } else {
            this.orderType = "DESC";
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setExactMatch(String column, List<String> list) {
        getParameters().put("list", list);
        setCondition(column + " in (:list)");
    }

    public int getTotalCount() {
        openSession();
        StringBuilder queryStringBuilder = new StringBuilder("SELECT t.id FROM ");
        queryStringBuilder.append(typeName).append(" t");

        if (condition != null) {
            queryStringBuilder.append(" WHERE");
            queryStringBuilder.append("(").append(condition).append(")");
        }

        if (groupBy != null) {
            queryStringBuilder.append(" ").append("GROUP BY ").append(groupBy);
        }
        Query q = session.createQuery(queryStringBuilder.toString());
        for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int count = q.list().size();
        closeSession();
        clear();
        return count;
    }

    public List<T> criteriaSearch(ResultTransformer rt, Projection p, Criterion... criterions) {
        openSession();
        Criteria criteria = session.createCriteria(type);
        for (Criterion c : criterions) {
            criteria.add(c);
        }

        if (p != null) {
            criteria.setProjection(p);
        }

        if (rt != null) {
            criteria.setResultTransformer(rt);
        }

        List<T> out = criteria.list();
        closeSession();
        return out;
    }

    private void clear() {
        groupBy = null;
        parameters.clear();
        condition = null;
        start = -1;
        limit = -1;
    }

    public Map<String, String> getOrderTypeMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Vzestupně", "ASC");
        map.put("Sestupně", "DESC");
        return map;
    }
}
