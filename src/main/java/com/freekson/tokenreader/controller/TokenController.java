package com.freekson.tokenreader.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freekson.tokenreader.entity.User;
import com.freekson.tokenreader.model.LoginResponse;
import com.freekson.tokenreader.model.LoginUserDto;
import com.freekson.tokenreader.model.RegisterUserDto;
import com.freekson.tokenreader.service.AuthenticationService;
import com.freekson.tokenreader.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class TokenController {

    private static final String BASE_URL = "https://api.coinlore.net/api/tickers/";
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final RestTemplate restTemplate;
    private boolean isFetched = false;
    private String fetchedData;

    @PostMapping("/auth/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/tokens")
    public ResponseEntity<String> getTokenList(@RequestParam(name = "start", required = false, defaultValue = "0") String start,
                                               @RequestParam(name = "limit", required = false, defaultValue = "100") String limit) {

        var url = BASE_URL + "?start=" + start + "&limit=" + limit;

        var response = restTemplate.getForObject(url, String.class);
        if (response != null) {
            isFetched = true;
            fetchedData = response;
        }

        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(response);
    }

    @PostMapping("/save")
    public ResponseEntity<Resource> saveTokenData() throws JsonProcessingException {
        if (isFetched && fetchedData != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(fetchedData);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.json");

            ByteArrayResource resource = new ByteArrayResource(jsonNode.toString().getBytes());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
