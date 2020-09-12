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
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonIgnore
    private Student student;

    public Mark() {
    }

    public Mark(Date mark_date, @DecimalMin(value = "2.00", message = "The lowest grade possible is 2.00") @DecimalMax(value = "6.00", message = "The highest grade possible is 6.00") @Digits(integer = 1, fraction = 2) float mark) {
        this.mark_date = mark_date;
        this.mark = mark;
    }

    @PrePersist
    protected void onCreate() {
        this.mark_date = new Date();
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
}
