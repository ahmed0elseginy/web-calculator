package com.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {

    @GetMapping({"/", "/calculator"})
    public String show(Model model) {
        model.addAttribute("display", "0");
        return "calculator";
    }

    @PostMapping("/calculator")
    public String press(@RequestParam String key, @RequestParam(defaultValue = "0") String display, Model model) {
        String next = display;

        if ("C".equals(key)) {
            next = "0";
        } else if ("=".equals(key)) {
            next = eval(display);
        } else {
            next = "0".equals(display) && !"+-*/.".contains(key) ? key : display + key;
        }

        model.addAttribute("display", next);
        return "calculator";
    }

    private String eval(String s) {
        try {
            int i = -1;
            char op = 0;
            for (int j = 1; j < s.length(); j++) {
                char c = s.charAt(j);
                if ("+-*/".indexOf(c) >= 0) { i = j; op = c; break; }
            }
            if (i < 0) return s;
            double a = Double.parseDouble(s.substring(0, i).trim());
            double b = Double.parseDouble(s.substring(i + 1).trim());
            double r = switch (op) {
                case '+' -> a + b;
                case '-' -> a - b;
                case '*' -> a * b;
                case '/' -> b == 0 ? Double.NaN : a / b;
                default -> Double.NaN;
            };
            return Double.isNaN(r) ? "Error" : (r == (long) r ? "" + (long) r : "" + r);
        } catch (Exception e) {
            return "Error";
        }
    }
}
