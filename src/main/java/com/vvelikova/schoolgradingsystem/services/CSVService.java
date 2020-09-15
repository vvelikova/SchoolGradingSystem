package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.helpers.CSVCourseHelper;
import com.vvelikova.schoolgradingsystem.helpers.CSVMarkHelper;
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

    @Autowired
    MarkService markService;

    public void save(MultipartFile file) {
        try {
            List<Student> students = CSVStudentHelper.csvToStudents(file.getInputStream());
            List<Student> studentsBatch01 = students.subList(0, 51);
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

            courseRepository.saveAll(courses);
        } catch (IOException exc) {
            throw new RuntimeException("Fail to store csv data: " + exc.getMessage());
        }
    }

    public void saveMarks(MultipartFile file) {
        try{
            List<Mark> marks = CSVMarkHelper.csvToMark(file.getInputStream());
            System.out.println("Before saving any marks!!");
            marks.stream().forEach(mark -> markService.addMark(999L, 999L, mark));
            for(int i = 0; i < 130; i ++) {
                System.out.println("Before saving mark with date" + marks.get(i).getMark_date());
                markService.addMark(999L, 999L, marks.get(i));
                System.out.println("After saving mark");
            }

        } catch (IOException exc) {
            throw new RuntimeException("Failed to store csv data: " + exc.getMessage());
        }
    }

    // Originally a LIST in teh example
    public Iterable<Student> getAllCourses() {
        return studentRepository.findAll();
    }

}
