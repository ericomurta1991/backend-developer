package pilipala.userdocumentation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pilipala.userdocumentation.entities.UserDocumentation;

public interface UserDocumentationRepository extends JpaRepository<UserDocumentation, Long>{

}
