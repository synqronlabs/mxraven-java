package com.mxraven.mail.model;

public record DeliveryBy(long seconds, DeliveryByMode mode, boolean trace) {
}
