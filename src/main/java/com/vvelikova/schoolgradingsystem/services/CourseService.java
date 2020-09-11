package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.exceptions.CourseNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course saveUpdateCourse(Course theCourse) {

        if (theCourse.getId() != null) {
            Course existingCourse = courseRepository.getById(theCourse.getId());

            if (existingCourse == null) {
                throw new CourseNotFoundException("Course with ID: " + theCourse.getId() + " does NOT exist in the system.");
            }
        }

        return courseRepository.save(theCourse);
    }

    public Course getCourseById(Long id) {
        Course existingCourse = courseRepository.getById(id);

        if (existingCourse == null) {
            throw new CourseNotFoundException("Course with ID: " + id + " does NOT exist in the system.");
        }

        return existingCourse;
    }
}
