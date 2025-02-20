package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/statistic")
@AllArgsConstructor
@CrossOrigin
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping
    public ResponseEntity<?> getMainStatistic(){
        return statisticService.generateMainStatistic();
    }

    @GetMapping("{canalId}")
    public ResponseEntity<?> getStatisticForCanal(@PathVariable UUID canalId){
        return statisticService.energyForCanal(canalId);
    }
}
