package com.example.simpletrustly;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.simpletrustly.Deposits.DepositInput;

@Controller
public class WebController {
    @GetMapping("/")
    public String GetRoot() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String GetHome() {
        return "home";
    }

    @GetMapping("/deposit")
    public String GetDeposit(Model model) {
        model.addAttribute("depositInput", new DepositInput());
        return "deposit";
    }

    @PostMapping("/deposit")
    public String PostDeposit(DepositInput input) {
        String url = RequestUtil.DoDeposit(input.getAmount());

        if(url != null) {
            return "redirect:" + url;
        }

        return "redirect:/txfail";
    }

    @GetMapping("/txfail")
    public String GetTxFail() {
        return "txfail";
    }

    @GetMapping("/txsuccess")
    public String GetTxSuccess() {
        return "txsuccess";
    }
}
