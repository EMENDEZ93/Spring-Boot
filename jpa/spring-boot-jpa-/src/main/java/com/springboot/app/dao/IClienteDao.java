package com.springboot.app.dao;

import java.util.List;

import com.springboot.app.models.Cliente;

public interface IClienteDao {

	public List<Cliente> findAll();
	
}