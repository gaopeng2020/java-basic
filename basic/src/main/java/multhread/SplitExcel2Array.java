package multhread;

import ept.commonapi.EPTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@AllArgsConstructor
public class SplitExcel2Array {
    private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors() / 2;
    private File file;
    private String range;

    public Map<String, String>[] getSplitedDocInfo() {
        Map<String, String>[] maps = new Map[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            maps[i] = new LinkedHashMap<>();
        }
        Workbook workbook = null;
        try {
            EPTUtils.getWorkBookFromFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        //默认封装表格所有内容
        if ("".equals(range) || range.isEmpty()) {
            int i = 1;
            while (i <= lastRowNum) {
                for (int j = 0; j < THREAD_NUM; j++) {
                    if (i + j <= lastRowNum) {
                        encapsulateInfo2Map(sheet, maps[j], i + j);
                    }
                }
                i += THREAD_NUM;
            }
            //封装指定范围的内容
        } else {

        }
        return maps;
    }

    private void encapsulateInfo2Map(Sheet sheet, Map<String, String> map, int index) {
        Row row = sheet.getRow(index);
        String id = EPTUtils.stringFormat(row.getCell(2).getStringCellValue());
        String name = EPTUtils.stringFormat(row.getCell(1).getStringCellValue());
        name = EPTUtils.wipeSpecialSymbol(name);
        map.put(id, name);
    }
}
