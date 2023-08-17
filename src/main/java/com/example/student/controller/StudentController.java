

package com.example.student.controller;

import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;



@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/students")
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        
        if (student.getFirstName().length() < 3 || student.getLastName().length() < 3) {
            return ResponseEntity.badRequest().body("First and last names must have a min 3 characters");
        }
        
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - student.getDob().getYear();
        if (age <= 15 || age > 20) {
            return ResponseEntity.badRequest().body("Age must be greater than 15 and less than or equal to 20");
        }
        
        if (student.getMarks() < 0 || student.getMarks() > 100) {
            return ResponseEntity.badRequest().body("Marks must be in  range of 0 to 100");
        }
        
        if (!"A".equals(student.getSection()) && !"B".equals(student.getSection()) && !"C".equals(student.getSection())) {
            return ResponseEntity.badRequest().body("Invalid section, Valid sections are A, B, and C");
        }
        
        if (!"M".equals(student.getGender()) && !"F".equals(student.getGender())) {
            return ResponseEntity.badRequest().body("Invalid gender, Valid genders are M and F.");
        }
        
        
        int totalMarks = student.getMarks();
        double averageMarks = totalMarks / 1.0; 
        String result = totalMarks >= 35 ? "Pass" : "Fail";

        student.setTotal(totalMarks);
        student.setAverage(averageMarks);
        student.setResult(result);

        studentRepository.save(student);

        return ResponseEntity.ok("Student created successfully.");
    }

    

    @PutMapping("/students/update-marks")
    public ResponseEntity<String> updateStudentMarks(
            @RequestParam Long studentId,
            @RequestParam int marks1,
            @RequestParam int marks2,
            @RequestParam int marks3) {

        Student student = studentRepository.findById(studentId)
                .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        if (marks1 < 0 || marks1 > 100 || marks2 < 0 || marks2 > 100 || marks3 < 0 || marks3 > 100) {
            return ResponseEntity.badRequest().body("Marks must be in the range of 0 to 100.");
        }

      
        student.setMarks(marks1 + marks2 + marks3);
        student.setTotal(student.getMarks());
        student.setAverage(student.getMarks() / 3.0); 
        student.setResult(student.getMarks() >= 35 * 3 ? "Pass" : "Fail");

        studentRepository.save(student);

        return ResponseEntity.ok("Student marks updated successfully.");
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        
        Student student = studentRepository.findById(studentId)
                .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }
}


