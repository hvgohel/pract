package com.dw.pract.config;

public class WebConfig {
  public static final String CREATE_STUDENT = "/test/student"; // POST
  public static final String CREATE_MULTIPLE_STUDENT = "/test/students"; // POST
  public static final String DELETE_STUDENT = "/test/student/{id}"; // DELETE
  public static final String DELETE_ALL_STUDENT = "/test/students"; // DELETE
  public static final String UPDATE_STUDENT = "/test/student/{id}"; // UPDATE
  public static final String ASSIGN_SUBJECT = "/test/student/{studentId}/{subjectId}"; // POST
  public static final String GET_STUDENT_BY_IDS = "/test/student"; // GET
  public static final String UPDATE_STUDENT_BY_ID = "/test/{id}/student"; // PATCH
  public static final String GET_STUDENT_BY_NAME = "/test/student/{name}"; // GET

  public static final String ADD_NEW_ADDRESS = "/test/address"; // POST

  public static final String ADD_NEW_SUBJECT = "/test/subject"; // POST
  public static final String UPDATE_SUBJECT = "/test/subject/{id}"; // PUT
  public static final String DELETE_SUBJECT = "/test/subject/{id}"; // DELETE
  public static final String GET_SUBJECTS = "/test/subjects"; // GET
  public static final String GET_SUBJECTS_BY_STUDENT = "/test/subjects/{studentId}"; // GET
  public static final String GET_STUDENTS_BY_SUBJECT = "/test/students/{subjectId}"; // GET
  public static final String GET_SUBJECT = "/test/subject/{id}"; // GET
  public static final String GET_SUBJECTS_BY_ID = "/test/subject"; // GET
  public static final String GET_SUBJECTS_BY_STUDENTS = "/test/students-subjects"; // GET
}
