package com.dw.pract.dao.impl;

import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dw.pract.dao.AddressDao;
import com.dw.pract.model.Address;
import com.dw.pract.service.impl.AbstractDaoImpl;

@Named
@Transactional(propagation = Propagation.REQUIRED)
public class AddressDaoImpl extends AbstractDaoImpl<Address, Long> implements AddressDao
{
}
