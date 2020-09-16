package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.exceptions.*;
import com.vvelikova.schoolgradingsystem.customQueriesReturnObjects.IGroupedAverageResponse;
import com.vvelikova.schoolgradingsystem.services.MarkService;
import com.vvelikova.schoolgradingsystem.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.List;

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

    /** The average mark a single student has across all courses */
    @GetMapping("/average/allCourses/{student_id}")
    public ResponseEntity<?> getAverageAcrossAllCourses(@PathVariable Long student_id) {
        DecimalFormat df = new DecimalFormat("#.##");
        double avg;
        try {
            avg = markService.getAverageGradeOfStudentAcrossCourses(student_id);
        } catch (StudentNotFoundException ex) {
            throw new StudentNotFoundException(ex.getMessage());
        }

        return new ResponseEntity<String>("Average grade for student with ID " + student_id + " across all courses is " + df.format(avg), HttpStatus.OK);
    }

    /** The average mark a single student has in a single course  */
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

    /** The average mark all students were given in all courses*/
    @GetMapping("/average/allStudents/allCourses")
    public ResponseEntity<?> getAverageGradeForAllCoursesForAllStudents() {
        DecimalFormat df = new DecimalFormat("#.##");
        double averageGrade = markService.getAverageGradeForAllCoursesForAllStudents();

        return new ResponseEntity<String>("The average mark of all students in all courses is " + df.format(averageGrade), HttpStatus.OK);
    }

    /** The average mark for all existing combinations of a student and a course */
    @GetMapping("/averages")
    public List<IGroupedAverageResponse> getAverages(){
        return markService.getAllAveragesForAllStudentsPerCOurse();
    }

    @DeleteMapping("/{mark_id}")
    public ResponseEntity<?> deleteMarkById(@PathVariable Long mark_id) {
        markService.deleteMarkById(mark_id);

        return new ResponseEntity<String>("Mark with ID " + mark_id + " is successfully deleted.", HttpStatus.OK);
    }
}
