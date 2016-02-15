package com.dw.pract.utils;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dw.pract.service.StudentService;

/**
 * This class is used to taken component from spring context.
 * 
 * @author ashvin
 */
@Named
public class BeanUtils implements ApplicationContextAware
{

    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    private static BeanUtils bOnly = null;

    private ApplicationContext applicationContext;

    @Inject
    private BeanMapper beanMapper;

    @Inject
    private StudentService studentService;

    private transient AutowireCapableBeanFactory beanFactory;

    public BeanUtils()
    {
        logger.debug("BeanUtils() :: Initilized.");
        bOnly = this;
    }

    /**
     * Get own object from spring context.
     * 
     * @return
     */
    public static BeanUtils only()
    {
        return bOnly;
    }

    /**
     * This method is used to get instance of {@link BeanMapper}
     * 
     * @return {@link BeanMapper} -instance of {@link BeanMapper}
     */
    public BeanMapper getBeanMapper()
    {
        return beanMapper;
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.applicationContext = context;
        beanFactory = context.getAutowireCapableBeanFactory();
        logger.debug("setApplicationContext() :: beanFactory injected");
    }

    /**
     * This method is used to get spring application context
     * 
     * @return {@link ApplicationContext} - spring application context
     */
    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    /**
     * Injects required dependencies to the object based on @Inject and @Autowired annotations.
     * 
     * @param bean
     */
    public void autowireBean(Object bean)
    {
        beanFactory.autowireBean(bean);
    }

    public StudentService getStudentService()
    {
        return studentService;
    }
}
