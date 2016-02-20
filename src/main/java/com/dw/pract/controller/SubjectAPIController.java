package com.dw.pract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dw.pract.config.WebConfig;
import com.dw.pract.model.Student;
import com.dw.pract.model.Subject;
import com.dw.pract.service.SubjectService;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SubjectAPIController extends BaseAPIController {
  @Autowired
  private SubjectService subjectService;

  @RequestMapping(value = WebConfig.ADD_NEW_SUBJECT, method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Subject add(@RequestBody Subject subject) {
    return subjectService.add(subject);
  }

  @RequestMapping(value = WebConfig.UPDATE_SUBJECT, method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Subject update(@PathVariable(value = "id") Long id, @RequestBody Subject subject) {
    subject.setId(id);
    return subjectService.update(subject);
  }

  @RequestMapping(value = WebConfig.DELETE_SUBJECT, method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable(value = "id") Long id) {
    subjectService.delete(id);
  }

  @RequestMapping(value = WebConfig.GET_SUBJECTS, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Subject> get() {
    return subjectService.get();
  }

  @RequestMapping(value = WebConfig.GET_SUBJECTS_BY_STUDENT, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Subject> getSubjects(@PathVariable(value = "studentId") Long studentId) {
    return subjectService.getSubjects(studentId);
  }

  @RequestMapping(value = WebConfig.GET_STUDENTS_BY_SUBJECT, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Student> getStudents(@PathVariable(value = "subjectId") Long subjectId) {
    return subjectService.getStudents(subjectId);
  }

  @RequestMapping(value = WebConfig.GET_SUBJECT, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Subject getSubject(@PathVariable(value = "id") Long id) {
    return subjectService.getSubject(id);
  }

  @RequestMapping(value = WebConfig.GET_SUBJECTS_BY_ID, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Subject> getSub(@RequestParam(value = "id", required = false) List<Long> id) {
    return subjectService.getSub(id);
  }

  @RequestMapping(value = WebConfig.GET_SUBJECTS_BY_STUDENTS, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Subject> getSub1(@RequestParam(value = "studentId", required = false) List<Long> studentIds) {
    return subjectService.getSub1(studentIds);
  }
}
