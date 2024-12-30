package com.sz.exchange;

import lombok.Data;

/**
 * @Author
 * @Date 2024-12-30 21:34
 * @Version 1.0
 * 交易
 * taker:
 *      选择立即执行市场上已有的订单的交易者。
 *      他们消耗市场中的现有订单以满足即时需求。Taker不提供流动性，而是利用市场上已存的订单进行交易‌
 * maker:
 *      提交限价单并将其放入订单簿，等待其他市场参与者来匹配成交的交易者。Maker提供了市场的流动性，
 *      因为他们的订单不会立即执行，而是等待其他用户的订单与之匹配。通过挂单，Maker为交易所创造了深度（流动性）‌
 */
@Data
public class Trade {

    private String takerOrderId;

    private String makerOrderId;

    private Long amount;

    private Long price;
}
