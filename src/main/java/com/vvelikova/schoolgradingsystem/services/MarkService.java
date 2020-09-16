package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.exceptions.CourseNotFoundException;
import com.vvelikova.schoolgradingsystem.exceptions.MarkNotFoundException;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
import com.vvelikova.schoolgradingsystem.customQueriesReturnObjects.IGroupedAverageResponse;
import com.vvelikova.schoolgradingsystem.repositories.MarkRepository;
import com.vvelikova.schoolgradingsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkService {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    public List<Mark> createMarkObjectsFromCSV(List<Mark> marksInfoCSV) {

        List<Mark> validMarkObjects = new ArrayList<>();

        for (Mark mark : marksInfoCSV) {
            Mark tempMark = new Mark();
            Student studentCSV = studentService.findStudentByCsvId(mark.getCsvStudentId());
            Course courseCSV = courseService.findCourseByCsvId(mark.getCsvCourseId());

            tempMark.setStudent(studentCSV);
            tempMark.setStudentName(studentCSV.getStudentName());
            tempMark.setCourse(courseCSV);
            tempMark.setCourseName(courseCSV.getCourseName());

            validMarkObjects.add(tempMark);
        }

        return validMarkObjects;
    }

    public Mark addMark(Long studentId, Long courseId, Mark theMark) throws StudentNotFoundException, CourseNotFoundException {
        System.out.println("In addMarks");
        System.out.println("is from csv check " + theMark.getMark());
        if (theMark.isFromCSV()) {
            System.out.println("From addMArk() isFromCSV");
            Student studentCSV = studentService.findStudentByCsvId(theMark.getCsvStudentId());

            Course courseCSV = courseService.findCourseByCsvId(theMark.getCsvCourseId());

            theMark.setStudent(studentCSV);
            theMark.setStudentName(studentCSV.getStudentName());
            theMark.setCourse(courseCSV);
            theMark.setCourseName(courseCSV.getCourseName());

            System.out.println("just before saving");

            return markRepository.save(theMark);
        }

        if (theMark.getId() != null) {
            Mark oldMarkObj = markRepository.getById(theMark.getId());
            if (oldMarkObj.getCsvStudentId() != null) {
                theMark.setCsvCourseId(oldMarkObj.getCsvCourseId());
                theMark.setCsvStudentId(oldMarkObj.getCsvStudentId());
            }
            theMark.setMark_date(oldMarkObj.getMark_date());
        }

        Student theStudent = studentService.getStudentById(studentId);
        Course theCourse = courseService.getCourseById(courseId);


        theMark.setStudent(theStudent);
        theMark.setStudentName(theStudent.getStudentName());
        theMark.setCourse(theCourse);
        theMark.setCourseName(theCourse.getCourseName());

        return markRepository.save(theMark);
    }

    public Iterable<Mark> findAllMarks() {
        Iterable<Mark> allMarks = markRepository.findAll();
        return allMarks;
    }

    public double getAverageGradeOfStudentAcrossCourses(Long studentId) throws StudentNotFoundException {
        studentService.getStudentById(studentId);
        Integer studentIdV = Integer.valueOf(studentId.intValue());
//        DecimalFormat df = new DecimalFormat("#.##");
        Double avgGrade = markRepository.getAverageGradeOfStudentAcrossCourses(studentIdV);

        return avgGrade;
    }

    public double getAverageGradeOfStudentForCourse(Long studentId, Long courseId) throws StudentNotFoundException, CourseNotFoundException {
        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);

        Integer courseIdV = Integer.valueOf(courseId.intValue()); //TODO
        Integer studentIdV = Integer.valueOf(studentId.intValue()); //TODO
//        DecimalFormat df = new DecimalFormat("#.##");
        Double avgGradeForParticularCourse = markRepository.getAverageGradeOfStudentForCourse(studentIdV, courseIdV);

        return avgGradeForParticularCourse;
    }

    public double getAverageGradeForAllCoursesForAllStudents() {
        return markRepository.getAverageGradeForAllCoursesForAllStudents();
    }

    public List<IGroupedAverageResponse> getAllAveragesForAllStudentsPerCOurse() {
        List<IGroupedAverageResponse> av = markRepository.averages();

        return av;
    }

    public Mark getMarkById(Long markId) {
        Mark existingMark = markRepository.getById(markId);

        if (existingMark == null) {
            throw new MarkNotFoundException("Mark with ID " + markId + " does NOT exist in the system.");
        }

        return existingMark;
    }

    @Async("asyncExecutor")
    public void deleteMarkById(Long markId) {
//        System.out.println("deleteMarkById on Thread -> " + Thread.currentThread().getName());

        markRepository.delete(getMarkById(markId));
    }
}
