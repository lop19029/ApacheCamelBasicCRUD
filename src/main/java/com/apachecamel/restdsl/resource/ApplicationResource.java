package com.apachecamel.restdsl.resource;

import com.apachecamel.restdsl.dto.Order;
import com.apachecamel.restdsl.processor.OrderProcessor;
import com.apachecamel.restdsl.services.OrderService;
import org.apache.camel.BeanInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ApplicationResource extends RouteBuilder {

    private final OrderService service;

    @BeanInject
    private OrderProcessor processor;

    public ApplicationResource(OrderService service) {
        this.service = service;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .port(9090)
                .host("localhost")
                .bindingMode(RestBindingMode.json);

        rest()
            //Get orders list
            .get("/getOrders")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .route()
                    .routeId("get-orders-list")
                    .log("Getting orders list")
                .bean(OrderService.class, "getOrders")
                .endRest()

            //Find an order by id
            .get("/getOrder/{id}")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .param()
                    .name("id")
                    .required(true)
                    .type(RestParamType.path) //Where is this variable coming from?
                .endParam()
                .route()
                    .routeId("get-order-by-id")
                    .log("Getting order from database")
                .to("bean:orderService?method=getOrderById(${header.id})")
                .endRest()

            //Create a new order
            .post("/addOrder")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(Order.class)
                .outType(Order.class)
                .route()
                    .routeId("add-new-order")
                    .log("Adding new order")
                .process(processor)
                .endRest()

            //Edit an order by id
            .put("/{id}")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(Order.class)
                .outType(Order.class)
                .param()
                    .name("id")
                    .required(true)
                    .type(RestParamType.path)
                .endParam()
                .route()
                    .routeId("edit-order-by-id")
                    .log("Editing order")
                .process(ex ->
                        service.updateOrder(
                                ex.getIn().getHeader("id", Integer.class), ex.getIn().getBody(Order.class)))
                .endRest()

            //Delete order
            .delete("/{id}")
                .param()
                    .name("id")
                    .required(true)
                    .type(RestParamType.path)
                .endParam()
                .route()
                    .routeId("delete-order-by-id")
                    .log("Deleting order")
                .bean(OrderService.class, "deleteOrder(${header.id})")
                .endRest();
    }
}
