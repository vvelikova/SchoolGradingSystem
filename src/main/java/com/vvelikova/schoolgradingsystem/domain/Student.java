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
    @Column(unique = true)
    private String studentName;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
            mappedBy = "student", orphanRemoval = true)
    private List<Mark> marks;

    private Long csvId = 0L;

    private boolean isFromCSV = false;

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

    public Long getCsvId() {
        return csvId;
    }

    public void setCsvId(Long csvId) {
        this.csvId = csvId;
    }

    public boolean isFromCSV() {
        return isFromCSV;
    }

    public void setFromCSV(boolean fromCSV) {
        isFromCSV = fromCSV;
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
