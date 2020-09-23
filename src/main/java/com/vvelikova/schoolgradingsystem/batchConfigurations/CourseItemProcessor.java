package com.vvelikova.schoolgradingsystem.batchConfigurations;

import com.vvelikova.schoolgradingsystem.domain.Course;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class CourseItemProcessor implements ItemProcessor<Course, Course> {

    private Set<Course> seenCourses = new HashSet<>();

    @Override
    public Course process(Course course) throws Exception {
        System.out.println("In courseItem processor process method");
        if(seenCourses.contains(course)) {
            return null;
        }

        seenCourses.add(course);

        return course;
    }
}
