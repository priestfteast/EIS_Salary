package Readers;

import Operators.Operator;
import Operators.Shift;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadLogedTime extends Reader {
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

    public ReadLogedTime(ArrayList<String> paths, Map<String, Operator> operators) {
        this.paths = paths;
        this.operators = operators;

//        filling night operators map
        for (Operator op:operators.values()
                ) {
            if(op.getNight()==true) {
                Operator oper = new Operator();
                oper.setNumber(op.getNumber());
                oper.setShifts(op.getShifts());
                night_operators.add(oper);
                op.setShifts(0);
            }


        }
    }



    public Map<String, Operator> get_operators_with_loged_time() throws Exception {


        for (int i = 0; i <paths.size() ; i++) {
//          creating map for storing list of shifts (loged time) for later average loged time counting
            Map<String, ArrayList<String>> map = new HashMap  <>();
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
//                    System.out.println(cell.getAddress() + " " + cell_data + " " + cell.getColumnIndex());
//                    filling list with operators numbers
                    operators_numbers.add(cell_data);
                }
                else {
                    closeReadBooks(readbookXls, readbookXlsx);
                    throw new Exception(String.format("Файл (%s)Некорректный формат доб. номера в ячейке %s" +
                            "\nКорректный формат: \"user123\"", paths.get(i), cell.getAddress().toString()));
                }

            }


            //      counting loged time
            for (Row row: readsheet) {
                for(Cell cell: row) {
                    String cell_data = dataFormatter.formatCellValue(cell);
                    if(cell_data.matches("^[0-9]+:[0-9]+:[0-9]+$")&&checkDayOff(readsheet,cell,operators)) {
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

//        checking quantity of night shifts

        for (Operator op: night_operators
             ) {
            if(op.getShifts()!=operators.get(op.getNumber()).getShifts())
                throw new Exception(String.format("Количество смен для ночника %s (%f) " +
                        "не совпадает с количеством смен указанных в списке операторов (%f)",
                        op.getNumber(),operators.get(op.getNumber()).getShifts(),op.getShifts()));
        }

        for (Operator op: night_operators
             ) {
            System.out.print(op.getNumber()+" "+op.getShifts()+" ");
        }
        System.out.println();
        for (Operator op:operators.values()
                ) {
            if(op.getNight()==true) {

                System.out.print(op.getNumber()+" "+op.getShifts()+" ");

            }

        }

        return  operators;
    }










    public void operate (Map <String, ArrayList<String>> map) throws Exception {



        for (Map.Entry<String,ArrayList<String>> mapp: map.entrySet()
             ) {
            String number = mapp.getKey();
            ArrayList list = mapp.getValue();


//            counting and setting total loged time
//            list.add( operators.get(number).getTotal_loged_time());
            String total_loged_time = get_Time(list);
            int time1 = (int) get_seconds( operators.get(number).getTotal_loged_time());
            int time2 = (int) get_seconds(total_loged_time);
            operators.get(number).setTotal_loged_time(get_Time(time1+time2));
//            counting and setting shifts
            double shifts = operators.get(number).getShifts();
            if( operators.get(number).getNight()==false)
            {
                shifts +=  list.size();

                operators.get(number).setShifts(shifts);
            }
            else
            {
                shifts += count_night_shifts(list);
                operators.get(number).setShifts(shifts);
            }

        }
        for (Operator op: operators.values()
             ) {
//            counting and setting average loged time
           int total_loged_time = (int) get_seconds( op.getTotal_loged_time());
           int average_loged_time = (int) (total_loged_time/op.getShifts());

           op.setLoged_time(get_Time(average_loged_time));
//            counting and setting Shift type
           op.setShift(check_shift(op.getLoged_time()));

        }
    }




    public static void main(String[] args) throws Exception {
        ReadOperators readOperators = new ReadOperators("C:\\Users\\Jeremy\\Documents\\reports\\Список.xlsx");
        ReadLogedTime readLogedTime = new ReadLogedTime(new ArrayList<>(),readOperators.get_operators());
        File file = new File("C:\\Users\\Jeremy\\Documents\\reports\\время в системе");
        File[] files = file.listFiles();
        for (File f: files
             ) {
            paths.add(f.getAbsolutePath());
        }




        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Operators_Salary");

        Map<String, Operator> map = readLogedTime.get_operators_with_loged_time();

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
                    op.getAverage_talk_duration(), op.getCalls_per_shift(), op.getCalls_quantity(), op.getBonus(), op.getBonus_explanation()};
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


    }






