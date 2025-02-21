package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.entity.Energy;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.repository.CanalRepository;
import org.example.waterbilling.repository.EnergyRepository;
import org.example.waterbilling.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticService {
    private final CanalRepository canalRepository;
    private final UserRepository userRepository;
    private final EnergyRepository energyRepository;

    public ResponseEntity<?> generateMainStatistic(){
        Map result = new HashMap();
        List<Map> canal = canalRepository.findAll()
                .stream()
                .map(it-> Map.of("id",it.getId(),"name",it.getName())).collect(Collectors.toList());

        Map canals = new HashMap();
        canals.put("canalList",canal);
        canals.put("canalTotal",canal.size());
        result.put("canals",canals);

        Map<String,Long> user = userRepository.findAll().stream().filter(it->it.getPosition()!=null).collect(Collectors.groupingBy(User::getPosition,Collectors.counting()));
        result.put("userList",user);

        return ResponseEntity.ok().body(result);
    }

    public ResponseEntity<?> energyForCanal(UUID id){
        return ResponseEntity.ok(energyRepository.findAllByCanal_IdAndFixedAtBetween(id, LocalDate.now().minusMonths(1).atStartOfDay(),LocalDate.now().atStartOfDay())
               .stream().collect(Collectors.toMap(Energy::getFixedAt, Energy::getTotal)));
    }
}
