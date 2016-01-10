package com.github.slamm.riskmatch;

import static java.lang.Long.valueOf;
import static java.lang.Math.multiplyExact;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Table implements ITable {

    private enum Operation {
        GET {

            @Override
            public long calculate(long... values) {
                return values[0];
            }
        },
        MULT {

            @Override
            public long calculate(long... values) {
                return multiplyExact(values[0], values[1]);
            }
        },
        SUM {

            @Override
            public long calculate(long... values) {
                return stream(values).sum();
            }
        };

        public abstract long calculate(long... values);
    }

    private final Map<String, Set<Entry<Long, Long>>> cells = new HashMap<>();

    @Override
    public long query(String queryString) {
        Operation operation = acquireOperation(queryString);
        String[] arguments = acquireArguments(queryString);
        long[] values = stream(arguments).mapToLong(this::getCellValue).toArray();
        return operation.calculate(values);
    }

    @Override
    public void set(String cellDef, long value) {
        String cellName = getCellName(cellDef);
        Long row = getCellRow(cellDef);
        Set<Entry<Long, Long>> cell = cells.get(cellName);
        if (cell == null) {
            cell = new HashSet<>();
        }
        cell.add(new SimpleEntry<>(row, value));
        cells.put(cellName, cell);
    }

    private long getCellValue(String cellDef) {
        String cellName = getCellName(cellDef);
        Long row = getCellRow(cellDef);
        Set<Entry<Long, Long>> cell = cells.get(cellName);
        if (cell == null) {
            return 0L;
        }
        return cell.stream().filter(r -> r.getKey().equals(row)).map(r -> r.getValue()).findAny().orElse(0L);
    }

    private static String[] acquireArguments(String queryString) {
        String[] query = queryString.split(" ");
        if (query.length < 2) {
            throw new IllegalArgumentException();
        }
        return copyOfRange(query, 1, query.length);
    }

    private static Operation acquireOperation(String query) {
        return stream(Operation.values()).filter(op -> query.startsWith(op.toString())).findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static String getCellName(String cellDef) {
        return cellDef.substring(0, 1);
    }

    private static long getCellRow(String cellDef) {
        return valueOf(cellDef.substring(1));
    }
}
