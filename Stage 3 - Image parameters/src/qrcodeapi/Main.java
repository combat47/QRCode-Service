package qrcodeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
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
            @RequestParam(name = "size", required = true) Integer size,
            @RequestParam(name = "type", required = true) String type
    ) {
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
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, size, size);
            graphics.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Update the content type based on the requested image type
            String contentType = "";
            if (type.equalsIgnoreCase("png")) {
                ImageIO.write(image, "png", outputStream);
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (type.equalsIgnoreCase("jpeg")) {
                ImageIO.write(image, "jpeg", outputStream);
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (type.equalsIgnoreCase("gif")) {
                ImageIO.write(image, "gif", outputStream);
                contentType = MediaType.IMAGE_GIF_VALUE;
            }

            byte[] imageBytes = outputStream.toByteArray();
            outputStream.close();

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
