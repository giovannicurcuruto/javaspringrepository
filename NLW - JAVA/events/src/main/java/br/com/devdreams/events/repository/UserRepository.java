package br.com.devdreams.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.devdreams.events.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{ 

    //public User findByUserEmail(String user_email);

    public User findByEmail(String email);
}

