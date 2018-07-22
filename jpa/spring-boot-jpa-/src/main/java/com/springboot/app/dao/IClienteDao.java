package com.springboot.app.dao;

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.models.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Serializable>{
	
}
