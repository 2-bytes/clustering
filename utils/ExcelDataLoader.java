package cclusteringmodified.utils;
/**
 *
 * @author Vitalii Umanets
 */
import cclusteringmodified.Cluster;
import cclusteringmodified.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
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
        XSSFSheet sh = wb.getSheetAt(listNmbr);
        int shift = omitFirstRow? 1 : 0;
        if(rowsNmbr>(sh.getLastRowNum()+1-shift))
            throw new ArrayIndexOutOfBoundsException("File has less rows than specified");
        double[][] data = new double[rowsNmbr][sh.getRow(shift).getPhysicalNumberOfCells()];
        Random r = new Random();
        IntStream ints =  r.ints(shift, sh.getLastRowNum()).distinct().limit(rowsNmbr);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        int index;
        for (double[] row : data) {
            index = iterator.next();
            for (int j = 0; j < data[0].length; j++) {
                row[j] = sh.getRow(index).getCell(j).getNumericCellValue();
            }
        }
        return data;
    }

    public static void savetoXLSX(Cluster[] data) throws FileNotFoundException, IOException{
        XSSFWorkbook wb = new XSSFWorkbook();
        FileOutputStream out = new FileOutputStream("result.xlsx");
        XSSFSheet sheet = wb.createSheet("output");
        int counter=0;

        Row row = sheet.createRow(counter);
        counter++;
        int pLength = data[0].getPoints().get(0).coordinates.length;
        row.createCell(0).setCellValue("Tag");
        for(int i=1;i<=pLength;i++){
            row.createCell(i).setCellValue("x"+i);
        }
        row.createCell(pLength+1).setCellValue("Cluster");

        for(int i=0;i<data.length;i++){
            for(Point p :data[i].getPoints()){
               row = sheet.createRow(counter);
               row.createCell(0).setCellValue(p.tag);
               for(int j=0;j<pLength;j++)
                   row.createCell(j+1).setCellValue(p.coordinates[j]);
               row.createCell(pLength+1).setCellValue(i);
               counter++;
            }
        }
        wb.write(out);
        out.close();
    }


}
