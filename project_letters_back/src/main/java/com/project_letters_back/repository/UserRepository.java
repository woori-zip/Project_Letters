package com.project_letters_back.repository;

import com.project_letters_back.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, byte[]> {
    Optional<User> findByEmail(String email);
    // 추가적인 쿼리 메서드 정의 가능
}
