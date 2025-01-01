package com.sz.exchange;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @Date 2024-12-30 21:37
 * @Version 1.0
 * 交易委托账本:价格优先、时间优先原则
 */
@Data
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

    //[91,93,95,98,100] 92
    public void addSellOrder(Order order) {
        int length = sellOrders.size();
        int index = length - 1;
        while (index >= 0 && sellOrders.get(index).getPrice() > order.getPrice()) {
            index--;
        }
        index++;
        sellOrders.add(index, order);
    }

    // 删除指定位置的买单
    public void removeBuyOrder(int index) {
        if (index < 0 || index >= buyOrders.size()) {
            throw new IndexOutOfBoundsException("Invalid buy order index");
        }
        buyOrders.remove(index);
    }

    // 删除指定位置的卖单
    public void removeSellOrder(int index) {
        if (index < 0 || index >= sellOrders.size()) {
            throw new IndexOutOfBoundsException("Invalid sell order index");
        }
        sellOrders.remove(index);
    }

    public List<Trade> process(Order order){
        return order.getSide() == 1 ? processLimitBuy(order) : processLimitSell(order);
    }

    private List<Trade> processLimitSell(Order order) {
        List<Trade> trades = new ArrayList<>();
        int buyNumbers = buyOrders.size();
        //买单是按照从大到小排列的；如果第一个最大的都比卖的金额小，那么肯定不会成交
        if(buyNumbers > 0 && buyOrders.get(0).getPrice() > order.getPrice()) {
            for (int i = 0; i < buyOrders.size(); i++) {
                Order buyOrder = buyOrders.get(i);
                if(buyOrder.getPrice() < order.getPrice()){
                    break;
                }

                //完全成交
                if(buyOrder.getAmount() >= order.getAmount()){
                    trades.add(new Trade(order.getId(), buyOrder.getId(), order.getAmount(), buyOrder.getPrice()));
                    buyOrder.setAmount(buyOrder.getAmount() - order.getAmount());
                    if(buyOrder.getAmount() == 0){
                        removeBuyOrder(i);
                    }
                    return trades;
                }

                //部分成交
                if(buyOrder.getAmount() < order.getAmount()){
                    trades.add(new Trade(order.getId(), buyOrder.getId(), buyOrder.getAmount(), buyOrder.getPrice()));
                    order.setAmount(order.getAmount() - buyOrder.getAmount());
                    removeBuyOrder(i);
                    i--;
                }
            }
        }
        //不会成交
        addSellOrder(order);
        return trades;
    }

    /**
     * 卖方：
     *  105 56
     *  104 100
     *  103 200
     *  102 300
     *  101 250
     *  100 210
     *
     *  买方：
     *   99 100
     *   98 140
     *   97 200-----此时就没法成交
     * @param order
     * @return
     */
    private List<Trade> processLimitBuy(Order order) {
        List<Trade> trades = new ArrayList<>();
        int sellNumbers = sellOrders.size();
        //撮合交易：能够撮合需要满足sellNumbers > 0 并且卖单的价格小于当前买单的价格
        if(sellNumbers > 0 && sellOrders.get(0).getPrice() <= order.getPrice()){
            for (int i = 0; i < sellOrders.size(); i++) {
                Order sellOrder = sellOrders.get(i);
                if(sellOrder.getPrice() > order.getPrice()){
                    break;
                }
                //如果当前位置的卖单数量大于等于买单订单的数量，则全部成交
                if(sellOrder.getAmount() >= order.getAmount()){
                    trades.add(new Trade(order.getId(), sellOrder.getId(), order.getAmount(), sellOrder.getPrice()));
                    sellOrder.setAmount(sellOrder.getAmount() - order.getAmount());
                    if(sellOrder.getAmount() == 0){
                        removeSellOrder(i);
                    }
                    return trades;
                }

                //如果当前位置的卖单数量小于买单订单的数量，则部分成交
                if(sellOrder.getAmount() < order.getAmount()){
                    trades.add(new Trade(order.getId(), sellOrder.getId(), sellOrder.getAmount(), sellOrder.getPrice()));
                    //肯定卖单已经没了
                    order.setAmount(order.getAmount() - sellOrder.getAmount());
                    removeSellOrder(i);
                    i--;
                }
            }
        }
        addBuyOrder(order);
        //否则，是成交不了的，加入到买单中即可
        return trades;
    }
}
