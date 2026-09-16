package com.example.hes_interface_demo;

import com.example.hes_interface_demo.demos.dto.*;
import com.example.hes_interface_demo.demos.util.MD5;
import com.example.hes_interface_demo.demos.web.BasicController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReceiverContractTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private BasicController controller;
    private MockMvc mvc;
    private static final String SINGLE = "konsungyitijijsondata";

    @BeforeEach void setup() {
        controller = new BasicController(mapper);
        ReflectionTestUtils.setField(controller, "configuredRsId", "konsung");
        mvc = MockMvcBuilders.standaloneSetup(controller)
            .addPlaceholderValue("ksy.receiver-path", "/parsingHesData").build();
    }
    private Map<String,Object> message(String key) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("dataId", "DEMO-001"); m.put("orgCode", "org-not-tenant");
        m.put("deviceCode", "DEMO-DEVICE");m.put("key", key);
        m.put("time", "2026-09-16 10:00:00");
        m.put("checkDate", "2026-09-16 10:00:00");
        m.put("version", "1.0.0"); m.put("doctorCode", "DEMO-DOCTOR");
        Map<String,String> person = new LinkedHashMap<>();
        person.put("name", "SYNTHETIC"); person.put("sexCode", "0");
        m.put("personInfo", person);
        m.put("checkData", Collections.singletonMap("bloodPressure", Collections.singletonMap("sbp", "120")));
        return m;
    }
    private void expect(Map<String,Object> m,String code) throws Exception {
        mvc.perform(post("/parsingHesData").contentType("application/json").content(mapper.writeValueAsBytes(m)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value(code));
    }
    @Test void knownVectorAndConfiguredTenant() throws Exception {
        assertEquals("87a81d8edfe6b43aa8763d7147f6245a",MD5.getMD5("konsung"+SINGLE));
        expect(message(MD5.getMD5("konsung"+SINGLE)),"10000");
    }
    @Test void orgCodeIsNotSignatureTenant() throws Exception {
        expect(message(MD5.getMD5("org-not-tenant"+SINGLE)),"00000");
    }
    @Test void unconfiguredReceiverNeverFallsBackToOrg() throws Exception {
        ReflectionTestUtils.setField(controller,"configuredRsId","");
        expect(message(MD5.getMD5("org-not-tenant"+SINGLE)),"00000");
    }
    @Test void oldSampleKeysAndBadKeysAreRejected() throws Exception {
        for(String key: Arrays.asList("8ed7d02b349d10c6ca0c194e273ef919","f79087e1aac6a0da1b4a3665c15ced1d","bad", ""))
            expect(message(key),"00000");
    }
    @Test void timeKeyRequiresExplicitCompatibilityOption() throws Exception {
        Map<String,Object> m=message(MD5.getMD5("2026-09-16 10:00:00"+SINGLE));
        expect(m,"00000");
        ReflectionTestUtils.setField(controller,"acceptLegacyTimeKey",true);
        expect(m,"10000");
        m.put("time","2026-09-16 10:00:01");expect(m,"00000");
        m.remove("time");expect(m,"00000");
    }
    @Test void missingEnvelopeFieldsAreRejected() throws Exception {
        for(String field:Arrays.asList("dataId","deviceCode","orgCode","doctorCode","checkDate","version","time","personInfo","checkData")) {
            Map<String,Object> m=message(MD5.getMD5("konsung"+SINGLE));m.remove(field);expect(m,"00000");
        }
    }
    @Test void removedAndUnknownCategoriesAreRejected() throws Exception {
        for(String category:Arrays.asList("xcg","threeWay","biochemicals","Hemameba","futureCategory")) {
            Map<String,Object> m=message(MD5.getMD5("konsung"+SINGLE));
            m.put("checkData",Collections.singletonMap(category,Collections.singletonMap("value","1")));
            expect(m,"00000");
        }
    }
    @Test void emptyMeasurementsAreNotAcknowledgedAsResults() throws Exception {
        for(Object empty:Arrays.asList(Collections.emptyMap(),Collections.singletonMap("bloodPressure",null),
                Collections.singletonMap("bloodPressure",Collections.emptyMap()),
                Collections.singletonMap("bloodPressure",Collections.singletonMap("sbp"," ")))) {
            Map<String,Object> m=message(MD5.getMD5("konsung"+SINGLE));m.put("checkData",empty);expect(m,"00000");
        }
    }
    @Test void hemamebaTotalIsCaseSensitiveAndPersonFieldsAreRequired() throws Exception {
        Map<String,Object> m=message(MD5.getMD5("konsung"+SINGLE));
        m.put("checkData",Collections.singletonMap("hemameba",Collections.singletonMap("Hemameba","6.8")));
        expect(m,"00000");
        for(String field:Arrays.asList("name","sexCode")) {
            m=message(MD5.getMD5("konsung"+SINGLE));
            ((Map<?,?>)m.get("personInfo")).remove(field);expect(m,"00000");
        }
    }
    @Test void conflictingEcgAliasesAreRejectedInEitherOrder() throws Exception {
        for(boolean reverse:Arrays.asList(false,true)) {
            Map<String,String> heart=new LinkedHashMap<>();
            heart.put(reverse?"pr":"PR","150");heart.put(reverse?"PR":"pr","160");
            Map<String,Object> m=message(MD5.getMD5("konsung"+SINGLE));
            m.put("checkData",Collections.singletonMap("heart",heart));expect(m,"00000");
            heart.put("PR","150");heart.put("pr","150");expect(m,"10000");
        }
    }
    @Test void configuredPathReceivesResults() throws Exception {
        MockMvc custom=MockMvcBuilders.standaloneSetup(controller)
            .addPlaceholderValue("ksy.receiver-path","/lis/ksy/results").build();
        custom.perform(post("/lis/ksy/results").contentType("application/json")
                .content(mapper.writeValueAsBytes(message(MD5.getMD5("konsung"+SINGLE)))))
            .andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value("10000"));
        custom.perform(post("/parsingHesData").contentType("application/json").content("{}"))
            .andExpect(status().isNotFound());
    }
    @Test void noUnsignedFragmentEndpoint() throws Exception {
        mvc.perform(post("/parsingHesData/breathing").contentType("application/json").content("{}"))
            .andExpect(status().isNotFound());
    }
    @Test void differentialFieldsAreRetainedByReceiver() throws Exception {
        String json="{\"hemameba\":\"6.85\",\"wbcLym\":\"2.10\",\"wbcMon\":\"0.45\",\"wbcNeu\":\"4.12\",\"wbcEos\":\"0.14\",\"wbcBas\":\"0.04\",\"wbcLymPercent\":\"30.7\",\"wbcMonPercent\":\"6.6\",\"wbcNeuPercent\":\"60.1\",\"wbcEosPercent\":\"2.0\",\"wbcBasPercent\":\"0.6\"}";
        assertEquals(mapper.readTree(json),mapper.valueToTree(mapper.readValue(json,AktHemamebaDto.class)));
    }
    @Test void ecgCaseVariantsRetainAllNineValues() throws Exception {
        List<String> fields=Arrays.asList("PR","QRS","QT","QTC","P","QRSZ","T","RV5","SV1");
        for(boolean lower:Arrays.asList(false,true)) {
            Map<String,String> input=new LinkedHashMap<>();
            for(int i=0;i<fields.size();i++)input.put(lower?fields.get(i).toLowerCase(Locale.ROOT):fields.get(i),String.valueOf(i+1));
            AktWaveForm wave=mapper.readValue(mapper.writeValueAsBytes(input),AktWaveForm.class);
            com.fasterxml.jackson.databind.JsonNode output=mapper.valueToTree(wave);
            for(int i=0;i<fields.size();i++)assertEquals(String.valueOf(i+1),output.path(fields.get(i)).asText());
        }
    }
    @Test void publishedRunnableExamplesPassStrictReceiver() throws Exception {
        for(String file:Arrays.asList("standard-checkdata.json","compatibility-checkdata.json","ecg-waveform-example.json")) {
            byte[] data=Files.readAllBytes(Paths.get("demo/sample-data",file));
            mvc.perform(post("/parsingHesData").contentType("application/json").content(data))
                .andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value("10000"));
        }
    }
    @Test void historicalAttachmentsPassOnlyAfterExplicitResigning() throws Exception {
        for(String file:Arrays.asList("normal-checkdata.json","custom-device.json","report-picture.json")) {
            com.fasterxml.jackson.databind.node.ObjectNode body=(com.fasterxml.jackson.databind.node.ObjectNode)
                mapper.readTree(Files.readAllBytes(Paths.get("demo/sample-data",file)));
            body.put("key",MD5.getMD5("konsung"+SINGLE));
            mvc.perform(post("/parsingHesData").contentType("application/json").content(mapper.writeValueAsBytes(body)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value("10000"));
        }
    }
    @Test void fullFixtureRetainsEverySuppliedMeasurement() throws Exception {
        com.fasterxml.jackson.databind.JsonNode input=mapper.readTree(
            Files.readAllBytes(Paths.get("demo/sample-data/compatibility-checkdata.json")));
        assertEquals(16,input.path("checkData").size());
        com.fasterxml.jackson.databind.JsonNode output=mapper.valueToTree(mapper.treeToValue(input,AktMeasureDto.class));
        assertContainsValues(input,output,"");
    }
    private void assertContainsValues(com.fasterxml.jackson.databind.JsonNode input,
            com.fasterxml.jackson.databind.JsonNode output,String path) {
        if(input.isObject()) {
            input.fields().forEachRemaining(e -> assertContainsValues(e.getValue(),output.path(e.getKey()),path+"/"+e.getKey()));
        } else if(input.isArray()) {
            assertEquals(input.size(),output.size(),path);
            for(int i=0;i<input.size();i++)assertContainsValues(input.get(i),output.path(i),path+"/"+i);
        } else assertEquals(input,output,path);
    }
    @Test void malformedJsonAndNonObjectBodiesAreRejected() throws Exception {
        mvc.perform(post("/parsingHesData").contentType("application/json").content("{broken"))
            .andExpect(status().isBadRequest());
        for(String body:Arrays.asList("[]","null","42")) {
            mvc.perform(post("/parsingHesData").contentType("application/json").content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value("00000"));
        }
    }
}
