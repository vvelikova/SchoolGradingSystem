package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.exceptions.StudentErrorResponse;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
import com.vvelikova.schoolgradingsystem.services.StudentService;
import com.vvelikova.schoolgradingsystem.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student theStudent,
                                           BindingResult result) {
        ResponseEntity<?> error = validationErrorService.ValidationErrorService(result);

        if (error != null) {
            return error;
        }

        Student student;
        try {
            student = studentService.saveUpdateStudent(theStudent);
        } catch (Exception e) {
            throw new StudentNotFoundException(e.getMessage());
        }

        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @GetMapping("{student_id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long student_id) {

        Student student = studentService.getStudentById(student_id);

        return new ResponseEntity<>(student, HttpStatus.OK);

    }

    @DeleteMapping("{student_id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long student_id) {
        studentService.deleteStudentById(student_id);

        return new ResponseEntity<String>("Student with ID: " + student_id + " is successfully deleted.", HttpStatus.OK);
    }

    @GetMapping("{all}")
    public Iterable<Student> getAllStudents() {
        return studentService.findAllStudents();
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
