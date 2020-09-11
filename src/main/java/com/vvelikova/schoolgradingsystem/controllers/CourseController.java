package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.exceptions.CourseErrorResponse;
import com.vvelikova.schoolgradingsystem.exceptions.CourseNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.CourseRepository;
import com.vvelikova.schoolgradingsystem.services.CourseService;
import com.vvelikova.schoolgradingsystem.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createUpdateCourse(@Valid @RequestBody Course theCourse,
                                                BindingResult result) {
        ResponseEntity<?> error = validationErrorService.ValidationErrorService(result);

        if (error != null) {
            return error;
        }

        Course course;
        try {
            course = courseService.saveUpdateCourse(theCourse);
        } catch (Exception exc) {
            throw new CourseNotFoundException(exc.getMessage());
        }

        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @GetMapping("{course_id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long course_id) {
        Course course;

        try {
            course = courseService.getCourseById(course_id);
        } catch (Exception exc) {
            throw new CourseNotFoundException(exc.getMessage());
        }

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<CourseErrorResponse> handleException(CourseNotFoundException exc) {
        CourseErrorResponse error = new CourseErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
