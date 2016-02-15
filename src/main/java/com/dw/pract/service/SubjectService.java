package com.dw.pract.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dw.pract.model.Student;
import com.dw.pract.model.Subject;

@Service
public interface SubjectService
{
    Subject add(Subject subject);

    Subject update(Subject subject);

    void delete(Long id);

    List<Subject> get();
    
    List<Subject> getSubjects(Long studentId);
    
    List<Student> getStudents(Long subjectId);
    
    Subject getSubject(Long id);
    
    List<Subject> getSub(List<Long> ids);
    
    List<Subject> getSub1(List<Long> studentIds);
}
