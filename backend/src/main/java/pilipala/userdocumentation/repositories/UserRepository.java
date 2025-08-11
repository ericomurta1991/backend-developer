package pilipala.userdocumentation.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pilipala.userdocumentation.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	boolean existsByCpf(String cpf);
	Optional<User> findByCpf(String cpf);
}
