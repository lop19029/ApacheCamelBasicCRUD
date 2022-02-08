package com.apachecamel.restdsl.processor;

import com.apachecamel.restdsl.dto.Order;
import com.apachecamel.restdsl.services.OrderService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements Processor {

    private final OrderService service;

    public OrderProcessor(OrderService service) {
        this.service = service;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Order body = exchange.getIn().getBody(Order.class);
        service.addOrder(body);
    }
}
