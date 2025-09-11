package com.hr.hrapplication.service;

import com.hr.hrapplication.dao.EmployeeEntity;
import com.hr.hrapplication.dao.EmployeeRepository;
import com.hr.hrapplication.dao.XmlImportResultDto;
import com.hr.hrapplication.domain.EmployeeXml;
import com.hr.hrapplication.domain.EmployeesXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.UnmarshalException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class XmlImportService {

    private final EmployeeRepository repo;

    public XmlImportService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public XmlImportResultDto importEmployees(InputStream raw) throws Exception {
        try (BOMInputStream bomInputStream = new BOMInputStream(
                raw, true,
                ByteOrderMark.UTF_8,
                ByteOrderMark.UTF_16BE,
                ByteOrderMark.UTF_16LE,
                ByteOrderMark.UTF_32BE,
                ByteOrderMark.UTF_32LE)) {

            Charset cs = StandardCharsets.UTF_8;
            if (bomInputStream.hasBOM()) {
                cs = Charset.forName(bomInputStream.getBOMCharsetName());
            }

            String xml = new String(bomInputStream.readAllBytes(), cs)
                    .replaceFirst("^\\uFEFF", "")
                    .replaceFirst("^\\s+", "");

            if (!xml.startsWith("<")) {
                throw new IllegalArgumentException("Uploaded file does not look like XML (no '<' at start).");
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(EmployeesXml.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            try (StringReader stringReader = new StringReader(xml)) {
                EmployeesXml employeesXml = (EmployeesXml) unmarshaller.unmarshal(stringReader);
                if (employeesXml == null || employeesXml.getEmployees() == null) {
                    throw new IllegalArgumentException("XML parsed but contains no <employee> records.");
                }

                int inserted = 0, updated = 0;
                List<String> skipped = new ArrayList<>();
                List<EmployeeEntity> employees = new ArrayList<>();

                for (EmployeeXml employeeXml : employeesXml.getEmployees()) {
                    if (employeeXml.getId() == null || employeeXml.getId().isBlank()) {
                        skipped.add("(missing-id)");
                        continue;
                    }
                    EmployeeEntity employeeEntity = repo.findById(employeeXml.getId()).orElse(new EmployeeEntity());
                    boolean exists = employeeEntity.getId() != null;

                    employeeEntity.setId(employeeXml.getId());
                    employeeEntity.setFirstname(employeeXml.getFirstname());
                    employeeEntity.setLastname(employeeXml.getLastname());
                    employeeEntity.setDivision(employeeXml.getDivision());
                    employeeEntity.setBuilding(employeeXml.getBuilding());
                    employeeEntity.setTitle(employeeXml.getTitle());
                    employeeEntity.setRoom(employeeXml.getRoom());
                    employeeEntity.setCreatedat(LocalDateTime.now());
                    employeeEntity.setUpdatedat(null);
                    log.info("Importing employee {}: {}", employeeEntity.getId(), employeeEntity);
                    repo.save(employeeEntity);
                    if (exists) {
                        updated++;
                    } else {
                        inserted++;
                        employees.add(employeeEntity);
                    }
                }
                log.info("Imported {} employees, updated {}, skipped {}, inserted : {} ", inserted, updated, skipped.size(), employees);
                return new XmlImportResultDto(inserted, updated, skipped, employees);
            }
        } catch (UnmarshalException ex) {
            LinkedHashMap<Object, Object> body = new LinkedHashMap<>();
            body.put("error", "Invalid XML (unmarshal failed)");
            Throwable cause = ex.getLinkedException() != null ? ex.getLinkedException() : ex.getCause();
            if (cause != null) body.put("details", cause.getMessage());
            return new XmlImportResultDto(0, 0, Collections.singletonList(body.toString()), Collections.emptyList());
        }
    }
}
