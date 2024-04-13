package projet.ais.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    // @Autowired
    // private StripeService stripeService;

    // @PostMapping("/charge")
    // public ResponseEntity < String > chargeCreditCard(@RequestParam("token") String token, @RequestParam("amount") double amount) {
    //     try {
    //         Charge charge = stripeService.chargeCreditCard(token, amount);
    //         return ResponseEntity.ok("Payment successful! Charge ID: " + charge.getId());
    //     } catch (StripeException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed: " + e.getMessage());
    //     }
    // }

    // @PutMapping("add/{paymentIntentId}")
    // public ResponseEntity < PaymentResponse > updateDatabase(@PathVariable("paymentIntentId") String paymentIntentId) {
    //     PaymentResponse response = service.savePaymentInformation(paymentIntentId);
    //     return ResponseEntity.ok(PaymentResponse.builder().withStatus(HttpStatus.OK.value())
    //         .withMessage("Payment Confirm successfully")
    //         .withTransactionNumber(response.getPayment().getPaymentId()).build());
    // }

}
