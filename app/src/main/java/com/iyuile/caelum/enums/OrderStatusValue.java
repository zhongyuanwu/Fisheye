package com.iyuile.caelum.enums;

/**
 * @Description 订单状态
 */
public enum OrderStatusValue {

    ALL(0), //全部
    UNPAID(1), //待付款
    PAID(2), //待发货
    SEND(3), //已发货
    FINISH(4); //已完成

    private int value;

    public int getValue() {
        return value;
    }

    /**
     * @param value
     */
    private OrderStatusValue(int value) {
        this.value = value;
    }

    public static OrderStatusValue getActionType(int v) {
        OrderStatusValue[] values = values();
        if (values != null) {
            for (OrderStatusValue each : values) {
                if (each.getValue() == v) {
                    return each;
                }
            }
        }
        return null;
    }

}
