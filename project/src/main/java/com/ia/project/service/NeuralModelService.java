package com.ia.project.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NeuralModelService {
    
    private final RestTemplate restTemplate;

    public String predictImageClass(MultipartFile image) {
        try {
            String url = "http://localhost:5000/predict";
            return restTemplate.postForObject(url, image, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during prediction: " + e.getMessage();
        }
    }

    public String getModelLossFunction() {
        try {
            String url = "http://localhost:5000/lossFunc";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching loss function: " + e.getMessage();
        }
    }

    public String getModelMetrics() {
        try {
            String url = "http://localhost:5000/metrics";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching model metrics: " + e.getMessage();
        }
    }

    public String getModelOptimizer() {
        try {
            String url = "http://localhost:5000/optimizer";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching model optimizer: " + e.getMessage();
        }
    }

        public String getModelLayers() {
            try {
                String url = "http://localhost:5000/layers";
                return restTemplate.getForObject(url, String.class);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error fetching model layers: " + e.getMessage();
            }
        }

        public String getModelSummary() {
                try {
                    String url = "http://localhost:5000/summary";
                    return restTemplate.getForObject(url, String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error fetching model summary: " + e.getMessage();
                }
        }
     
}
