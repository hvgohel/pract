package com.dw.pract.dao;

import com.dw.pract.model.Address;

public interface AddressDao extends AbstractDao<Address, Long> {
  Address get(Long id);
}
