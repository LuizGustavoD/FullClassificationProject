package com.ia.project.service;



import org.springframework.stereotype.Service;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ia.project.model.response.PredictResponseModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NeuralModelService {
    
    private final RestTemplate restTemplate;

    public String predictImageClass(MultipartFile image) throws IOException {

        String flaskUrl = "http://localhost:5000/predict";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<PredictResponseModel> response = restTemplate.postForEntity(
                flaskUrl,
                requestEntity,
                PredictResponseModel.class
        );

        String prediction = response.getBody().getPredicted_class();

        switch (prediction){
            case "0":
                return "Avião";
            case "1":
                return "Automóvel";
            case "2":
                return "Pássaro";
            case "3":
                return "Gato";
            case "4":
                return "Cervo";
            case "5":
                return "Cachorro";
            case "6":
                return "Sapo";
            case "7":
                return "Cavalo";
            case "8":
                return "Navio";
            case "9":
                return "Caminhão";
            default:
                return "Previsão desconhecida: " + prediction;
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
