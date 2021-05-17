package com.api.cdms.command.infrastructure.controller;

import com.api.cdms.command.application.dto.MutantDTO;
import com.api.cdms.command.application.manager.MutantSaveManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/mutant")
@Api(tags = {"Controlador para el manejo de las transacciones de un ADN"})
public class MutantCommandController {

    private final MutantSaveManager mutantSaveManager;

    @Autowired
    public MutantCommandController(MutantSaveManager mutantSaveManager) {
        this.mutantSaveManager = mutantSaveManager;
    }

    @PostMapping
    @ApiOperation("Verifica si un humano es mutante por medio de un ADN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Se realizó la verificación y el guardado del ADN correctamente cuando es un mutante"),
            @ApiResponse(code = 400, message = "No se está enviando la información correcta al momento de realizar la petición"),
            @ApiResponse(code = 403, message = "Se realizó la verificación y el guardado del ADN correctamente cuando es un humano"),
            @ApiResponse(code = 500, message = "Error interno en el servidor al procesar la petición")
    })
    public ResponseEntity<Boolean> save(@RequestBody MutantDTO mutantDTO) {
        return new ResponseEntity<>(mutantSaveManager.execute(mutantDTO.getDna()), HttpStatus.OK);
    }
}
