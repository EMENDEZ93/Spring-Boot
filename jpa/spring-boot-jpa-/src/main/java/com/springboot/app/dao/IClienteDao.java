package com.springboot.app.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Serializable>{
	
}
