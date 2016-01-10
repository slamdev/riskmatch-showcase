package com.github.slamm.riskmatch;

public interface ITable {

    /**
     * Sets the value of the column to specified value.
     *
     * @param cell string in cell string format
     * @param value positive integer [1, 1_000_000_000]
     */
    void set(String cell, long value);

    /**
     * Runs specified query.
     *
     * @param queryString string in query string format
     * @return query result
     */
    long query(String queryString);
}
