package com.vvelikova.schoolgradingsystem.helpers;


import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Student;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVCourseHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equalsIgnoreCase(file.getContentType())) {
            return false;
        }

        return true;
    }

    // read InputStream of a file, return a list of Students
    public static List<Course> csvToCourses(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withHeader("first_col","student_id","student_name","course_id","course_name","mark_id","mark","mark_date")
                             .withIgnoreHeaderCase().withTrim())) {

            List<Course> courses = new ArrayList<Course>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Long csvId = Long.parseLong(csvRecord.get("course_id"));

                Course course = new Course(csvRecord.get("course_name"));
                course.setCsvId(csvId);
                course.setFromCSV(true);

                /** if a course with the given name already exists, do NOT create new object.
                 * Name property must be unique */
                boolean courseNameExists = courses.stream().anyMatch(stu -> csvRecord.get("course_name").equalsIgnoreCase(stu.getCourseName()));
                if (!courseNameExists) {
                    courses.add(course);
                }
            }

            System.out.println("Student List size: " + courses.size());

            return courses;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream studentsToCSV(List<Student> students) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Student student : students) {
                List<String> data = Arrays.asList(student.getStudentName());

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}

