package com.dw.pract.listener;

import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dw.pract.model.Student;

public class StudentNameListener
{
    private static Logger logger = LoggerFactory.getLogger(StudentNameListener.class);

    @PreUpdate
    public void preUpdate(Student student)
    {
        logger.info("preUpdate() :: start");
        student.setName("HGohel");
        logger.info("preUpdate() :: end");
    }
}
