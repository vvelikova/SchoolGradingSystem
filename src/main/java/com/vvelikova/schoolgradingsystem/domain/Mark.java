package com.vvelikova.schoolgradingsystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.util.Date;

@Entity
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date mark_date;

    @DecimalMin(value = "2.00", message = "The lowest grade possible is 2.00")
    @DecimalMax(value = "6.00", message = "The highest grade possible is 6.00")
    @Digits(integer = 1, fraction = 2)
    private float mark;

    @ManyToOne
    @JoinColumn(name = "student_ref", referencedColumnName = "id")
    @JsonIgnore
    private Student student;

    private String studentName;

    @ManyToOne
    @JoinColumn(name = "mark_ref", referencedColumnName = "id")
    @JsonIgnore
    private Course course;

    private String courseName;

    private boolean isFromCSV = false;

    private Long csvStudentId;
    private Long csvCourseId;

    public Mark() {
    }

    public Mark(Date mark_date, @DecimalMin(value = "2.00", message = "The lowest grade possible is 2.00") @DecimalMax(value = "6.00", message = "The highest grade possible is 6.00") @Digits(integer = 1, fraction = 2) float mark) {
        this.mark_date = mark_date;
        this.mark = mark;
    }

    public Mark(@DecimalMin(value = "2.00", message = "The lowest grade possible is 2.00") @DecimalMax(value = "6.00", message = "The highest grade possible is 6.00") @Digits(integer = 1, fraction = 2) float mark) {
        this.mark = mark;
    }

    /** On creation of a mark, auto-generate the value of mark_date ONLY if the
     * mark is not coming from CSV file. Marks from CSV file should have their mark_date defined in the
     *  CSV file that is uploaded. */
    @PrePersist
    protected void onCreate() {
        if(!this.isFromCSV()) {
            this.mark_date = new Date();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMark_date() {
        return mark_date;
    }

    public void setMark_date(Date mark_date) {
        this.mark_date = mark_date;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isFromCSV() {
        return isFromCSV;
    }

    public void setFromCSV(boolean fromCSV) {
        isFromCSV = fromCSV;
    }

    public Long getCsvStudentId() {
        return csvStudentId;
    }

    public void setCsvStudentId(Long csvStudentId) {
        this.csvStudentId = csvStudentId;
    }

    public Long getCsvCourseId() {
        return csvCourseId;
    }

    public void setCsvCourseId(Long csvCourseId) {
        this.csvCourseId = csvCourseId;
    }
}
