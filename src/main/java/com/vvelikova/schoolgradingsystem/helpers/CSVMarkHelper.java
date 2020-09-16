package com.vvelikova.schoolgradingsystem.helpers;

import com.vvelikova.schoolgradingsystem.domain.Mark;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVMarkHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equalsIgnoreCase(file.getContentType())) {
            return false;
        }

        return true;
    }

    /**
     * Read InputStream of a file, return a list of Marks
     */
    public static List<Mark> csvToMark(InputStream is) throws ParseException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withHeader("first_col", "student_id", "student_name", "course_id", "course_name", "mark_id", "mark", "mark_date")
                             .withIgnoreHeaderCase().withTrim())) {

            List<Mark> marks = new ArrayList<Mark>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Mark mark = new Mark(Float.valueOf(csvRecord.get("mark")));
                mark.setCsvStudentId(Long.parseLong(csvRecord.get("student_id")));
                mark.setCsvCourseId(Long.parseLong(csvRecord.get("course_id")));
                mark.setFromCSV(true);

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date markDateCSV = formatter.parse(csvRecord.get("mark_date"));
                mark.setMark_date(markDateCSV);

                marks.add(mark);
            }

            return marks;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }
}
