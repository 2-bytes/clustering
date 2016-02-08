package cclusteringmodified.utils;
/**
 *
 * @author Vitalii Umanets
 */
import java.io.File;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataLoader {

    /**
     * Loads data from the given file as an array of numeric values.
     * Attention: Data is loaded from the first list.
     * @param path The path of the *.xlsx file to read.
     * @param omitFirstRow If the first row has names then this flag should be set to true to ignore it.
     * @throws IOException
     * @throws InvalidFormatException
     * @return Data loaded from the file as an array of
     */
    public static double[][] loadXLSXFile(String path, boolean omitFirstRow) throws IOException, InvalidFormatException{
        XSSFWorkbook wb = new XSSFWorkbook(new File(path));
        XSSFSheet sh = wb.getSheetAt(0); // Only the first one, maybe will put it as a parameter later
        int shift = omitFirstRow? 1 : 0;
        double[][] data = new double[sh.getLastRowNum() + 1 - shift][sh.getRow(shift).getPhysicalNumberOfCells()];
        for(int i=0;i < data.length;i++)
            for(int j=0; j < data[0].length;j++){
                data[i][j] = sh.getRow(i+shift).getCell(j).getNumericCellValue();
            }
        return data;
    }


}
