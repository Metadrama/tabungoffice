//this interface is what SB will use to talk to DB unlike UserController which talks to client

package io.github.metadrama.tabungoffice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByPhone (String phone);

}
