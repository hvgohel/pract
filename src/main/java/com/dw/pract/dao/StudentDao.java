package com.dw.pract.dao;

import java.util.List;

import com.dw.pract.model.Student;

public interface StudentDao extends AbstractDao<Student, Long>
{
    Student get(Long id);

    void delete(Long id);
    
    List<Student> getStudents(Long subjectId);
    
    List<Student> get(List<Long> ids);
    
    List<Student> get(String name);
}
