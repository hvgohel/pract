package com.dw.pract.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dw.pract.PractApplication;
import com.dw.pract.dao.AddressDao;
import com.dw.pract.dao.StudentDao;
import com.dw.pract.exception.IllegalArgumentException;
import com.dw.pract.exception.ResourceNotFoundException;
import com.dw.pract.model.Address;
import com.dw.pract.model.FacetNode;
import com.dw.pract.model.FacetResult;
import com.dw.pract.model.Student;
import com.dw.pract.model.Subject;
import com.dw.pract.repository.SubjectRepository;
import com.dw.pract.service.StudentService;
import com.dw.pract.utils.BeanMapper;
import com.dw.pract.utils.BeanUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rx.Observable;
import rx.Subscriber;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class StudentServiceImpl implements StudentService
{
    private static final Logger logger = LoggerFactory.getLogger(PractApplication.class);

    @Inject
    private BeanMapper          beanMapper;

    @Inject
    private StudentDao          studentDao;

    @Inject
    private AddressDao          addressDao;

    @Inject
    private SubjectRepository   subjectRepository;

    @Inject
    private ObjectMapper        objectMapper;

    @Inject
    private ResourceLoader      resourceLoader;

    @Override
    public Student add(Student student)
    {

        logger.info("add() :: student creation start");
        logger.debug("add() :: student creation start");
        if (student == null)
        {
            throw new IllegalArgumentException("1", "Request body should not be null");
        }

        if (student.getAddress() == null)
        {
            // throw new IllegalArgumentException("2", "address should not be null");
        }
        
        Set<Subject> subjects = student.getSubjects();
        
        studentDao.save(student);
        student = beanMapper.map(student, Student.class, "student-2");

        try
        {
            Thread.sleep(1l);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        final String name = student.getName();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
        {
            @Override
            public void afterCommit()
            {
                logger.info("add() :: Student " + name + " created successfully ");
                try
                {
                    Thread.sleep(1l);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        return student;
    }

    @Override
    public void add(List<Student> students)
    {
        logger.info("add() multiple student add method starting");
        for (Student s : students)
        {
            BeanUtils.only().getStudentService().asyncAdd(s);
            // BeanUtils.only().getStudentService().syncAdd(s);
            logger.debug("student {} created successfully", s.getName());
        }
        logger.info("add() add multiple student method end");
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void asyncAdd(Student student)
    {
        logger.debug("asyncAdd() :: async starting");
        add(student);
        logger.debug("asyncAdd() :: async complete");
    }

    public synchronized void syncAdd(Student student)
    {
        logger.debug("syncAdd() :: sync starting");
        add(student);
        logger.debug("syncAdd() :: sync complete");
    }

    @Override
    public Student update(Long id)
    {
        logger.info("update() :: update student method starting");
        // get student by id
        Student student = studentDao.get(id);

        // update student detail
        BeanUtils.only().getStudentService().updateStud(student);

        updateStud2(student);

        logger.info("get() :: get id by student name is " + student.getName());
        logger.info("get() :: get id by student method end ");

        return beanMapper.map(student, Student.class, "student-3");
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateStud(Student student)
    {
        logger.info("updateStud() :: student update starting");
        logger.info("updateStud() :: old student name is " + student.getName());
        student.setName("Hiren");
        studentDao.save(student);

        try
        {
            Thread.sleep(5000l);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("updateStud() :: new student name is " + student.getName());
        logger.info("updateStud() :: student update end");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateStud2(Student student)
    {
        logger.info("updateStud2() :: student update2 starting");
        logger.info("updateStud2() :: old student name is " + student.getName());
        student.setName("Hiren2");
        studentDao.save(student);
        logger.info("updateStud2() :: new student name is " + student.getName());
        logger.info("updateStud2() :: student update2 end");
    }

    @Override
    public Student update(Student student)
    {
        Student stud = studentDao.get(student.getId());
        if (stud == null)
        {
            throw new ResourceNotFoundException("11", "Given student id is not exist");
        }
        beanMapper.map(student, stud, "student-1");
        studentDao.save(stud);
        return beanMapper.map(stud, Student.class);
    }

    @Override
    public void delete(Long id)
    {
        Student stud = studentDao.get(id);
        if (stud == null)
        {
            throw new ResourceNotFoundException("21", "Given student id is not exist");
        }
        studentDao.delete(stud);
    }

    @Override
    public Address add(Address address)
    {
        addressDao.save(address);
        return beanMapper.map(address, Address.class);
    }

    @Override
    public Student assign(Long studentId, Long subjectId)
    {
        Student stud = studentDao.get(studentId);
        if (stud == null)
        {
            throw new ResourceNotFoundException("31", "Given student id is not exist");
        }

        Subject sub = subjectRepository.findOne(subjectId);
        if (sub == null)
        {
            throw new ResourceNotFoundException("32", "Given subject id is not exist");
        }

        Set<Subject> studentSubject = stud.getSubjects();
        studentSubject.add(sub);
        stud.setSubjects(studentSubject);
        studentDao.save(stud);

        Set<Student> students = sub.getStudents();
        students.add(stud);
        sub.setStudents(students);

        return beanMapper.map(stud, Student.class, "student-2");
    }

    @Override
    public List<Student> get(List<Long> ids)
    {
        List<Student> list = studentDao.get(ids);
        return beanMapper.mapCollection(list, Student.class, "student-3");
    }

    @Override
    public List<Student> get(String name)
    {
        List<Student> list = studentDao.get(name);
        return beanMapper.mapCollection(list, Student.class, "student-3");
    }

    @Override
    public int getCount()
    {
        String path = "classpath:plan-criteria.json";

        List<FacetResult> facetResults = null;

        try
        {
            Resource criteriaResource = resourceLoader.getResource(path);

            facetResults = objectMapper.readValue(criteriaResource.getInputStream(),
                    new TypeReference<List<FacetResult>>()
                    {
                    });

            String o = objectMapper.writeValueAsString(facetResults);
            logger.debug(o);
        }
        catch (Exception e)
        {
            logger.debug(" write and read operation failed : ", e);
        }

        int count = 0;
        for (FacetResult facetResult : facetResults)
        {
            System.out.println("type : " + facetResult.getType());
            for (FacetNode facetNode : facetResult.getFacetNodes())
            {
                count = this.getCnt(facetNode, count);
                System.out.println(count);
            }
        }

        return count;
    }

    private Integer getCnt(FacetNode facetNode, int count)
    {
        if (BooleanUtils.isTrue(facetNode.isSelected()))
        {
            count++;
        }
        if (facetNode.getNodes() == null || facetNode.getNodes().isEmpty())
        {
            return count;
        }
        for (FacetNode f : facetNode.getNodes())
        {
            count = getCnt(f, count);
        }
        return count;
    }

    // @Scheduled(fixedRate = 2000)
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void generate()
    {
        TaskScheduler taskScheduler = null;
        taskScheduler.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                logger.info("generate() :: invoke " + new Date(System.currentTimeMillis()));
            }
        }, new CronTrigger("*/5 * * * * MON-FRI"));
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Student> asyncAdd(List<Student> students)
    {
        List<Student> students2 = new ArrayList<>();
        for (Student s : students)
        {
            Student student = add(s);
            students2.add(student);
        }
        return students2;
    }

    @Override
    public void reactiveX(List<Student> students)
    {
        logger.info("reactiveX() :: starting");

        /*
         * List<Student> students2 = BeanUtils.only().getStudentService().asyncAdd(students);
         * 
         * Observable<Student> observable = Observable.from(students2);
         * 
         * observable.subscribe(new Subscriber() {
         * 
         * @Override public void onCompleted() { logger.info("reactiveX() :: Completed");
         * 
         * }
         * 
         * @Override public void onError(Throwable arg0) { logger.info("Error : " + arg0); }
         * 
         * @Override public void onNext(Object arg0) { logger.info("Next : " + arg0.toString()); } });
         */

        // observable.subscribe(tweet -> System.out.println("Subscriber 1 >> " + tweet));

        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> observer)
            {
                try
                {
                    if (!observer.isUnsubscribed())
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            observer.onNext(i);
                        }
                        observer.onCompleted();
                    }
                }
                catch (Exception e)
                {
                    observer.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>()
        {
            @Override
            public void onNext(Integer item)
            {
                logger.debug("Next: {}", item);
                logger.info("Next: " + item);
            }

            @Override
            public void onError(Throwable error)
            {
                logger.debug("Error: {} ", error.getMessage());
                logger.info("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted()
            {
                logger.debug("Sequence complete.");
                logger.info("Sequence complete.");
            }
        });
        logger.info("reactiveX() :: end");
    }
}
