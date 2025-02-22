package org.example.waterbilling.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import org.example.waterbilling.model.entity.Contract;
import org.example.waterbilling.repository.ContractRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QrService {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private final AuthService authService;
    private final ContractRepository contractRepository;
    private final MediaFileService mediaFileService;

    public ResponseEntity<?> generateQRCodeAsMultipart(UUID contractId) throws WriterException, IOException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(
                "http://89.218.1.74:9081/api/qr/pay?contractId=" + contract.getId(),
                BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "PNG", outputStream);

        // Конвертируем в MultipartFile
        return mediaFileService.uploadFile(new MockMultipartFile(
                "file",                        // Имя параметра
                "qrcode.png",                   // Оригинальное имя файла
                "image/png",                     // MIME-тип
                new ByteArrayInputStream(outputStream.toByteArray()) // Контент
        ));
    }

    public void saveQRCodeToFile(String text, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public ResponseEntity<?> pay(UUID contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(()->new RuntimeException("Contract not found"));
        contract.setPayStatus("SUCCESS");
        return ResponseEntity.ok(contractRepository.save(contract));
    }

}
