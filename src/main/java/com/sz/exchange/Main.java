package com.sz.exchange;

import java.util.List;

/**
 * @Author
 * @Date 2024-12-30 21:28
 * @Version 1.0
 *///TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        List<Trade> tradeList = orderBook.process(new Order(100L, 256L, "1001", OrderConstant.SELL));
        log(tradeList);
        List<Trade> tradeList2 = orderBook.process(new Order(100L, 256L, "1002", OrderConstant.SELL));
        log(tradeList2);
        List<Trade> tradeList3 = orderBook.process(new Order(100L, 255L, "1003", OrderConstant.BUY));
        log(tradeList3);
        List<Trade> tradeList4 = orderBook.process(new Order(150L, 257L, "1003", OrderConstant.BUY));
        log(tradeList4);

    }

    private static void log(List<Trade> tradeList) {
        if(tradeList.isEmpty()){
            System.out.println("暂无订单成交");
        }else {
            for (Trade trade : tradeList) {
                System.out.println(trade);
            }
        }
    }
}