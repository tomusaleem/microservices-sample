package com.confiz.microservices.limitsservice.controller;

import com.confiz.microservices.limitsservice.bean.Limit;
import com.confiz.microservices.limitsservice.configurations.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

  @Autowired
  private Configuration configuration;

  @GetMapping("/limits")
  public Limit retrieveLimit() {
    return new Limit(configuration.getMinimum(), configuration.getMaximum());
//    return new Limit(1, 1000);
  }
}
