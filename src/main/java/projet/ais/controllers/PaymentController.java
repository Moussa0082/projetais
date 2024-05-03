package projet.ais.controllers;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import projet.ais.services.StripeService;

@RestController
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    StripeService stripeService;

    @PostMapping("/charge")
    public ResponseEntity<String>chargeCreditCard(@RequestParam("token") String token, @RequestParam("amount") double amount) {
        try {
            Charge charge = stripeService.chargeCreditCard(token, amount);
            return ResponseEntity.ok("Payment successful! Charge ID: " + charge.getId());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Payment failed: " + e.getMessage());
        }
    }

    // @PutMapping("add/{paymentIntentId}")
    // public ResponseEntity <PaymentResponse> updateDatabase(@PathVariable("paymentIntentId") String paymentIntentId) {
    //     PaymentResponse response = service.savePaymentInformation(paymentIntentId);
    //     return ResponseEntity.ok(PaymentResponse.builder().withStatus(HttpStatus.OK.value())
    //         .withMessage("Payment Confirm successfully")
    //         .withTransactionNumber(response.getPayment().getPaymentId()).build());
    // }

}
