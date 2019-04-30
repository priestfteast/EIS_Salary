package Readers;

import Operators.Operator;
import Operators.Shift;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadCallsQuantity extends Reader {

//    creating categories values
    String A ="";
    String B ="";
    String C ="";

    //    creating an array with paths to sheets with loged time info (initialized in constructor)
    public static ArrayList<String> paths;

    //Creating a Treemap of operators
    Map<String, Operator> operators ;

    //Creating a list of night operators for checking night shifts quantity in supervisors list of operators and quantity calculated by this class
    ArrayList<Operator> night_operators = new ArrayList<>() ;

    //    Creating a list of operators that will be filled from loged time files and compared with initial list from operators map
    ArrayList<String> operators_numbers = new ArrayList<>();

    //    Creating list of dates that will be filled from loged time files and compared with calender
    ArrayList<Calendar> days = new ArrayList<>();

    public ReadCallsQuantity(ArrayList<String> paths, Map<String, Operator> operators) {
        this.paths = paths;
        this.operators = operators;
    }


    public Map<String, Operator> get_operators_with_calls_quantity() throws Exception {


        for (int i = 0; i <paths.size() ; i++) {
//          creating map for storing list of shifts (loged time) for later average loged time counting
            Map<String, ArrayList<String>> map = new HashMap<>();
            for (String number: operators.keySet()
                    ) {
                map.put(number,new ArrayList<>());
            }



            File file = new File(paths.get(i));

            // Creating a Workbook from an Excel file (.xls or .xlsx)
            Workbook readbookXls = null;
            XSSFWorkbook readbookXlsx = null;

            if (XlsCheck(paths.get(i)) == 1) {
                readbookXls = WorkbookFactory.create(file);
            }
            if (XlsCheck(paths.get(i)) == 2) {
                readbookXlsx = new XSSFWorkbook(new FileInputStream(file));
            }
            if (XlsCheck(paths.get(i)) == 3)
                throw new Exception(String.format("Указанный файл (%s) не является таблицей Excel. Перезагрузите файл.",paths.get(i)));

            // Getting the Sheet at index zero
            Sheet readsheet = null;
            if (readbookXls != null)
                readsheet = readbookXls.getSheetAt(0);
            else
                readsheet = readbookXlsx.getSheetAt(0);



            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
//        getting columns and rows quantity
            int columns = 0;
            try{
                columns = readsheet.getRow(0).getLastCellNum();
            }
            catch (NullPointerException ex){
                columns = readsheet.getRow(1).getLastCellNum();
//                System.out.println(columns);
            }

            int rows = readsheet.getLastRowNum();

//      filling missing dates list
            for (Row row: readsheet) {
                for(Cell cell: row) {
                    String cell_data = dataFormatter.formatCellValue(cell);
                    if(cell_data.matches("^[0-9]+/[0-9]+/[0-9]{4}")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");
                        Date date;
                        try {
                            Calendar calendar = new GregorianCalendar();
                            date = simpleDateFormat.parse(cell_data);
                            calendar.setTime(date);
                            days.add(calendar);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

//      Checking matching usernumber format in first row
            for (int j = 1; j < columns; j++) {
                Row row = readsheet.getRow(0);
                Cell cell;
                try{
                    cell = row.getCell(j);
                }
                catch (NullPointerException ex){
                    row = readsheet.getRow(1);
                    cell = row.getCell(j);
                }

                String cell_data = dataFormatter.formatCellValue(cell);

                if (checkNumberFormat(cell_data)) {

//     filling list with operators numbers
                    operators_numbers.add(cell_data);
                }
                else {
                    closeReadBooks(readbookXls, readbookXlsx);
                    throw new Exception(String.format("Файл (%s)Некорректный формат доб. номера в ячейке %s" +
                            "\nКорректный формат: \"user123\"", paths.get(i), cell.getAddress().toString()));
                }

            }


            //      counting calls quantity
            for (Row row: readsheet) {
                for(Cell cell: row) {
                    String cell_data = dataFormatter.formatCellValue(cell);
                    if(cell_data.matches("^[0-9]{1,3}$")&&checkDayOff(readsheet,cell,operators)) {
                        String cell_address = String.valueOf(cell.getColumnIndex());
                        String op_number;
                        try{
                            op_number = readsheet.getRow(0).getCell(Integer.parseInt(cell_address)).getStringCellValue();
                        }
                        catch (NullPointerException ex){
                            op_number = readsheet.getRow(1).getCell(Integer.parseInt(cell_address)).getStringCellValue();
                        }

                        if(operators.containsKey(op_number))
                        {

                            map.get(op_number).add(cell_data);
                        }
                    }
                }

            }

            // Closing the workbook
            closeReadBooks(readbookXls, readbookXlsx);

//
            operate(map);
        }
//        checking days matching
        CheckCalender(days);

//      Checking operators fullness in loged time files

        CheckoperatorsFullness(operators,operators_numbers);


//      calculating categories values
        calculate_caregories_values();




        return  operators;
    }










    public void operate (Map <String, ArrayList<String>> map) throws Exception {



        for (Map.Entry<String,ArrayList<String>> mapp: map.entrySet()
                ) {
            String number = mapp.getKey();
            ArrayList list = mapp.getValue();


//            counting and setting total calls quantity

            int total_calls_quantity = get_calls_quantity(list);
            int calls_already_written = operators.get(number).getCalls_quantity();

            operators.get(number).setCalls_quantity(calls_already_written+total_calls_quantity);

        }
        for (Operator op: operators.values()
                ) {
//            counting and setting average calls quantity
            int total_calls_quantity = op.getCalls_quantity();
            int calls_per_shift =  ((int)Math.round(total_calls_quantity/op.getShifts()));

            op.setCalls_per_shift(calls_per_shift);
            if(op.getShift()==Shift.nine)
                op.setCalls_per_hour((int)Math.round(op.getCalls_per_shift()/7.5));
            else
                op.setCalls_per_hour((int)Math.round(op.getCalls_per_shift()/10.5));

        }
    }

//    sending categories values
    public String[] get_categories_values(){
        return new String[]{A,B,C};
    }


    public static void main(String[] args) throws Exception {
        ReadOperators readOperators = new ReadOperators("C:\\Users\\Jeremy\\Documents\\reports\\Список.xlsx");
        ArrayList<String> logedpaths = new ArrayList();
        File file = new File("C:\\Users\\Jeremy\\Documents\\reports\\время в системе");
        File[] files = file.listFiles();

        for (File f: files
                ) {
            logedpaths.add(f.getAbsolutePath());
        }
        ReadLogedTime readLogedTime = new ReadLogedTime(logedpaths,readOperators.get_operators());

        ArrayList<String> logedOutpaths = new ArrayList();
        File file2 = new File("C:\\Users\\Jeremy\\Documents\\reports\\не готов");
        File[] files2 = file2.listFiles();

        for (File f: files2
                ) {
            logedOutpaths.add(f.getAbsolutePath());
        }
        ReadLogedOutTime readLogedOutTime = new ReadLogedOutTime(logedOutpaths,readLogedTime.get_operators_with_loged_time());

        ArrayList<String> CallsQuantitypaths = new ArrayList();
        File file3 = new File("C:\\Users\\Jeremy\\Documents\\reports\\отвеченные");
        File[] files3 = file3.listFiles();

        for (File f: files3
                ) {
            CallsQuantitypaths.add(f.getAbsolutePath());
        }
        ReadCallsQuantity readCallsQuantity = new ReadCallsQuantity(CallsQuantitypaths,readLogedOutTime.get_operators_with_loged_out__time());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Operators_Salary");

        Map<String, Operator> map = readCallsQuantity.get_operators_with_calls_quantity();

        String[] list = {"ФИО","Добавочный","Ночь","Смен","Отработано часов","В сети за день","Не готов","Время разговора","Звонков за день","Звонков за час","Премия","Пояснения"};
        int cellnum = 0;
        Row row = sheet.createRow(0);
        for (String s : list) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(s);
        }


        int rownum = 1;
        String Categories ="";
        for (Operator op : map.values()) {
            row = sheet.createRow(rownum++);
            Categories =op.getCategory_explanation();
            Object[] objArr = {op.getName(), op.getNumber(),op.getNight(),op.getShifts(), op.getTotal_loged_time(), op.getLoged_time(), op.getLoged_out(),
                    op.getAverage_talk_duration(), op.getCalls_per_shift(), op.getCalls_per_hour(), op.getBonus(), op.getBonus_explanation()};
            cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);


                if (obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if (obj instanceof Boolean)
                    cell.setCellValue((Boolean) obj);
                else if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((int) obj);
            }
        }
        Cell cell = sheet.createRow(rownum+3).createCell(0);
        cell.setCellValue(Categories);

        try {
            FileOutputStream out = new FileOutputStream(new File("E:\\Salary.xls"));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


//        calculating categories values
    public void calculate_caregories_values() {
        int max = 0;

        for (Operator op : operators.values()
                ) {
            if (op.getCalls_per_hour() >= max)
                max = op.getCalls_per_hour();
        }
        A = max + "-" + (int) Math.round(max * 0.75);
        B = (int) (Math.round(max * 0.75) - 1) + "-" + (int) Math.round(max * 0.65);
        C = (int)( Math.round(max * 0.65)-1) + "-" + 0;

        System.out.println(A+" "+B+" "+C);
    }



}
