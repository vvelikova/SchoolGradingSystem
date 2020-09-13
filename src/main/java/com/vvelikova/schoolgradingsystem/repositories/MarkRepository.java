package com.vvelikova.schoolgradingsystem.repositories;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import com.vvelikova.schoolgradingsystem.customQueriesReturnObjects.IGroupedAverageResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarkRepository extends CrudRepository<Mark, Long> {

    List<Mark> findByStudentId(Long id);

    Mark getById(Long id);

    @Override
    Iterable<Mark> findAll();

    @Query(value = "SELECT AVG(Mark.mark) FROM Mark where Mark.student_ref = ?1",
            nativeQuery = true)
    Double getAverageGradeOfStudentAcrossCourses(int student_id);

    @Query(value = "SELECT AVG(Mark.mark) FROM Mark where Mark.student_ref = ?1 AND Mark.mark_ref = ?2",
            nativeQuery = true)
    Double getAverageGradeOfStudentForCourse(int student_id, int course_id);

    @Query(value = "SELECT AVG(Mark.mark) FROM Mark",
            nativeQuery = true)
    Double getAverageGradeForAllCoursesForAllStudents();

    @Query(value = "SELECT Mark.student_name AS studentName, Mark.course_name AS courseName, AVG(Mark.mark) AS averageMark FROM Mark GROUP BY Mark.student_ref, Mark.mark_ref", nativeQuery = true)
    List<IGroupedAverageResponse> averages();

}
