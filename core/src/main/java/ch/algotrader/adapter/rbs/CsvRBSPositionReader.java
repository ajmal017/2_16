/***********************************************************************************
 * AlgoTrader Enterprise Trading Framework
 *
 * Copyright (C) 2013 Flury Trading - All rights reserved
 *
 * All information contained herein is, and remains the property of Flury Trading.
 * The intellectual and technical concepts contained herein are proprietary to
 * Flury Trading. Modification, translation, reverse engineering, decompilation,
 * disassembly or reproduction of this material is strictly forbidden unless prior
 * written permission is obtained from Flury Trading
 *
 * Fur detailed terms and conditions consult the file LICENSE.txt or contact
 *
 * Flury Trading
 * Badenerstrasse 16
 * 8004 Zurich
 ***********************************************************************************/
package ch.algotrader.adapter.rbs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 * SuperCSV Reader that reads RBS position files
 *
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 *
 * @version $Revision$ $Date$
 */
public class CsvRBSPositionReader {

    //@formatter:off
    private static CellProcessor[] processor = new CellProcessor[] {
        new ParseInt(),
        new ParseLong(),
        new StrNotNullOrEmpty(),
        new ParseDate("dd/MM/yyyy"),
        new ParseBigDecimal(),
        new StrNotNullOrEmpty(),
        new StrNotNullOrEmpty(),
        new ParseBigDecimal(),
        new StrNotNullOrEmpty(),
        new ParseBigDecimal(),
        new ParseBigDecimal(),
        new ParseDate("dd/MM/yyyy"),
        new ParseInt(),
        new ParseInt(),
        new StrNotNullOrEmpty()
    };
    //@formatter:on

    public static List<Map<String, ? super Object>> readPositions(byte[] data) throws IOException {

        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(data));
        CsvMapReader mapReader = new CsvMapReader(inputStreamReader, CsvPreference.EXCEL_PREFERENCE);

        List<Map<String, ? super Object>> list;
        try {
            String[] header = mapReader.getHeader(true);

            list = new ArrayList<Map<String, ? super Object>>();

            Map<String, ? super Object> position;
            while ((position = mapReader.read(header, processor)) != null) {
                list.add(position);
            }

            return list;

        } finally {
            mapReader.close();
        }
    }
}
