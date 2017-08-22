package com.shsxt.crm.service;

import java.util.List;

import com.shsxt.crm.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shsxt.crm.model.User;

@Service
public class TestService {
	
	@Autowired
	private UserDao userDao;
	
	
	public User findById(Integer id) {
		User user = userDao.findById(id);
		return user;
	}
	
	public List<User> find() {
		List<User> users = userDao.find();

		return users;
	}

	public static void main(String[] args) {
		String a="25664";
		Integer integer = Integer.getInteger(a);


		System.out.println(integer);
	}
	
	
}
