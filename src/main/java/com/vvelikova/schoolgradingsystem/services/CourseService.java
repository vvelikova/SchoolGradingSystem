package com.vvelikova.schoolgradingsystem.services;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.exceptions.CourseNotFoundException;
import com.vvelikova.schoolgradingsystem.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    @Lazy
    private MarkService markService;

    /** saveUpdateCourse() handles the update ONLY of the name of the Course.
     *  If there is a need for a certain mark to be changed, the methods exposed by the MarkService should be used.
     *  saveUpdateCourse() handles the update of the linked marks a Course has */
    public Course saveUpdateCourse(Course theCourse) {

        if (theCourse.getId() != null) {
            Course existingCourse = courseRepository.getById(theCourse.getId());

            if (existingCourse == null) {
                throw new CourseNotFoundException("Course with ID: " + theCourse.getId() + " does NOT exist in the system.");
            }

            List<Mark> courseMarks = existingCourse.getMarks();
            List<Mark> updatedMarkInfo = new ArrayList<>();

            for (Mark obj: courseMarks) {
                Mark tempMark = markService.getMarkById(obj.getId());
                tempMark.setCourseName(theCourse.getCourseName());
                tempMark.setCourse(theCourse);
                tempMark.setMark_date(new Date());
                updatedMarkInfo.add(tempMark);
            }
            theCourse.setMarks(updatedMarkInfo);
        }

        return courseRepository.save(theCourse);
    }

    public Course getCourseById(Long id) {
        Course existingCourse = courseRepository.getById(id);

        if (existingCourse == null) {
            throw new CourseNotFoundException("Course with ID " + id + " does NOT exist in the system.");
        }

        return existingCourse;
    }

    @Async("asyncExecutor")
    public void deleteCourseById(Long id) {
//        System.out.println("DeleteCourseById on Thread -> " + Thread.currentThread().getName());
        courseRepository.delete(getCourseById(id));
    }

    public Iterable<Course> findAllCourses() {
        return courseRepository.findAll();

    }
}
