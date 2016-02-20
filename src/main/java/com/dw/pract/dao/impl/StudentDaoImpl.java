package com.dw.pract.dao.impl;

import java.util.List;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dw.pract.dao.StudentDao;
import com.dw.pract.model.Student;
import com.dw.pract.model.Subject;
import com.dw.pract.service.impl.AbstractDaoImpl;

@Named
@Transactional(propagation = Propagation.REQUIRED)
public class StudentDaoImpl extends AbstractDaoImpl<Student, Long> implements StudentDao {
  @Override
  public List<Student> getStudents(Long subjectId) {
    CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Student> query = builder.createQuery(Student.class);
    Root<Student> root = query.from(Student.class);
    Join<Student, Subject> subJoin = root.join("subjects");
    query.where(builder.equal(subJoin.get("id"), subjectId));
    return getEntityManager().createQuery(query).getResultList();
  }

  @Override
  public List<Student> get(List<Long> ids) {
    CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Student> query = builder.createQuery(Student.class);
    Root<Student> root = query.from(Student.class);
    query.where(root.in(ids));
    return getEntityManager().createQuery(query).getResultList();
  }

  @Override
  public List<Student> get(String name) {
    CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Student> query = builder.createQuery(Student.class);
    Root<Student> root = query.from(Student.class);
    query.where(builder.like(root.get("name"), "%" + name + "%"));
    return getEntityManager().createQuery(query).getResultList();
  }

}
