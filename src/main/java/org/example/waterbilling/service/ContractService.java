package org.example.waterbilling.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.CardDto;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.model.entity.Canal;
import org.example.waterbilling.model.entity.Contract;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.model.enumr.Statuses;
import org.example.waterbilling.repository.CanalRepository;
import org.example.waterbilling.repository.ContractRepository;
import org.example.waterbilling.repository.UserRepository;
import org.example.waterbilling.service.script.AnnotationScript;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final CanalRepository canalRepository;
    private final MediaFileService mediaFileService;

    public ResponseEntity<?> getTableColumns(){
        List<Map<String, String>> result = AnnotationScript.getFieldsFromClass(Contract.class);
        result.add(Map.of("accessor","fullName","title","ФИО"));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getTableData(ReportDynamicDto dto){
        return ResponseEntity.ok(contractRepository.findAll().stream()
                .map(contract -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", contract.getId());
                    map.put("fullName", getFullName(contract.getUserId()));
                    map.put("canalId", getCanalName(contract.getCanalId()));
                    map.put("createdAt", contract.getCreatedAt());
                    map.put("fixedAt", contract.getFixedAt());
                    map.put("payStatus", contract.getPayStatus());
                    map.put("waterStatus", contract.getWaterStatus());
                    map.put("tariff", contract.getTariff());
                    map.put("price", contract.getPrice());
                    return map;
                }));
    }

    private String getFullName(UUID id){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            return "";
        }
        return user.getFirstname()+" "+user.getLastname()+" "+user.getFathersname();
    }
    private String getCanalName(UUID id){
        Canal canal = canalRepository.findById(id).orElse(null);
        if(canal==null){
            return "";
        }
        return canal.getName();
    }

    public ResponseEntity<?> action(UUID id, String action){
        Contract contract = contractRepository.findById(id).orElse(null);
        if(action.equals("start")){
            contract.setWaterStatus("Успешно");
        }else {
            contract.setWaterStatus("Отказано");
        }
        return ResponseEntity.ok(contract);
    }

    public ResponseEntity<?> getByClientId(UUID clientId){
        List<Map> contracts = contractRepository.getContractsByUserId(clientId).stream().map(contract -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", contract.getId());
            map.put("fullName", getFullName(contract.getUserId()));
            map.put("canalId", getCanalName(contract.getCanalId()));
            map.put("createdAt", contract.getCreatedAt());
            map.put("fixedAt", contract.getFixedAt());
            map.put("payStatus", contract.getPayStatus());
            map.put("waterStatus", contract.getWaterStatus());
            map.put("tariff", contract.getTariff());
            map.put("price", contract.getPrice());
            map.put("value", contract.getValue());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(contracts);
    }

    public ResponseEntity<?> changeTariff(UUID id,Map<String,Object> tariff){
        Contract contract = contractRepository.findById(id).orElse(null);
        contract.setTariff(String.valueOf(tariff.get("tariff")));
        contractRepository.save(contract);
        return ResponseEntity.ok(contract);
    }

    public ResponseEntity<?> createContract(UUID id,Map<String,String> value){
        Contract contract = new Contract();
        contract.setValue(String.valueOf(value.get("value")));
        contract.setFixedAt(LocalDateTime.parse((value.get("fixedAt"))));
        contract.setCanalId(id);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        contract.setUserId(userRepository.findByEmail(login).getId());
        return ResponseEntity.ok(contractRepository.save(contract));
    }

    public ResponseEntity<?> pay(UUID id, CardDto dto){
        Contract contract = contractRepository.findById(id).orElse(null);
        contract.setPayStatus("Оплачено");
        return ResponseEntity.ok(contract);
    }

    public ResponseEntity<?> generateReceipt(UUID contractId) throws IOException {
        Contract contract = contractRepository.findById(contractId).orElseThrow(()->new RuntimeException("Contract not found"));
        User user = userRepository.findById(contract.getUserId()).orElse(null);
        Canal canal = canalRepository.findById(contract.getCanalId()).orElse(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdfDoc);
        document.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));


        document.add(new Paragraph("Фискальный чек"));
        document.add(new Paragraph("ID: " + contractId));
        document.add(new Paragraph("ФИО: " + user.getFirstname() + " " + user.getLastname()));
        document.add(new Paragraph("Канал: " + canal.getName()));
        document.add(new Paragraph("Дата: " + contract.getCreatedAt()));
        document.add(new Paragraph("Тариф: " + contract.getTariff()));
        document.add(new Paragraph("Объем: " + contract.getValue()));
        document.add(new Paragraph("Статус оплаты: " + contract.getPayStatus()));
        document.add(new Paragraph("Статус подачи воды: " + contract.getWaterStatus()));
        document.add(new Paragraph("Итого: " + (Float.parseFloat(contract.getPrice())*Float.parseFloat(contract.getTariff())) + " KZT"));

        document.close();
        return mediaFileService.uploadFile(new MockMultipartFile("receipt.pdf", "receipt.pdf", "application/pdf", outputStream.toByteArray()));
    }

    public ResponseEntity<?> getById(UUID id) throws IOException {
        Contract contract = contractRepository.findById(id).orElse(null);
        Map<String, Object> map = new HashMap<>();
        map.put("id", contract.getId());
        map.put("fullName", getFullName(contract.getUserId()));
        map.put("canalId", getCanalName(contract.getCanalId()));
        map.put("createdAt", contract.getCreatedAt());
        map.put("fixedAt", contract.getFixedAt());
        map.put("payStatus", contract.getPayStatus());
        map.put("waterStatus", contract.getWaterStatus());
        map.put("tariff", contract.getTariff());
        map.put("price", contract.getPrice());
        map.put("value", contract.getValue());
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> statuses(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Statuses.getAll());
    }
}
