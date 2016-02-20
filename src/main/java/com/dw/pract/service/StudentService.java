package com.dw.pract.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dw.pract.model.Address;
import com.dw.pract.model.Student;

@Service
public interface StudentService {
  Student add(Student student);

  Student update(Student student);

  void delete(Long id);

  Address add(Address address);

  Student assign(Long studentId, Long subjectId);

  List<Student> get(List<Long> ids);

  List<Student> get(String name);

  int getCount();

  void add(List<Student> students);

  void asyncAdd(Student student);

  void syncAdd(Student student);

  Student update(Long id);

  void updateStud(Student student);

  void updateStud2(Student student);

  void generate();

  void reactiveX(List<Student> students);

  List<Student> asyncAdd(List<Student> student);
}
