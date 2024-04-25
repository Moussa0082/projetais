package projet.ais.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Service
public class StripeService {

  // @Value("${STRIPE_SECRET_KEY}")
  private String secretKey;

  public Charge chargeCreditCard(String token, double amount) throws StripeException {
    Stripe.apiKey = secretKey;

    Map<String, Object> params = new HashMap<>();
    params.put("amount", (int)(amount * 100));
    params.put("currency", "USD");
    params.put("source", token);

    Charge charge = Charge.create(params);
    return charge;
  }
}