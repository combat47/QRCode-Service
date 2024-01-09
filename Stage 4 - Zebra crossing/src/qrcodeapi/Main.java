package qrcodeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@RestController
class ApiController {

    @GetMapping("/api/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("200 OK");
    }

    @GetMapping(path = "/api/qrcode")
    public ResponseEntity<?> getQRCode(
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "type", required = true) String type,
            @RequestParam(name = "contents", required = true) String contents
    ) {
        if (contents.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Contents cannot be null or blank\"}");
        }

        if (size < 150 || size > 350) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}");
        }

        if (!type.equalsIgnoreCase("png") && !type.equalsIgnoreCase("jpeg") && !type.equalsIgnoreCase("gif")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Only png, jpeg and gif image types are supported\"}");
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type, outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            outputStream.close();

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("image/" + type))
                    .body(imageBytes);
        } catch (WriterException | IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to generate QR code\"}");
        }
    }
}
