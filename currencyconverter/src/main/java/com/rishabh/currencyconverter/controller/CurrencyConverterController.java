package com.rishabh.currencyconverter.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CurrencyConverterController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterController.class);

    private final String API_KEY = "0fe022453168479fbe9e35c94c5a64ba";
    private final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=" + API_KEY;

    @GetMapping("/api/convert")
    public String convertCurrency(@RequestParam double amount,
                                  @RequestParam String fromCurrency,
                                  @RequestParam String toCurrency,
                                  Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL, String.class);

        // Log the response to check its structure
        logger.info("API Response: {}", response);

        try {
            JSONObject json = new JSONObject(response);


            if (json.has("error") && json.getBoolean("error")) {
                model.addAttribute("result", "Error fetching conversion rates.");
                return "index";
            }

            double conversionRate = json.getJSONObject("rates").getDouble(toCurrency) /
                    json.getJSONObject("rates").getDouble(fromCurrency);

            double convertedAmount = amount * conversionRate;
            model.addAttribute("result", String.format("%.2f %s", convertedAmount, toCurrency));
        } catch (Exception e) {
            model.addAttribute("result", "Error parsing conversion rates.");
            logger.error("Error processing API response", e);
        }
        try {
            JSONObject json = new JSONObject(response);

        } catch (JSONException e) {
            model.addAttribute("result", "Error parsing conversion rates.");
            logger.error("Error parsing JSON response", e);
        } catch (Exception e) {
            model.addAttribute("result", "Unexpected error occurred.");
            logger.error("Unexpected error", e);
        }


        return "index";
    }
}
