package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.config.constant.HostAndPort;
import org.example.waterbilling.model.dto.LoginRequest;
import org.example.waterbilling.model.entity.MediaFile;
import org.example.waterbilling.repository.MediaFileRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
public class MediaFileService {
    private final RestTemplate restTemplate;
    private final MediaFileRepository mediaFileRepository;
    private static String URL = "http://89.218.1.74:1337";

    public ResponseEntity<?> uploadFile(MultipartFile file) {
        String fileUrl = getFileUrl(file);
        MediaFile mediaFile = new MediaFile();
        mediaFile.setUrl(URL+fileUrl);
        mediaFile = mediaFileRepository.save(mediaFile);
        Map<String,String> result = new HashMap<>();
        result.put("url", mediaFile.getUrl());
        result.put("id",mediaFile.getId().toString());
        return ResponseEntity.ok(result);
    }

    private String getFileUrl(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + getJwt());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", file.getResource());
        body.add("alternativeText","");
        body.add("caption","");
        body.add("name",null);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String response = restTemplate.exchange(HostAndPort.HOST_AND_PORT_CMS_SERVICE + "/upload", HttpMethod.POST, requestEntity, String.class).getBody();
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getString("url");
    }

    private String getJwt() {
        LoginRequest request = new LoginRequest();
        request.setEmail("abzal.tugan@gmail.com");
        request.setPassword("Qazaq214!");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);
        String jwtToken = restTemplate.exchange(HostAndPort.HOST_AND_PORT_CMS_SERVICE + "/admin/login",
                HttpMethod.POST, entity, String.class).getBody();
        JSONObject jsonObject = new JSONObject(jwtToken);
        return jsonObject.getJSONObject("data").getString("token");
    }

    public ResponseEntity<?> findById(UUID id) {
        MediaFile mediaFile = mediaFileRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        Map<String,String> result = new HashMap<>();
        result.put("url", mediaFile.getUrl());
        result.put("id",mediaFile.getId().toString());
        return ResponseEntity.ok(result);
    }
}
