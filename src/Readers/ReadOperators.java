package Readers;

import Operators.Operator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.collections.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReadOperators extends Reader {
//    creating a path to a file with operators list
        public static String path;

        //Creating a Treemap of operators
        Map<String, Operator> operators = Collections.synchronizedMap( new TreeMap<>());

        public ReadOperators(String path) {
        this.path = path;
    }



        public Map<String, Operator> get_operators() throws Exception {


            File file = new File(path);

            // Creating a Workbook from an Excel file (.xls or .xlsx)
            Workbook readbookXls = null;
            XSSFWorkbook readbookXlsx = null;

            if(XlsCheck(path)==1) {
                readbookXls = WorkbookFactory.create(file);
            }
            if(XlsCheck(path)==2) {
                readbookXlsx = new XSSFWorkbook(new FileInputStream(file));
            }
            if(XlsCheck(path)==3)
                throw new Exception("Указанный файл не является таблицей Excel. Перезагрузите файл.");

            // Getting the Sheet at index zero
            Sheet readsheet = null;
            if(readbookXls!=null)
                readsheet = readbookXls.getSheetAt(0);
            else
                readsheet = readbookXlsx.getSheetAt(0);




            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            // 2. Or you can use a for-each loop to iterate over the rows and columns

            for (Row row : readsheet) {
                String FIO = "";
                String dob = "";
                String night = "";
                String shifts_quantity = "";
                String date = "";

                Operator op = new Operator();
                for (Cell cell : row) {

                    if (row.getRowNum() == 0) {
                        if(     row.getCell(0)==null||!row.getCell(0).getStringCellValue().equals("ФИО")||
                                row.getCell(1)==null|| !row.getCell(1).getStringCellValue().equals("Доб")||
                                row.getCell(2)==null|| !row.getCell(2).getStringCellValue().equals("Ночь")||
                                row.getCell(3)==null|| !row.getCell(3).getStringCellValue().equals("Количество смен")||
                                row.getCell(4)==null ||!row.getCell(4).getStringCellValue().equals("Неполный день")  )
                        {
                            closeReadBooks(readbookXls,readbookXlsx);
                            throw new Exception("Некорректный формат таблицы. Корректный формат: |ФИО|Доб|Ночь|Количество смен|Неполный день|");
                        }

                    }
                }

            }

            // 2. Or you can use a for-each loop to iterate over the rows and columns

            for (Row row: readsheet) {
                String number ="";
                String name ="";
                Date date;
                Operator op = new Operator();
                for(Cell cell: row) {
                    if(row.getRowNum()!=0) {
                        String cell_data = dataFormatter.formatCellValue(cell);
                        if(cell.getAddress().toString().contains("A"))
                        {  name = cell_data;
                            if(nameDuplicate(name)) {
                                closeReadBooks(readbookXls,readbookXlsx);
                                throw new Exception("Задвоение оператора " + name +
                                        "\nПожалуйста внесите исправления в список и перезагрузите его");

                            }
                            if(!checkNameFormat(name)) {
                                closeReadBooks(readbookXls, readbookXlsx);
                                throw new Exception(String.format("Некорректный формат имени в ячейке %s" +
                                        "\nКорректный формат: Имя Фамилия (пример: Иванов Иван)", cell.getAddress().toString()));
                            }
                            else
                                op.setName(name);
                        }
                        if(cell.getAddress().toString().contains("B"))
                        {   number =cell_data.trim();

                            if(operators.containsKey(number)) {
                                closeReadBooks(readbookXls,readbookXlsx);
                            throw new Exception("Задвоение доб. номера " + number + " для " + name+
                                    "\nПожалуйста внесите исправления в список и перезагрузите его");

                            }
                            if(!checkNumberFormat(number)) {
                                closeReadBooks(readbookXls, readbookXlsx);
                                throw new Exception(String.format("Некорректный формат доб. номера в ячейке %s" +
                                        "\nКорректный формат: \"user123\"", cell.getAddress().toString()));
                            }

                            op.setNumber(number);
                        }
                        if(cell.getAddress().toString().contains("C")&&cell_data.equals("н")){
                            op.setNight(true);
                                                   }
                        if(cell.getAddress().toString().contains("D")&&!cell_data.equals("")){
                            cell_data= cell_data.replace(",",".");
                            op.setShifts(Double.valueOf(cell_data));
                        }


                        if(cell.getAddress().toString().contains("E")&&cell_data.length()>4){
                            try {
                                op.getTimeOff().addAll(getDayOffs(name, cell_data.split("\\s+")));
                            }
                            catch (Exception e){
                                closeReadBooks(readbookXls, readbookXlsx);
                                throw e;
                            }

                        }
                        if(op.getNumber()!=null&&op.getName()!=null&&!operators.containsKey(op.getNumber()))
                            operators.put(number,op);

                    }


                }

            }


            // Closing the workbook
           closeReadBooks(readbookXls,readbookXlsx);

//            for (Operator op:operators.values()
//                    ) {
//                System.out.println(op.getName()+" "+op.getNumber()+ op.getTimeOff());
//            }

            return  operators;

        }





        public static void main(String[] args) throws Exception {
            ReadOperators readOperators = new ReadOperators("C:\\Users\\Jeremy\\Documents\\reports\\Список.xlsx");
            readOperators.get_operators();
        }
        public boolean nameDuplicate(String name){
            for (Operator op: operators.values()
                 ) {
                if(op.getName().equals(name))
                    return true;
            }
            return false;
        }

        public ArrayList<Date> getDayOffs(String name, String[] dates) throws Exception {
           ArrayList<Date> list = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.M.yyyy");
            for (String s: dates
                 ) {
                try {
                    Date date = simpleDateFormat.parse(s);
                    list.add(date);
                                  }
                catch (ParseException ex){
                    throw new Exception(String.format("Некорректный формат времени %s для оператора %s. Корректный формат д.м.год (2.2.2019) Раздел между датами пробелом.",
                            s,name));
                }
            }
            return list;
        }

    }


