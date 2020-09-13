package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.exceptions.StudentErrorResponse;
import com.vvelikova.schoolgradingsystem.exceptions.StudentNotFoundException;
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

    @PostMapping("/{student_id}")
    public ResponseEntity<?> addMarkForStudent(@Valid @PathVariable Long student_id,
                                               @RequestBody Mark theMark, BindingResult result) {
        ResponseEntity<?> error = validationErrorService.ValidationErrorService(result);

        if (error != null) {
            return error;
        }

        Mark mark;
        try {
            mark = markService.addMark(student_id, theMark);
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException(ex.getMessage());
        }

        return new ResponseEntity<>(mark, HttpStatus.CREATED);
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

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}
