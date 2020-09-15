package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.helpers.CSVCourseHelper;
import com.vvelikova.schoolgradingsystem.helpers.CSVStudentHelper;
import com.vvelikova.schoolgradingsystem.repositories.CourseRepository;
import com.vvelikova.schoolgradingsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    public void save(MultipartFile file) {
        try {
            List<Student> students = CSVStudentHelper.csvToStudents(file.getInputStream());
            List<Student> studentsBatch01 =  students.subList(0,51);
            List<Student> studentBatch02 = students.subList(51, 101);
            List<Student> studentsBatch03 = students.subList(101, 151);
            List<Student> studentsBatch04 = students.subList(151, 201);
            studentRepository.saveAll(studentsBatch01);
            studentRepository.saveAll(studentBatch02);
            studentRepository.saveAll(studentsBatch03);
            studentRepository.saveAll(studentsBatch04);
        } catch (IOException exc) {
            throw new RuntimeException("Fail to store csv data: " + exc.getMessage());
        }
    }

    // Originally a LIST in teh example
    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveCourses(MultipartFile file) {
        try {
            List<Course> courses = CSVCourseHelper.csvToCourses(file.getInputStream());
//            List<Course> coursesBatch01 =  courses.subList(0,51);
//            List<Course> coursesBatch02 = courses.subList(51, 101);
//            List<Course> coursesBatch03 = courses.subList(101, 151);
//            List<Course> coursesBatch04 = courses.subList(151, 201);
            courseRepository.saveAll(courses);
//            studentRepository.saveAll(studentBatch02);
//            studentRepository.saveAll(studentsBatch03);
//            studentRepository.saveAll(studentsBatch04);
        } catch (IOException exc) {
            throw new RuntimeException("Fail to store csv data: " + exc.getMessage());
        }
    }

    // Originally a LIST in teh example
    public Iterable<Student> getAllCourses() {
        return studentRepository.findAll();
    }

}
