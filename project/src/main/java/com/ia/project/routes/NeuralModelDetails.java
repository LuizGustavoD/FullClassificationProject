package com.ia.project.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/model")
@RestController
public class NeuralModelDetails {

    
    
    @GetMapping("/accuracy")
    public ResponseEntity<String> getAccuracy() {
        // Lógica para retornar a acurácia do modelo
        return ResponseEntity.ok("Acurácia do modelo: 95%");
    }

    @GetMapping("/summary")
    public ResponseEntity<String> getSummary() {
        // Lógica para retornar um resumo do modelo
        return ResponseEntity.ok("Resumo do modelo: ...");
    }

    @GetMapping("/lossFunc")
    public ResponseEntity<String> getLossFunction() {
        // Lógica para retornar a função de perda do modelo
        return ResponseEntity.ok("Função de perda do modelo: SparseCategoricalCrossentropy");
    }


}
