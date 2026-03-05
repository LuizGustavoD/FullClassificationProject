package com.ia.project.routes;



import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ia.project.service.NeuralModelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class NeuralRequestController {
    
    
    private final NeuralModelService neuralModelService;

    @PostMapping
    public ResponseEntity<String> receiveData(@RequestParam("image") MultipartFile image) throws IOException {
        String prediction = neuralModelService.predictImageClass(image);
        if (prediction.startsWith("Error")) {
            return ResponseEntity.status(500).body(prediction);
        }
        return ResponseEntity.ok(prediction);
        
    }

}
