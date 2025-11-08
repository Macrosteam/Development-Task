package com.omar.SpringTask.report;

import com.omar.SpringTask.entity.Purchase;
import com.omar.SpringTask.entity.Refund;
import com.omar.SpringTask.repository.PurchaseRepository;

import com.omar.SpringTask.repository.RefundRepository;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.util.List;

import org.springframework.mail.javamail.MimeMessageHelper;

@Component
public class ReportSender {


    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ReportConfig config;

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ReportCache reportCache;
    @Autowired
    private RefundRepository refundRepository;


    @Scheduled(cron = "#{T(java.lang.String).format('0 %d %d * * ?', @reportConfig.minute, @reportConfig.hour)}")
    public void sendReport()throws MessagingException {

        List<Long> rangePurchase = reportCache.getPurchaseRange();

        List<Purchase> purchases = purchaseRepository.findByIdBetween(rangePurchase.get(0), rangePurchase.get(1)-1);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        StringBuilder html = new StringBuilder("<p>Please find the daily report for ").append(yesterday.toString()).append("</p>");

        if(purchases.isEmpty()){
            html.append("<h3>No Purchases</h3>");
        }
        else
        {
            html.append("<h3>Purchase List</h3>");
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr><th>Purchase ID</th><th>Customer ID</th><th>Product ID</th><th>Amount</th></tr>");
            for (Purchase purchase : purchases) {
                html.append("<tr>")
                        .append("<td>").append(purchase.getId()).append("</td>")
                        .append("<td>").append(purchase.getCustomerId()).append("</td>")
                        .append("<td>").append(purchase.getProductId()).append("</td>")
                        .append("<td>").append(purchase.getAmount()).append("</td>")
                        .append("</tr>");
            }
            html.append("</table>");
        }

        List<Long> rangeRefund = reportCache.getRefundRange();

        List<Refund> refunds = refundRepository.findByIdBetween(rangeRefund.get(0), rangeRefund.get(1)-1);

        if(refunds.isEmpty()){
            html.append("<h3>No Refunds</h3>");
        }
        else
        {
            html.append("<h3>Refund List</h3>");
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr><th>Refund ID</th><th>Purchase ID</th><th>Customer ID</th><th>Product ID</th><th>Amount</th></tr>");
            for (Refund refund : refunds) {
                html.append("<tr>")
                        .append("<td>").append(refund.getId()).append("</td>")
                        .append("<td>").append(refund.getPurchaseId()).append("</td>")
                        .append("<td>").append(refund.getCustomerId()).append("</td>")
                        .append("<td>").append(refund.getProductId()).append("</td>")
                        .append("<td>").append(refund.getAmount()).append("</td>")
                        .append("</tr>");
            }

            html.append("</table>");
        }


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(config.getRecipient());
        helper.setSubject(config.getSubject());
        helper.setText(html.toString(), true);

        mailSender.send(message);

    }
}
