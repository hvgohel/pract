package com.dw.pract.repository;

import org.springframework.data.repository.CrudRepository;

import com.dw.pract.model.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

}
