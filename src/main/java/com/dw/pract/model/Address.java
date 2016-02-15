package com.dw.pract.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@Entity(name = "address")
@JsonSerialize(include=Inclusion.NON_NULL)
public class Address implements com.dw.pract.model.Entity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long   id;

    private String city;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Override
    public boolean hasValidId()
    {
        return id != null && id > 0;
    }
}
