/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hes_interface_demo.demos.web;

import com.example.hes_interface_demo.demos.dto.AktMeasureDto;
import com.example.hes_interface_demo.demos.dto.ThirdResultCodeDto;
import com.example.hes_interface_demo.demos.util.MD5;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/** Receiver example only. No patient record or database is written by this demo. */
@Slf4j
@RestController
public class BasicController {
    private static final String SINGLE = "konsungyitijijsondata";
    private final ObjectMapper mapper;

    public BasicController(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // Assigned by Konsung and configured out of band; never inferred from orgCode.
    @Value("${ksy.rsid:}")
    private String configuredRsId;

    // Enable only when the deployed platform's scheduled resend rule is confirmed.
    @Value("${ksy.accept-legacy-time-key:false}")
    private boolean acceptLegacyTimeKey;

    @PostMapping("${ksy.receiver-path:/parsingHesData}")
    public ThirdResultCodeDto parsingHesData(@RequestBody JsonNode body) {
        if (!StringUtils.hasText(configuredRsId)) {
            return failure("Receiver configuration missing: ksy.rsid");
        }
        if (body == null || !body.isObject() || !body.path("key").isTextual()
                || !StringUtils.hasText(body.path("key").asText())) {
            return failure("Missing key");
        }
        String key = body.path("key").asText();
        boolean verified = MD5.getMD5(configuredRsId + SINGLE).equals(key);
        if (!verified && acceptLegacyTimeKey && body.path("time").isTextual()
                && StringUtils.hasText(body.path("time").asText())) {
            verified = MD5.getMD5(body.path("time").asText() + SINGLE).equals(key);
        }
        if (!verified) {
            return failure("Invalid key");
        }
        for (String field : new String[]{"dataId", "orgCode", "deviceCode", "doctorCode",
                "checkDate", "version", "time"}) {
            if (!body.path(field).isTextual() || !StringUtils.hasText(body.path(field).asText())) {
                return failure("Missing or non-string field: " + field);
            }
        }
        JsonNode person = body.path("personInfo");
        if (!person.isObject() || !person.path("name").isTextual()
                || !StringUtils.hasText(person.path("name").asText())
                || !person.path("sexCode").isTextual()
                || !StringUtils.hasText(person.path("sexCode").asText())) {
            return failure("Missing personInfo.name or personInfo.sexCode");
        }
        JsonNode checkData = body.path("checkData");
        if (!checkData.isObject() || !hasValue(checkData)) {
            return failure("Missing checkData measurements");
        }
        JsonNode heart = checkData.path("heart");
        for (String field : new String[]{"PR", "QRS", "QT", "QTC", "P", "QRSZ", "T", "RV5", "SV1"}) {
            String alias = field.toLowerCase(Locale.ROOT);
            if (heart.has(field) && heart.has(alias) && !heart.get(field).equals(heart.get(alias))) {
                return failure("Conflicting ECG field aliases: " + field);
            }
        }
        if (checkData.path("hemameba").has("Hemameba")) {
            return failure("Use lower-case hemameba for the WBC total");
        }
        try {
            AktMeasureDto dto = mapper.treeToValue(body, AktMeasureDto.class);
            // DTO conversion verifies object shapes before the LIS processing point below.
            if (dto.getCheckData() == null) {
                return failure("Missing checkData");
            }
        } catch (JsonProcessingException | IllegalArgumentException ex) {
            return failure("Invalid message structure or unsupported checkData category");
        }
        // Replace this point with durable storage and idempotent processing in your LIS.
        // Production receivers must acknowledge only after successful processing.
        log.info("Demo request validated; no database write performed");
        return new ThirdResultCodeDto("10000", "Demo validation only; no database write");
    }

    private ThirdResultCodeDto failure(String message) {
        return new ThirdResultCodeDto("00000", message);
    }

    private boolean hasValue(JsonNode node) {
        if (node.isContainerNode()) {
            Iterator<JsonNode> values = node.elements();
            while (values.hasNext()) {
                if (hasValue(values.next())) return true;
            }
            return false;
        }
        return !node.isNull() && StringUtils.hasText(node.asText());
    }
}
