package com.tcs.usermanagement.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UnsafeUserQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(UnsafeUserQueryRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> searchByNameVulnerable(String name) {
        String unsafeSql = "SELECT id, name, email, role FROM app_user WHERE name = '" + name + "'";
        log.warn("Executing intentionally unsafe SQL for demo purposes: {}", unsafeSql);
        return jdbcTemplate.queryForList(unsafeSql);
    }
}