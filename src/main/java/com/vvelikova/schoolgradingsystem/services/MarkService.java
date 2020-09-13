package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.exceptions.MarkNotFoundExceptoin;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collection;

@Service
public class MarkService {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private StudentService studentService;

    public Mark addMark(Long studentId, Mark theMark) throws StudentNotFoundException {
        Student theStudent = studentService.getStudentById(studentId);

        theMark.setStudent(theStudent);

        return markRepository.save(theMark);
    }

    public Iterable<Mark> findAllMarks() {
        Iterable<Mark> allMarks = markRepository.findAll();
        return allMarks;
    }

    public double getAverageGradeOfStudentAcrossCourses(Long studentId) throws StudentNotFoundException {
        Student theStudent = studentService.getStudentById(studentId);
        Integer studentIdV = Integer.valueOf(studentId.intValue());
        DecimalFormat df = new DecimalFormat("#.##");
        Double avgGrade = markRepository.getAverageGradeOfStudentAcrossCourses(studentIdV);
        System.out.println(avgGrade);
        return avgGrade;
    }

    public Mark getMarkById(Long markId) {
        Mark existingMark = markRepository.getById(markId);

        if(existingMark == null ) {
            throw new MarkNotFoundExceptoin("Mark with ID " + markId + " does NOT exist in the system.");
        }
        return existingMark;

    }

    public void deleteMarkById(Long markId) {
        markRepository.delete(getMarkById(markId));
    }
}
