package com.hr.hrapplication.controller;

import com.hr.hrapplication.dao.XmlImportResultDto;
import com.hr.hrapplication.service.XmlImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Tag(name = "Import", description = "XML import endpoints")
@RestController
@RequestMapping("/api/import")
public class XmlImportController {

    private final XmlImportService importer;

    public XmlImportController(XmlImportService importer) {
        this.importer = importer;
    }

    @PostMapping(value = "/employees/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importXml(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No file uploaded (form-data field must be 'file')."));
        }

        try (InputStream inputStream = file.getInputStream()) {
            XmlImportResultDto xmlImportResult = importer.importEmployees(inputStream);
            return ResponseEntity.ok(xmlImportResult);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "Invalid XML");
            String msg = ex.getMessage();
            if (msg != null && !msg.isBlank()) body.put("details", msg);
            return ResponseEntity.badRequest().body(body);
        } catch (Exception ex) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "Failed to import XML");
            String msg = ex.getMessage();
            if (msg != null && !msg.isBlank()) body.put("details", msg);
            body.put("exception", ex.getClass().getSimpleName());
            return ResponseEntity.internalServerError().body(body);
        }
    }
}
