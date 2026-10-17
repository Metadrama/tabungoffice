package io.github.metadrama.tabungoffice.web;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.metadrama.tabungoffice.User;

@RestController
public class UserController {

	@GetMapping("/list_users")
	ArrayList<User> listUsers() {

		
		ArrayList<User> users = new ArrayList<>(); // TODO: fetch from the db once users are stored there
		return users;
	};
};