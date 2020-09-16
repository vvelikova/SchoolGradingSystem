package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.helpers.CSVCourseHelper;
import com.vvelikova.schoolgradingsystem.helpers.CSVMarkHelper;
import com.vvelikova.schoolgradingsystem.helpers.CSVStudentHelper;
import com.vvelikova.schoolgradingsystem.repositories.CourseRepository;
import com.vvelikova.schoolgradingsystem.repositories.MarkRepository;
import com.vvelikova.schoolgradingsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MarkService markService;

    @Autowired
    MarkRepository markRepository;


    /**
     * The conversion of csv information to entries in the database is now only supporting small files.
     */
    public void save(MultipartFile file) {
        try {
            List<Student> students = CSVStudentHelper.csvToStudents(file.getInputStream());
            studentRepository.saveAll(students);
        } catch (IOException exc) {
            throw new RuntimeException("Fail to store csv data: " + exc.getMessage());
        }
    }

    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * The conversion of csv information to entries in the database is now only supporting small files.
     */
    public void saveCourses(MultipartFile file) {
        try {
            List<Course> courses = CSVCourseHelper.csvToCourses(file.getInputStream());

            courseRepository.saveAll(courses);
        } catch (IOException exc) {
            throw new RuntimeException("Fail to store csv data: " + exc.getMessage());
        }
    }

    /**
     * The conversion of csv information to entries in the database is now only supporting small files.
     */
    public void saveMarks(MultipartFile file) throws ParseException {
        try {
            List<Mark> marks = CSVMarkHelper.csvToMark(file.getInputStream());

            marks.stream().forEach(mark -> markService.addMark(999L, 999L, mark));

        } catch (IOException exc) {
            throw new RuntimeException("Failed to store csv data: " + exc.getMessage());
        }
    }
}
