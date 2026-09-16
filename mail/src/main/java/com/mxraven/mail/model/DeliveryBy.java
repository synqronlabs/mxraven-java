package com.mxraven.mail.model;

/**
 * The DELIVERBY parameter of RFC 2852: a delivery time limit, a notification
 * mode, and whether a trace is requested.
 *
 * @param seconds the time limit in seconds
 * @param mode    the requested notification mode
 * @param trace   whether a trace of the delivery path is requested
 */
public record DeliveryBy(long seconds, DeliveryByMode mode, boolean trace) {
}
