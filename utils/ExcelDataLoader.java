package cclusteringmodified.utils;
/**
 *
 * @author Vitalii Umanets
 */
import java.io.File;
import java.io.IOException;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataLoader {

    /**
     * Loads data from the given file as an array of numeric values.
     * @param path The path of the *.xlsx file to read.
     * @param listNmbr Number of the list to read.
     * @param rowsNmbr Represents the number of random rows to be read. They will be unordered!
     * @param omitFirstRow If the first row has names then this flag should be set to true to ignore it.
     * @throws IOException
     * @throws InvalidFormatException
     * @return Data loaded from the file as an array of
     */
    public static double[][] loadXLSXFile(String path, int listNmbr,int rowsNmbr, boolean omitFirstRow) throws IOException, InvalidFormatException{
        XSSFWorkbook wb = new XSSFWorkbook(new File(path));
        XSSFSheet sh = wb.getSheetAt(listNmbr); // Only the first one, maybe will put it as a parameter later
        int shift = omitFirstRow? 1 : 0;
        if(rowsNmbr>sh.getLastRowNum())
            throw new ArrayIndexOutOfBoundsException("File has less rows than specified");
        double[][] data = new double[rowsNmbr + 1 - shift][sh.getRow(shift).getPhysicalNumberOfCells()];
        Random r = new Random();
        IntStream ints =  r.ints(shift, sh.getLastRowNum() + 1).distinct().limit(rowsNmbr);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        int index;
        for(int i = 0;i < data.length;i++){
            index = iterator.next();
            for(int j = 0; j < data[0].length;j++)
                data[i][j] = sh.getRow(index).getCell(j).getNumericCellValue();
            }
        return data;
    }


}
