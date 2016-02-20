package com.dw.pract.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dw.pract.config.WebConfig;
import com.dw.pract.model.Address;
import com.dw.pract.model.Student;
import com.dw.pract.repository.StudentRepository;
import com.dw.pract.service.StudentService;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StudentAPIController extends BaseAPIController {
  private static Logger logger = LoggerFactory.getLogger(StudentAPIController.class);

  @Inject
  private StudentService studentService;

  @Inject
  private StudentRepository studentRepository;

  @RequestMapping(value = WebConfig.CREATE_STUDENT, method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student add(@RequestBody Student student) {
    return studentService.add(student);
  }

  @RequestMapping(value = WebConfig.CREATE_MULTIPLE_STUDENT, method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void add(@RequestBody List<Student> students) {
    studentService.add(students);
  }

  @RequestMapping(value = WebConfig.UPDATE_STUDENT, method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student update(@RequestBody Student student, @PathVariable(value = "id") Long id) {
    student.setId(id);
    return studentService.update(student);
  }

  @RequestMapping(value = WebConfig.DELETE_STUDENT, method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable(value = "id") Long id) {
    studentService.delete(id);
  }

  @RequestMapping(value = WebConfig.DELETE_ALL_STUDENT, method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete() {
    studentRepository.deleteAll();
  }

  @RequestMapping(value = WebConfig.ADD_NEW_ADDRESS, method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Address addAddress(@RequestBody Address address) {
    return studentService.add(address);
  }

  @RequestMapping(value = WebConfig.ASSIGN_SUBJECT, method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student assignSubject(@PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "subjectId") Long subjectId) {
    return studentService.assign(studentId, subjectId);
  }

  @RequestMapping(value = WebConfig.GET_STUDENT_BY_IDS, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<Student> get(@RequestParam(value = "id") List<Long> ids) {
    return studentService.get(ids);
  }

  @RequestMapping(value = WebConfig.GET_STUDENT_BY_NAME, method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<Student> get(@PathVariable(value = "name") String name, Model model) {
    return studentService.get(name);
  }

  @RequestMapping(value = "/test/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public int getCount() {
    return studentService.getCount();
  }

  @RequestMapping(value = WebConfig.UPDATE_STUDENT_BY_ID, method = RequestMethod.PATCH,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student update(@PathVariable(value = "id") Long id) {
    return studentService.update(id);
  }

  @RequestMapping(value = "/test/scheduling", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void get() {
    studentService.generate();
  }

  @RequestMapping(value = "/test/reactiveX", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void addStudentUsingReactiveX(@RequestBody List<Student> students) {
    studentService.reactiveX(students);
  }
}
