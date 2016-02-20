package com.dw.pract.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@Entity(name = "student")
@JsonSerialize(include = Inclusion.NON_NULL)
// @EntityListeners(StudentNameListener.class)
public class Student implements com.dw.pract.model.Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @Column(name = "birthdate")
  @Temporal(TemporalType.DATE)
  private Date birthDate;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "address")
  private Address address;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "student_subject", joinColumns = {@JoinColumn(name = "studentid")},
      inverseJoinColumns = {@JoinColumn(name = "subjectid")})
  private Set<Subject> subjects;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Set<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(Set<Subject> subjects) {
    this.subjects = subjects;
  }

  @Override
  public boolean hasValidId() {
    return id != null && id > 0;
  }

  @Override
  public String toString() {
    return "[ " + id + ", " + name + " ]";
  }
}
