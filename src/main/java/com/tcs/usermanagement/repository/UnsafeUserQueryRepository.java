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
        String sql = "SELECT id, name, email, role FROM app_user WHERE name = ?";
        log.info("Executing parameterized SQL query for user lookup by name");
        return jdbcTemplate.queryForList(sql, name);
    }
}