package com.sz.exchange;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @Date 2024-12-30 21:37
 * @Version 1.0
 * 交易委托账本
 */
public class OrderBook {

    private List<Order> buyOrders;

    private List<Order> sellOrders;

    public OrderBook() {
        this.buyOrders = new ArrayList<>();
        this.sellOrders = new ArrayList<>();
    }

    // [100,94,93,91] 98
    public void addBuyOrder(Order order) {
        int length = buyOrders.size();
        int index = length - 1;
        //如上图所示，从91开始计算
        while (index >= 0 && buyOrders.get(index).getPrice() < order.getPrice()) {
            index--;
        }
        //插在此时index的后面
        index++;
        buyOrders.add(index, order);
    }
}
