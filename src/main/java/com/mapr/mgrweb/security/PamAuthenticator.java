package com.mapr.mgrweb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PamAuthenticator {

    private final String URI_VOLUME_INFO = "/volume/info";
    private final String URI_VOLUME_CREATE = "/volume/create";
    private final String URI_VOLUME_REMOVE = "/volume/remove";
    private static final Logger log = LoggerFactory.getLogger(PamAuthenticator.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${api.host.baseurl}")
    private String apiHost;

    @Value("${server.port}")
    private String serverPort;

    @Value("${local.host.baseurl}")
    private String localhost;

    @RequestMapping(value = "/api/c8vol", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> c8vol(@RequestBody Map<String, Object> payload) throws Exception {
        log.debug(String.valueOf(payload));
        String username = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        String volumePath = (String) payload.get("path");
        //http[s]://<host>:<port>/rest/volume/info?<parameters>
        //curl -k -X GET 'https://abc.sj.us:8443/rest/volume/info?name=test_vol' --user mapr:mapr
        //curl -k -X POST 'https://<hostname>:8443/rest/volume/create?name=<volName>&path=<mountPath>' --user mapr:mapr
        try {
            //Map<String, String> params = new HashMap<>();
            //params.put("name", volume);
            //params.put("path", path);
            // create request
            // HttpEntity request = new HttpEntity(params, headers);
            HttpHeaders headers = createAuthHeader(username, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiHost + URI_VOLUME_CREATE)
                .queryParam("name", volume)
                .queryParam("path", volumePath);

            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, Map.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("error encountered: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/api/deletevol", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> deletevol(@RequestBody Map<String, Object> payload) throws Exception {
        log.debug(String.valueOf(payload));
        String username = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        try {
            HttpHeaders headers = createAuthHeader(username, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + URI_VOLUME_REMOVE).queryParam("name", volume);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, Map.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("error encountered: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/api/volinfo", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> volinfo(@RequestBody Map<String, Object> payload) throws Exception {
        log.debug(String.valueOf(payload));
        String username = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        try {
            HttpHeaders headers = createAuthHeader(username, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + URI_VOLUME_INFO).queryParam("name", volume);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("error encountered: " + e.getMessage());
        }
    }

    @GetMapping("/api/pamauthenticate")
    public ResponseEntity<String> pamauthenticate(@RequestParam Map<String, String> payload) {
        System.out.println(payload);
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        try {
            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(localhost + ":" + serverPort + "/api/paminfo")
                .queryParam("userid", userid)
                .queryParam("password", password);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("error encountered: " + e.getMessage());
        }
    }

    private HttpHeaders createAuthHeader(String username, String password) {
        // create auth credentials
        String authStr = username + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }
}
