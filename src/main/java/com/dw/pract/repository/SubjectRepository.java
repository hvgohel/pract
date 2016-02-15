package com.dw.pract.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.dw.pract.model.Subject;

public interface SubjectRepository extends CrudRepository<Subject, Long>, JpaRepository<Subject, Long>
{
    List<Subject> findBystudents_id(Long id);

    List<Subject> findByIdIn(List<Long> ids);

    // List<Student> findBysubjects_id(Long id);

    List<Subject> findBystudents_idIn(List<Long> ids);
}
