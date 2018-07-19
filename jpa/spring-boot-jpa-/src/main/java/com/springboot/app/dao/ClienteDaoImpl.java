package com.springboot.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import com.springboot.app.models.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager entityManager; 
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> findAll() {
		return entityManager.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		if( cliente.getId() != 0L && cliente.getId() > 0 ) {
			entityManager.merge(cliente);
		}else {
			entityManager.persist(cliente);	
		}
	}

	@Override
	public Cliente findOne(Long id) {
		return entityManager.find(Cliente.class, id);
	}

}
