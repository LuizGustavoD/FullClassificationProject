package com.ia.project.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.project.service.NeuralModelService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/model")
@RestController
@RequiredArgsConstructor
public class NeuralModelDetails {

    private final NeuralModelService neuralModelService;
    
    @GetMapping("/accuracy")
    public ResponseEntity<String> getAccuracy() {
        // Lógica para retornar a acurácia do modelo
        return ResponseEntity.ok("Acurácia do modelo: 97%");
    }

    @GetMapping("/summary")
    public ResponseEntity<String> getSummary() {
        String summary = neuralModelService.getModelSummary();
        if (summary.startsWith("Error" )) {
            return ResponseEntity.status(500).body(summary);
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/lossFunc")
    public ResponseEntity<String> getLossFunction() {
        String lossFunction = neuralModelService.getModelLossFunction();
        if (lossFunction.startsWith("Error")) {
            return ResponseEntity.status(500).body(lossFunction);
        }
        return ResponseEntity.ok(lossFunction);
    }

    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        String metrics = neuralModelService.getModelMetrics();
        if (metrics.startsWith("Error")) {
            return ResponseEntity.status(500).body(metrics);
        }
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/optimizer")
    public ResponseEntity<String> getOptimizer() {
        String optimizer = neuralModelService.getModelOptimizer();
        if (optimizer.startsWith("Error")) {
            return ResponseEntity.status(500).body(optimizer);
        }
        return ResponseEntity.ok(optimizer);
    }

    @GetMapping("/layers")
    public ResponseEntity<String> getLayers() {
        String layers = neuralModelService.getModelLayers();
        if (layers.startsWith("Error")) {
            return ResponseEntity.status(500).body(layers);
        }
        return ResponseEntity.ok(layers);
    }

    


}
