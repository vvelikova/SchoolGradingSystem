package com.vvelikova.schoolgradingsystem.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name of the course is required.")
    @Column(unique = true)
    private String courseName;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
            mappedBy = "course", orphanRemoval = true)
    public List<Mark> marks;

    private Long csvId = 0L;

    private boolean fromCSV = false;

    public Course() {}

    public Course (String name) {
        this.courseName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
        return fromCSV;
    }

    public void setFromCSV(boolean fromCSV) {
        fromCSV = fromCSV;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", marks=" + marks +
                ", csvId=" + csvId +
                ", isFromCSV=" + fromCSV +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }

        else if (obj.getClass() != this.getClass()){
            return false;
        }

        final Course other = (Course) obj;

        if ((this.courseName == null) ? (other.courseName != null) : !this.courseName.equals((other.courseName))) {
            return false;
        }

        return true;
    }
}
