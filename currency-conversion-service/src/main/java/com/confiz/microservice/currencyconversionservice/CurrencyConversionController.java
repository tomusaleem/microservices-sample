package com.confiz.microservice.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

  @Autowired
  private CurrencyExchangeProxy proxy;

  @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
  public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
      @PathVariable String to, @PathVariable BigDecimal quantity) {

    HashMap<String, String> uriVariables = new HashMap<>(Map.of("from", from, "to", to));
    ResponseEntity<CurrencyConversion> forEntity = new RestTemplate()
        .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
            uriVariables);
    CurrencyConversion currencyConversion = forEntity.getBody();
    return new CurrencyConversion(currencyConversion.getId(), currencyConversion.getFrom(),
        currencyConversion.getTo(), quantity, currencyConversion.getConversionMultiple(),
        quantity.multiply(currencyConversion.getConversionMultiple()),
        currencyConversion.getEnvironment() + " rest template");
  }

  @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
  public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,
      @PathVariable String to, @PathVariable BigDecimal quantity) {

    CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
    return new CurrencyConversion(currencyConversion.getId(), currencyConversion.getFrom(),
        currencyConversion.getTo(), quantity, currencyConversion.getConversionMultiple(),
        quantity.multiply(currencyConversion.getConversionMultiple()),
        currencyConversion.getEnvironment() + " feign");
  }
}
