package com.alex.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SQLRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String PORTFOLIO_ST_TP = "select stop_loss, take_profit from portfolio_data where quantranks_id = ?";

    public Map<String, Object> getPortfolioSTTP(String id) {
        return jdbcTemplate.queryForMap(PORTFOLIO_ST_TP, "" + id);
    }
}
