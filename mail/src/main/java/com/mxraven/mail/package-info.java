/**
 * The core mxRaven mail package for submitting messages over SMTP.
 *
 * <p>{@link com.mxraven.mail.SmtpClient} is the main entry point. Configure a
 * connection with {@link com.mxraven.mail.SmtpConfig}, send a message with
 * {@link com.mxraven.mail.SmtpClient#send(com.mxraven.mail.model.Mail)}, and
 * inspect the outcome through {@link com.mxraven.mail.SendResult}.
 *
 * <p>Message building and modeling live in {@code com.mxraven.mail.model},
 * MIME parsing and generation in {@code com.mxraven.mail.mime}, recipient
 * feedback in {@code com.mxraven.mail.feedback}, and webhook handling in
 * {@code com.mxraven.mail.webhook}.
 */
package com.mxraven.mail;
