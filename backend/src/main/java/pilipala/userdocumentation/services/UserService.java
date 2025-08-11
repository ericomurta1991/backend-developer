package pilipala.userdocumentation.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import pilipala.userdocumentation.dto.UserDTO;
import pilipala.userdocumentation.entities.User;
import pilipala.userdocumentation.exceptions.DesafioException;
import pilipala.userdocumentation.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository repository;
	
	@Transactional
	public Page<UserDTO> findAllPaged(PageRequest pageRequest){
		Page<User> list = repository.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new DesafioException("Usuário não encontrado com id: " + id));
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO insert(UserDTO dto) {
		
		if(repository.existsByCpf(dto.getCpf())) {
			throw new DesafioException("CPF já cadastrado");
		}
		
		User entity = new User();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity = repository.save(entity);
		
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			
			Optional<User> userWithCpf = repository.findByCpf(dto.getCpf());
			if(userWithCpf.isPresent() && !userWithCpf.get().getId().equals(id)) {
				throw new DesafioException("CPF já cadastrado para outro usuário");
			}
			
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			
			entity = repository.save(entity);
			return new UserDTO(entity);			
		} catch(EntityNotFoundException e) {
			throw new DesafioException("usuário não encontrado com id: " + id);
		}
	}
	
	@Transactional
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new DesafioException("Usuário não encontrado com id: " + id);
		}
		
		
		try {
			repository.deleteById(id);
		}catch(org.springframework.dao.EmptyResultDataAccessException e) {
			throw new DesafioException("Usuário não encontrado com id: " + id);
		}catch(org.springframework.dao.DataIntegrityViolationException e) {
			throw new DesafioException("Não é possível deletar o usuário pois há dados relacionados");
		}
	}
}
