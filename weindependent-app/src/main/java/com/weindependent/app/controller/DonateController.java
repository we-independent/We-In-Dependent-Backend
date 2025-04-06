package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "捐款管理")
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
