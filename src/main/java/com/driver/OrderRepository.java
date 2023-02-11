package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String, List<String>> partnerIdAndOrderMap=new HashMap<>();
    Map<String,String> assignedMap=new HashMap<>();


    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }
    public void addPartner(String partnerId){
        //creating a new partner object
        DeliveryPartner deliveryPartner=new DeliveryPartner();
        deliveryPartnerMap.put(partnerId,deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId){
        List<String> pairString=partnerIdAndOrderMap.getOrDefault(partnerId,new ArrayList<>());
        pairString.add(orderId);
        partnerIdAndOrderMap.put(partnerId,pairString);
        assignedMap.put(orderId,partnerId);
        //Now updating the number of orders in the entity
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(pairString.size());
    }

    public Order getOrderById(String orderId){

        //order should be returned with an orderId.
        return orderMap.getOrDefault(orderId,null);
    }

    public DeliveryPartner getPartnerById(String partnerId){

        return deliveryPartnerMap.getOrDefault(partnerId,null);
    }

    public Integer getOrderCountByPartnerId(String partnerId){

        return deliveryPartnerMap.get(partnerId).getNumberOfOrders();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> orders =partnerIdAndOrderMap.getOrDefault(partnerId,null);

        //orders should contain a list of orders by PartnerId

        return orders;
    }
    public List<String> getAllOrders(){
        //We only have to get the order Id not the whole object
        List<String> orders = new ArrayList<>();
        for (String s:orderMap.keySet()){
            orders.add(s);
        }
        //Get all orders
        return orders;
    }

    public int  getCountOfUnassignedOrders(){
        int countOfOrders = orderMap.size()-assignedMap.size();

        //Count of orders that have not been assigned to any DeliveryPartner

        return countOfOrders;
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){

        int convertedTime=Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3));
        Integer countOfOrders = 0;
        List<String> copyOfOrderList=partnerIdAndOrderMap.get(partnerId);
        for (String order:copyOfOrderList){
            Order order1=orderMap.get(order);
            if (order1.getDeliveryTime()>convertedTime){
                countOfOrders++;
            }
        }
        //countOfOrders that are left after a particular time of a DeliveryPartner
        return countOfOrders;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time ="";

        List<String> copyOfOrderList=partnerIdAndOrderMap.get(partnerId);
        //We Don't know whether the latest order is the last or not
        //In order to find out we need to iterate over List
        int deliveryTime=0;
        for (String s:copyOfOrderList){
            Order order=orderMap.get(s);
            deliveryTime=Math.max(order.getDeliveryTime(),deliveryTime);
        }
        //For Hour
        int hour=deliveryTime/60;
        String sHour="";
        if(hour < 10){
            sHour="0"+String.valueOf(hour);
        } else {
            sHour=String.valueOf(hour);
        }
        //For Minute
        int min=deliveryTime%60;
        String sMin="";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }
        time=sHour+":"+sMin;
        //Return the time when that partnerId will deliver his last delivery order.
        return time;
    }

    public void deletePartnerById(String partnerId){

        deliveryPartnerMap.remove(partnerId);
        List<String> list=partnerIdAndOrderMap.getOrDefault(partnerId,new ArrayList<>());
        ListIterator<String> itr=list.listIterator();
        while(itr.hasNext()){
            String s=itr.next();
            assignedMap.remove(s);
        }
        //Now removing pair db
        partnerIdAndOrderMap.remove(partnerId);
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
    }
    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        orderMap.remove(orderId);
        String partnerId=assignedMap.get(orderId);
        assignedMap.remove(orderId);
        List<String> list=partnerIdAndOrderMap.get(partnerId);
        for(String s:list){
            if(s.equals(orderId)){
                list.remove(s);
            }
        }
        //updating after deleting
        partnerIdAndOrderMap.put(partnerId,list);
    }
}
