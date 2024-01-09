package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

@RestController
public class qrController {

    @GetMapping("/api/health")
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("200", HttpStatus.OK);
    }

    @GetMapping("api/qrcode")
    public ResponseEntity<?> getImage(@RequestParam() String contents,
                                      @RequestParam(required = false) Integer size,
                                      @RequestParam(required = false) String type,
                                      @RequestParam(required = false) String correction) throws WriterException {

        if (contents == null || contents.isEmpty() || contents.matches("\\s*")) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    "Contents cannot be null or blank");
        } else if (size != null && (size < 150 || size > 350)) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    "Image size must be between 150 and 350 pixels");
        } else if (correction != null && (correction.matches("[^LMQH]"))) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    "Permitted error correction levels are L, M, Q, H");
        } else if (type != null && !type.matches(".*png|.*jpeg|.*gif")) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    "Only png, jpeg and gif image types are supported");
        } else if (contents != null && size != null && type == null && correction == null) {
            BufferedImage bufferedImage = createImage(contents, size);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(bufferedImage);
        } else if (contents != null && correction != null && size == null && type == null) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            ErrorCorrectionLevel correctionLevel = switch (correction) {
                case "L" -> ErrorCorrectionLevel.L;
                case "M" -> ErrorCorrectionLevel.M;
                case "Q" -> ErrorCorrectionLevel.Q;
                case "H" -> ErrorCorrectionLevel.H;
                default -> null;
            };
            assert correctionLevel != null;
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 250, 250, hints);
            BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(picture);
        } else if (contents != null && correction == null && size == null && type != null) {
            BufferedImage bufferedImage = createImage(contents);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/" + type.toUpperCase()))
                    .body(bufferedImage);
        } else if (contents != null && correction != null && size != null && type == null) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            ErrorCorrectionLevel correctionLevel = switch (correction) {
                case "L" -> ErrorCorrectionLevel.L;
                case "M" -> ErrorCorrectionLevel.M;
                case "Q" -> ErrorCorrectionLevel.Q;
                case "H" -> ErrorCorrectionLevel.H;
                default -> null;
            };
            assert correctionLevel != null;
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(picture);
        } else if (contents != null && correction == null && size != null && type != null) {
            BufferedImage bufferedImage = createImage(contents, size);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/" + type.toUpperCase()))
                    .body(bufferedImage);
        } else if (contents != null && correction != null && size == null && type != null) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            ErrorCorrectionLevel correctionLevel = switch (correction) {
                case "L" -> ErrorCorrectionLevel.L;
                case "M" -> ErrorCorrectionLevel.M;
                case "Q" -> ErrorCorrectionLevel.Q;
                case "H" -> ErrorCorrectionLevel.H;
                default -> null;
            };
            assert correctionLevel != null;
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 250, 250, hints);
            BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/" + type.toUpperCase()))
                    .body(picture);
        } else if (contents != null && correction != null && size != null && type != null) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            ErrorCorrectionLevel correctionLevel = switch (correction) {
                case "L" -> ErrorCorrectionLevel.L;
                case "M" -> ErrorCorrectionLevel.M;
                case "Q" -> ErrorCorrectionLevel.Q;
                case "H" -> ErrorCorrectionLevel.H;
                default -> null;
            };
            assert correctionLevel != null;
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/" + type.toUpperCase()))
                    .body(picture);
        }
        BufferedImage bufferedImage = createImage(contents);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/png"))
                .body(bufferedImage);

    }

    private static BufferedImage createImage() {
        int width = 250;
        int height = 250;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.dispose();

        return image;
    }

    private static BufferedImage createImage(int size2) {

        BufferedImage image = new BufferedImage(size2, size2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, size2, size2);
        g.dispose();

        return image;
    }

    private static BufferedImage createImage(String contents) throws WriterException {
        int width = 250;
        int height = 250;
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = image.createGraphics();
//        g.setColor(Color.white);
//        g.fillRect(0, 0, width, height);
//        g.dispose();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);

        return picture;
    }

    private static BufferedImage createImage(String contents, int size) throws WriterException {
        int width = size;
        int height = size;
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = image.createGraphics();
//        g.setColor(Color.white);
//        g.fillRect(0, 0, width, height);
//        g.dispose();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage picture = MatrixToImageWriter.toBufferedImage(bitMatrix);

        return picture;
    }
}