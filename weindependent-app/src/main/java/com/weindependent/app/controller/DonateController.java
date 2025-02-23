package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donate")
public class DonateController {

    @SignatureAuth
    @PostMapping("/demo")
    @CrossOrigin(origins = "*")
    public String demo() {
        return "This is just for demo";
    }
}
