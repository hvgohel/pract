package com.dw.pract.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dw.pract.dao.StudentDao;
import com.dw.pract.exception.IllegalArgumentException;
import com.dw.pract.model.Student;
import com.dw.pract.model.Subject;
import com.dw.pract.repository.SubjectRepository;
import com.dw.pract.service.SubjectService;
import com.dw.pract.utils.BeanMapper;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class SubjectServiceImpl implements SubjectService {
  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private StudentDao studentDao;

  @Autowired
  private BeanMapper beanMapper;

  @Override
  public Subject add(Subject subject) {
    subjectRepository.save(subject);
    return beanMapper.map(subject, Subject.class);
  }

  @Override
  public Subject update(Subject subject) {
    Subject sub = subjectRepository.findOne(subject.getId());
    if (sub == null) {
      throw new IllegalArgumentException("51", "Given subject id is not exist");
    }

    beanMapper.map(subject, sub, "subject-1");
    subjectRepository.save(sub);
    return beanMapper.map(sub, Subject.class);
  }

  @Override
  public void delete(Long id) {
    Subject subject = subjectRepository.findOne(id);
    if (subject == null) {
      throw new IllegalArgumentException("41", "Given subject id is not exist");
    }
    subjectRepository.delete(subject);
  }

  @Override
  public List<Subject> get() {
    List<Subject> subjects = subjectRepository.findAll();
    return beanMapper.mapCollection(subjects, Subject.class, "subject-2");
  }

  @Override
  public List<Subject> getSubjects(Long studentId) {
    List<Subject> list = subjectRepository.findBystudents_id(studentId);
    return beanMapper.mapCollection(list, Subject.class, "subject-2");
  }

  @Override
  public List<Student> getStudents(Long subjectId) {
    List<Student> list = studentDao.getStudents(subjectId);
    return beanMapper.mapCollection(list, Student.class, "student-3");
  }

  @Override
  public Subject getSubject(Long id) {
    Subject sub = subjectRepository.findOne(id);
    return beanMapper.map(sub, Subject.class, "subject-1");
  }

  @Override
  public List<Subject> getSub(List<Long> ids) {
    List<Subject> list;
    if (ids == null) {
      list = subjectRepository.findAll();
    } else {
      list = subjectRepository.findByIdIn(ids);
    }

    return beanMapper.mapCollection(list, Subject.class, "subject-1");
  }

  @Override
  public List<Subject> getSub1(List<Long> studentIds) {
    List<Subject> list = subjectRepository.findBystudents_idIn(studentIds);
    return beanMapper.mapCollection(list, Subject.class, "subject-3");
  }
}
