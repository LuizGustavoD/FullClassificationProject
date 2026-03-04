package com.ia.project.routes;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class NeuralRequestController {
    

    @PostMapping
    public String receiveData() {
        // Lógica para processar os dados recebidos e iniciar o treinamento da rede neural
        return "Dados recebidos e treinamento iniciado!";
    }

}
