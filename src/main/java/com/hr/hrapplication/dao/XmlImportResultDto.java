package com.hr.hrapplication.dao;

import com.hr.hrapplication.domain.Employee;

import java.util.List;

public record XmlImportResultDto(
        int inserted,
        int updated,
        List<String> skippedIds,
        List<EmployeeEntity> insertedRecords
) {
}
