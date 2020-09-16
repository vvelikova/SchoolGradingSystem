package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    @Lazy
    private MarkService markService;

    /**
     * saveUpdateStudent() handles the update ONLY of the name of the Student.
     * If there is a need for a certain mark to be changed, the methods exposed by the MarkService should be used.
     * saveUpdateStudent() handles the update of the linked marks a Student has
     */
    public Student saveUpdateStudent(Student theStudent) {

        /** Check if the student obj is coming from CSV file and is about to be created.
         If it is not then make a check if it has an id or not assigned */
        if (theStudent.getId() != null && !theStudent.isFromCSV()) {
            Student existingStudent = studentRepository.getById(theStudent.getId());

            if (existingStudent == null) {
                throw new StudentNotFoundException("Student with ID: " + theStudent.getId() + " does NOT exist in the system.");
            }

            List<Mark> studentMarks = existingStudent.getMarks();
            List<Mark> updatedMarkInfo = new ArrayList<>();

            for (Mark obj : studentMarks) {
                Mark tempMark = markService.getMarkById(obj.getId());
                tempMark.setStudentName(theStudent.getStudentName());
                tempMark.setStudent(theStudent);
                updatedMarkInfo.add(tempMark);
            }
            theStudent.setMarks(updatedMarkInfo);
        }

        /** If updating an existing student, take care of persisting old info about student's csvID.
         * In case the Student was created using CSV file, the student's csvId holds highly important role in mapping marks to students */
        if (theStudent.getId() != null) {
            Student oldObj = studentRepository.getById(theStudent.getId());
            theStudent.setCsvId(oldObj.getCsvId());
        }

        /** In order further on to be able to update Student objects, imported by csv file,
         * should change the value of the property fromCSV to false */
        theStudent.setFromCSV(false);

        return studentRepository.save(theStudent);
    }

    public Student getStudentById(Long id) {
        Student existingStudent = studentRepository.getById(id);

        if (existingStudent == null) {
            throw new StudentNotFoundException("Student with ID " + id + " does NOT exist in the system.");
        }

        return existingStudent;
    }

    public Student findStudentByCsvId(Long id) {
        Student existingStudent = studentRepository.findByCsvId(id);

        if (existingStudent == null) {
            throw new StudentNotFoundException("Student with ID " + id + " passed in the imported CSV file DOES NOT exists!");
        }

        return existingStudent;
    }

    @Async("asyncExecutor")
    public void deleteStudentById(Long id) {
//        System.out.println("deleteStudentById on Thread -> " + Thread.currentThread().getName());

        studentRepository.delete(getStudentById(id));
    }

    public Iterable<Student> findAllStudents() {
        return studentRepository.findAll();
    }
}
