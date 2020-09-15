package com.vvelikova.schoolgradingsystem.helpers;

import com.vvelikova.schoolgradingsystem.domain.Student;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVStudentHelper {
    public static String TYPE = "text/csv";
//    static String[] HEADERs = {"student_id", "student_name"};

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equalsIgnoreCase(file.getContentType())) {
            return false;
        }

        return true;
    }

    // read InputStream of a file, return a list of Students
    public static List<Student> csvToStudents(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withHeader("first_col","student_id","student_name","course_id","course_name","mark_id","mark","mark_date")
                             .withIgnoreHeaderCase().withTrim())) {

            List<Student> students = new ArrayList<Student>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            int count = 1;
            for (CSVRecord csvRecord : csvRecords) {
                Long csvId = Long.parseLong(csvRecord.get("student_id"));

                Student student = new Student(csvRecord.get("student_name"));
                student.setCsvId(csvId);
                student.setFromCSV(true);

                /** if a student with the given name already exists, do NOT create new object.
                 * Name property must be unique */
                boolean studentNameExists = students.stream().anyMatch(stu -> csvRecord.get("student_name").equalsIgnoreCase(stu.getStudentName()));
                if (!studentNameExists) {
                    students.add(student);
                }

            }

            System.out.println("Student List size: " + students.size());

            return students;
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
