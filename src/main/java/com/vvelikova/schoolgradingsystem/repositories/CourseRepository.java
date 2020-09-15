package com.vvelikova.schoolgradingsystem.repositories;

import com.vvelikova.schoolgradingsystem.domain.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

    Course findByCsvId(Long id);

    Course getById(Long id);

    @Override
    Iterable<Course> findAll();

}
