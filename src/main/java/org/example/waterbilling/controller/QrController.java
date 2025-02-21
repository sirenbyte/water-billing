package org.example.waterbilling.controller;

import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.example.waterbilling.service.QrService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 36000,allowCredentials = "false")
public class QrController {
    private final QrService qrService;

    @GetMapping
    public ResponseEntity<?> qrForPay(@RequestParam UUID contractId) throws IOException, WriterException {
        return qrService.generateQRCodeAsMultipart(contractId);
    }

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam UUID contractId) {
        return qrService.pay(contractId);
    }
}
