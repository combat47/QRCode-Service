package qrcodeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>("200 OK", HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<String> notImplemented() {
        return new ResponseEntity<>("501 NOT IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED);
    }
}
