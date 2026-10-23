//this Class talks to client unlike UserRepository interface which talks to DB
//each method here is called when requested via URL
//whatever returns, returns in JSON format

package io.github.metadrama.tabungoffice.web;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.metadrama.tabungoffice.User;
import io.github.metadrama.tabungoffice.UserRepository;

@RestController
public class UserController {
	private final UserRepository repository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	record StoreUser(String displayName, String email, String phone, String password) {
	};

	record CallUser(String phone, String password) {
	};

	public UserController(UserRepository repository) {

		this.repository = repository;
	}

	@PostMapping("/register")
	ResponseEntity<User> registerUser(@RequestBody StoreUser signup) {
		if (repository.existsByPhone(signup.phone())) {
			return ResponseEntity.status(409).build();
		}
		User user = new User();
		user.displayName = signup.displayName();
		user.setEmail(signup.email());
		user.setPhone(signup.phone());
		user.setPassword(passwordEncoder.encode(signup.password()));
		return ResponseEntity.ok(repository.save(user));
	}

	@PutMapping("/users/{id}")
	ResponseEntity<User> updateUser(@RequestBody User changes, @PathVariable Long id) {
		User existing = repository.findById(id).orElse(null);
		if (existing == null) {
			return ResponseEntity.notFound().build();
		}
		existing.displayName = changes.displayName;
		existing.setEmail(changes.getEmail());
		existing.setPhone(changes.getPhone());
		return ResponseEntity.ok(repository.save(existing));
	}

	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {

		repository.deleteById(id);
	}

	@GetMapping("/list_users")
	List<User> listUsers() {
		return repository.findAll();
	};

	@GetMapping("/users/{id}")
	ResponseEntity<User> getUser(@PathVariable Long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/login")
	boolean login(@RequestBody CallUser attempt) {
		User found = repository.findByPhone(attempt.phone());
		if (found == null) {
			return false;
		}
		return passwordEncoder.matches(attempt.password(), found.getPassword());
	}
}
