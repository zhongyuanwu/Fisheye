package com.iyuile.caelum.enums;

/**
 * @Description 支付类型
 */
public enum PayType {

    WeChat(1), // 微信
    Alipay(2), // 支付宝
    CreditCard(3); // 信用卡

    private int value;

    public int getValue() {
        return value;
    }

    /**
     * @param value
     */
    private PayType(int value) {
        this.value = value;
    }

    public static PayType getActionType(int v) {
        PayType[] values = values();
        if (values != null) {
            for (PayType each : values) {
                if (each.getValue() == v) {
                    return each;
                }
            }
        }
        return null;
    }

}
