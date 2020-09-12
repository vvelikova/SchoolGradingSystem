package com.vvelikova.schoolgradingsystem.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "The name of the student is required.")
    private String studentName;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
            mappedBy = "student", orphanRemoval = true)
    private List<Mark> marks;

    public Student() {
    }

    public Student(@NotBlank(message = "The name of the student is required.") String studentName) {
        this.studentName = studentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }


    // convenience method for bi-directional relationship
    public void add(Mark tempMark) {
        if (marks == null) {
            marks = new ArrayList<>();
        }

        marks.add(tempMark);
        tempMark.setStudent(this);
    }
}
