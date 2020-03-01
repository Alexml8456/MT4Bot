package com.alex.repository;

import com.alex.services.CsvMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SQLRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CsvMetrics csvMetrics;

    private static final String PORTFOLIO_ST_TP = "select stop_loss, take_profit from portfolio_data where quantranks_id = ?";

    private static final String CREATE_MT4_STATISTICS = "CREATE\n" +
            "\tTABLE\n" +
            "\t\tIF NOT EXISTS `mt4_statistics`(\n" +
            "\t\t\t`SYMBOL` varchar(50) NOT NULL,\n" +
            "\t\t\t`PERIOD` int(11) NOT NULL,\n" +
            "\t\t\t`DT` datetime NOT NULL,\n" +
            "\t\t\t`UPGlobalX3Trend` int(1) NOT NULL,\n" +
            "\t\t\t`UPLocalX3Trend` int(1) NOT NULL,\n" +
            "\t\t\t`UPZigZagLocalTrend` int(1) NOT NULL,\n" +
            "\t\t\t`UPZigZagMainTrend` int(1) NOT NULL,\n" +
            "\t\t\t`SEFC10UP` int(1) NOT NULL,\n" +
            "\t\t\t`HalfTrendUP` int(1) NOT NULL,\n" +
            "\t\t\t`BBUpTrend` int(1) NOT NULL,\n" +
            "\t\t\t`BBUpMainTrend` int(1) NOT NULL,\n" +
            "\t\t\t`BBUpTrendIndex` int(100) NOT NULL,\n" +
            "\t\t\t`BBDownTrendIndex` int(100) NOT NULL,\n" +
            "\t\t\t`BrainTrend2StopUP` int(1) NOT NULL,\n" +
            "\t\t\t`BrainTrend2StopMainUP` int(1) NOT NULL,\n" +
            "\t\t\t`KX4ChannelUP` int(1) NOT NULL,\n" +
            "\t\t\t`KX4ChannelBuyIndex` int(100) NOT NULL,\n" +
            "\t\t\t`KX4ChannelSellIndex` int(100) NOT NULL,\n" +
            "\t\t\t`KX4LineUP` int(1) NOT NULL,\n" +
            "\t\t\t`KX4LineBuyIndex` int(100) NOT NULL,\n" +
            "\t\t\t`KX4LineSellIndex` int(100) NOT NULL,\n" +
            "\t\t\t`LastPrice` DOUBLE NOT NULL,\n" +
            "\t\t\t`LastLowPrice` DOUBLE NOT NULL,\n" +
            "\t\t\t`LastHighPrice` DOUBLE NOT NULL,\n" +
            "\t\t\t`TCondition` varchar(50),\n" +
            "\t\t\tINDEX(\n" +
            "\t\t\t\tSYMBOL,\n" +
            "\t\t\t\tPERIOD,\n" +
            "\t\t\t\tDT\n" +
            "\t\t\t)\n" +
            "\t\t) ENGINE = InnoDB DEFAULT CHARSET = utf8;";

    private static final String INSERT_VALUES = "INSERT INTO mt4_statistics(SYMBOL, PERIOD, DT, UPGlobalX3Trend, UPLocalX3Trend, UPZigZagLocalTrend, UPZigZagMainTrend," +
            "SEFC10UP, HalfTrendUP, BBUpTrend, BBUpMainTrend, BBUpTrendIndex, BBDownTrendIndex, BrainTrend2StopUP, BrainTrend2StopMainUP," +
            "KX4ChannelUP, KX4ChannelBuyIndex, KX4ChannelSellIndex, KX4LineUP, KX4LineBuyIndex, KX4LineSellIndex," +
            "LastPrice, LastLowPrice, LastHighPrice, TCondition) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    private static final String DROP_MT4 = "DROP TABLE IF EXISTS mt4_statistics;";

    public Map<String, Object> getPortfolioSTTP(String id) {
        return jdbcTemplate.queryForMap(PORTFOLIO_ST_TP, "" + id);
    }

    public void dropTable() {
        jdbcTemplate.execute(DROP_MT4);
        log.info("MT4 table was dropped!");
    }

    public void createTable() {
        jdbcTemplate.execute(CREATE_MT4_STATISTICS);
        log.info("MT4 table was created!");
    }

    public void insertValues() {
        jdbcTemplate.batchUpdate(INSERT_VALUES, csvMetrics.getCsvList());
        log.info("DB was updated with new values!");
    }
}
