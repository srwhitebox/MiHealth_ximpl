package com.ximpl.lib.db;

import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class EntityManagerHelper {
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;
	
	/**
	 * Constructor
	 */
	public EntityManagerHelper(){
	}
	
	/**
	 * EntityManagerFacotry Setter
	 * @param entityManagerFactory
	 */
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
		this.entityManager= entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
	}

	/**
	 * Excute native SQL query : insert, update, delete
	 * @param nativeSql
	 */
	public void excute(String nativeSql){
		execute(query(nativeSql));
	}
	
	public void execute(Query query){
		if (!entityTransaction.isActive())
			entityTransaction.begin();
		query.executeUpdate();		
		entityTransaction.commit();
	}
	
	public Query query(String nativeSql){
		return entityManager.createNativeQuery(nativeSql);
	}
	
	public Query query(String nativeSql, Class<?> type){
		entityManager.clear();
		return entityManager.createNativeQuery(nativeSql, type);
	}
	
	public void flush(){
		this.entityManager.flush();
	}

}
