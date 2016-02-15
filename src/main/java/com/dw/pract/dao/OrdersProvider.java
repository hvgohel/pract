package com.dw.pract.dao;

import java.util.List;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.dw.pract.model.Entity;

public interface OrdersProvider<T extends Entity> {

    List<Order> getOrders(Root<T> root);
}
