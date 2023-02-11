package com.driver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class OrderService {


    OrderRepository orderRepository=new OrderRepository();

    //1
    public void addOrder(Order order){

         orderRepository.addOrder(order);
    }

    //2
    public void addPartner(String partnerId){
        orderRepository.addPartner(partnerId);
    }


    //3
    public void addOrderPartnerPair(String orderId,String partnerId){

        //This is basically assigning that order to that partnerId
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    //4
    public Order getOrderById(String orderId){

        //order should be returned with an orderId.
        return orderRepository.getOrderById(orderId);
    }

    //5
    public DeliveryPartner getPartnerById( String partnerId){

       return orderRepository.getPartnerById(partnerId);
    }

    //6
    public Integer getOrderCountByPartnerId(String partnerId){

        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    //7
    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrdersByPartnerId(partnerId);

        //orders should contain a list of orders by PartnerId
    }

    //8
    public List<String> getAllOrders(){
        List<String> orders = orderRepository.getAllOrders();
        //return all orders
        return orders;
    }

    //9
    public Integer getCountOfUnassignedOrders(){
        Integer countOfOrders = orderRepository.getCountOfUnassignedOrders();

        //Count of orders that have not been assigned to any DeliveryPartner
        return countOfOrders;
    }

    //10
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){

        Integer countOfOrders = orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);

        //countOfOrders that are left after a particular time of a DeliveryPartner

        return countOfOrders;
    }

    //11
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time = orderRepository.getLastDeliveryTimeByPartnerId(partnerId);

        //Return the time when that partnerId will deliver his last delivery order.

        return time;
    }

    //12
    public void deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        orderRepository.deletePartnerById(partnerId);
    }

    //13
    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        orderRepository.deleteOrderById(orderId);
    }
}
