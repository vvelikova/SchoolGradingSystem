package com.vvelikova.schoolgradingsystem.controllers;

import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Student;
import com.vvelikova.schoolgradingsystem.helperResponses.CSVResponseMessage;
import com.vvelikova.schoolgradingsystem.helpers.CSVCourseHelper;
import com.vvelikova.schoolgradingsystem.helpers.CSVStudentHelper;
import com.vvelikova.schoolgradingsystem.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    private CSVService fileService;

    @PostMapping("/upload/students")
    public ResponseEntity<CSVResponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
        String msg = "";

        if(CSVStudentHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);

                msg = "The file was uploaded successfully: " + file.getOriginalFilename();
                return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.OK);
            } catch (Exception exc) {
                System.out.println(exc.getStackTrace());
                msg = "Could not upload file: " +file.getOriginalFilename() + "!!!";
                return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.EXPECTATION_FAILED);
            }
        }

        msg = "Please upload a CSV file.";
        return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/upload/courses")
    public ResponseEntity<CSVResponseMessage> uploadFileCourses(@RequestParam("file")MultipartFile file) {
        String msg = "";

        if(CSVCourseHelper.hasCSVFormat(file)) {
            try {
                fileService.saveCourses(file);

                msg = "The file was uploaded successfully: " + file.getOriginalFilename();
                return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.OK);
            } catch (Exception exc) {
                System.out.println(exc.getStackTrace());
                msg = "Could not upload file: " +file.getOriginalFilename() + "!!!";
                return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.EXPECTATION_FAILED);
            }
        }

        msg = "Please upload a CSV file.";
        return new ResponseEntity<>(new CSVResponseMessage(msg), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        try {
            Iterable<Student> students = fileService.getAllStudents();
            List<Student> studentList = new ArrayList<>();

            students.forEach(studentList::add);

            if (studentList.isEmpty()) {
                return new ResponseEntity<String>("No students found", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("An exception occurred ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   //    @GetMapping("/download")
//    public ResponseEntity<Resource> getFile() {
//        String filename = "tutorials.csv";
//        InputStreamResource file = new InputStreamResource(fileService.load());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//                .contentType(MediaType.parseMediaType("application/csv"))
//                .body(file);
//    }

}
