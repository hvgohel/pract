package com.dw.pract.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseAPIController {

  @RequestMapping(value = "test", method = RequestMethod.GET)
  public String get() {
    return "Mitul";
  }
}
