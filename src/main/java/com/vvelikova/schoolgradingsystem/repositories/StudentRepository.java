package com.vvelikova.schoolgradingsystem.repositories;

import com.vvelikova.schoolgradingsystem.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    Student getById(Long id);

    @Override
    Iterable<Student> findAll();
}
