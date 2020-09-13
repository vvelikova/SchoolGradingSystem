package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.exceptions.*;
import com.vvelikova.schoolgradingsystem.services.MarkService;
import com.vvelikova.schoolgradingsystem.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;

@RestController
@RequestMapping("/api/mark")
public class MarkController {

    @Autowired
    private MarkService markService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/{student_id}/{course_id}")
    public ResponseEntity<?> addMarkForStudent(@Valid @PathVariable Long student_id, @PathVariable Long course_id,
                                               @RequestBody Mark theMark, BindingResult result) {
        ResponseEntity<?> error = validationErrorService.ValidationErrorService(result);

        if (error != null) {
            return error;
        }

        Mark mark;
        try {
            mark = markService.addMark(student_id, course_id, theMark);
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException(ex.getMessage());
        }

        return new ResponseEntity<>(mark, HttpStatus.CREATED);
    }

    @GetMapping("/{mark_id}")
    public ResponseEntity<?> getMarkById(@PathVariable Long mark_id) {
        Mark theMark = markService.getMarkById(mark_id);
        return new ResponseEntity<>(theMark, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Mark> getAllMarks() {
        return markService.findAllMarks();
    }

    @GetMapping("/average/allCourses/{student_id}")
    public ResponseEntity<?> getAverageAcrossAllCourses(@PathVariable Long student_id) {
        DecimalFormat df = new DecimalFormat("#.##");
        double avg;
        try {
            avg = markService.getAverageGradeOfStudentAcrossCourses(student_id);
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException(ex.getMessage());
        }

        System.out.println("AVERAGE GRADE is " + avg);

        return new ResponseEntity<String>("Average grade for student with ID " + student_id + " across all courses is " + df.format(avg), HttpStatus.OK);
    }

    @GetMapping("/average/{student_id}/{course_id}")
    public ResponseEntity<?> getAverageGradeOfStudentForCourse(@PathVariable Long student_id, Long course_id) {
        DecimalFormat df = new DecimalFormat("#.##");
        double avgForCourse;
        try {
            avgForCourse = markService.getAverageGradeOfStudentForCourse(student_id, course_id);
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException(ex.getMessage());
        } catch (MarkNotFoundException exc) {
            throw new MarkNotFoundException(exc.getMessage());
        }


        return new ResponseEntity<String>("Average grade for student with ID " + student_id + " for course with ID " + course_id + " is " + df.format(avgForCourse), HttpStatus.OK);
    }

    @DeleteMapping("/{mark_id}")
    public ResponseEntity<?> deleteMarkById(@PathVariable Long mark_id) {
        markService.deleteMarkById(mark_id);

        return new ResponseEntity<String>("Mark with ID " + mark_id + " is successfully deleted.", HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MarkErrorResponse> handleException(MarkNotFoundException exc) {
        MarkErrorResponse error = new MarkErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
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
