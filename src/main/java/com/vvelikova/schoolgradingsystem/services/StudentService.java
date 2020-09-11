package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student saveUpdateStudent(Student theStudent) {

        if (theStudent.getId() != null) {
            Student existingStudent = studentRepository.getById(theStudent.getId());

            if (existingStudent == null) {
                throw new StudentNotFoundException("Student with ID: " + theStudent.getId() + " does NOT exist in the system.");
            }
        }

        return studentRepository.save(theStudent);
    }

    public Student getStudentById(Long id) {
        Student existingStudent = studentRepository.getById(id);

        if (existingStudent == null) {
            throw new StudentNotFoundException("Student with ID: " + id + " does NOT exist in the system.");
        }

        return existingStudent;
    }

    public void deleteStudentById(Long id) {
        studentRepository.delete(getStudentById(id));
    }
}
