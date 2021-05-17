package com.api.cdms.query.infrastructure.controller;

import com.api.cdms.query.application.dto.StatsDTO;
import com.api.cdms.query.application.manager.StatsQueryManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/stats")
@Api(tags = {"Controlador para la consulta de las estadísticas de las verificaciones de ADN"})
public class StatsQueryController {

    private final StatsQueryManager statsQueryManager;

    @Autowired
    public StatsQueryController(StatsQueryManager statsQueryManager) {
        this.statsQueryManager = statsQueryManager;
    }

    @GetMapping
    @ApiOperation("Realiza la consulta de las estadísticas de los ADN analizados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Se realizó la consulta de las estadísticas correctamente"),
            @ApiResponse(code = 500, message = "Error interno en el servidor al procesar la petición")
    })
    public ResponseEntity<StatsDTO> getStats() {
        return new ResponseEntity<>(statsQueryManager.execute(), HttpStatus.OK);
    }
}
