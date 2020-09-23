package com.vvelikova.schoolgradingsystem.batchConfigurations;

import com.vvelikova.schoolgradingsystem.domain.Student;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class StudentItemProcessor implements ItemProcessor<Student, Student>{

    private Set<Student> seenStudents = new HashSet<>();

    @Override
    public Student process(Student student) throws Exception {
        if(seenStudents.contains(student)) {
            return null;
        }

        seenStudents.add(student);

        return student;
    }
}
