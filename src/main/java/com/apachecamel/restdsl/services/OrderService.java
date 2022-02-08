package com.apachecamel.restdsl.services;

import com.apachecamel.restdsl.dto.Order;
import com.apachecamel.restdsl.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private List<Order> list = new ArrayList<>();

    @PostConstruct
    public void initDB() {
        list.add(new Order(67, "Mobile", 2500));
        list.add(new Order(96, "Videogame", 300));
        list.add(new Order(102, "Smartwatch", 2000));
    }

    public Order addOrder(Order order) {
        list.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return list;
    }

    public Order getOrderById(int id){
        for (Order order:
             list) {
            if(order.getId()==id)
                return order;
        }
        throw new OrderNotFoundException();
    }

    public Order updateOrder(int id, Order body) {
        Order order = this.getOrderById(id);
        body.setId(id);
        list.remove(order);
        list.add(body);
        return body;
    }

    public void deleteOrder(int id){
        Order order = this.getOrderById(id);
        list.remove(order);
    }
}
