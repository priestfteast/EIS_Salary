package Readers;

import Operators.Category;
import Operators.Operator;
import Operators.Shift;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Calculator extends Reader{

    private String not_ready_9;
    private String not_ready_12;
    private String loged_time_9;
    private String loged_time_12;
    private String talk_duration;
    private String A;
    private String B;
    private String C;

    public Calculator(String[]args) {
        this.not_ready_9 = args[4];
        this.not_ready_12 = args[5];
        this.loged_time_9 = args[6];
        this.loged_time_12 = args[7];
        this.talk_duration = args[3];
        A = args[0];
        B = args[1];
        C = args[2];
    }

    public void write_file(String path,Map<String, Operator> operators, String...args){

        Calculator calculator = new Calculator(args);




        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Operators_Salary");

        Map<String, Operator> map = calculator.category_check(operators);

        String[] list = {"ФИО","Добавочный","Ночь","Смен","Отработано часов","В сети за день","Не готов","Время разговора",
                "Звонков за день", "Звонков за час","Категория","Время в сети","Не готов","Время разговора","Бонус"};
        int cellnum = 0;
        Row row = sheet.createRow(0);
        for (String s : list) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(s);
        }


        int rownum = 1;
        String Categories =calculator.A+" || "+calculator.B+" || "+calculator.C;
        for (Operator op : map.values()) {
            row = sheet.createRow(rownum++);

            Object[] objArr = {op.getName(), op.getNumber(),op.getNight(),op.getShifts(), op.getTotal_loged_time(), op.getLoged_time(),
                    op.getLoged_out(), op.getAverage_talk_duration(),  op.getCalls_per_shift(), op.getCalls_per_hour(),
                    op.getCategory().toString(),calculator.check_loged_time(op),calculator. check_not_ready(op),calculator.check_talk_duration(op),op.getBonus()};
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
            FileOutputStream out = new FileOutputStream(new File(path+"\\salary.xls"));
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


    public Map<String, Operator>  category_check(Map<String, Operator> operators) {

        for (Operator op : operators.values()
                ) {
            if (op.getCalls_per_hour() >= Integer.parseInt(A.split("-")[1])) {
                op.setCategory(Category.A);
                op.setBonus(op.getBonus()+2000);
            }
            if (op.getCalls_per_hour() <= Integer.parseInt(B.split("-")[0]) && op.getCalls_per_hour() >= Integer.parseInt(B.split("-")[1])) {
                op.setCategory(Category.B);
                op.setBonus(op.getBonus()+1000);
            }
            if (op.getCalls_per_hour() <= Integer.parseInt(C.split("-")[0])) {
                op.setCategory(Category.C);
            }
        }

        return operators;
    }

//    checking 5 min matching
    public String check_loged_time(Operator op){
        String positive = "соблюдено";
        String negative = "не соблюдено";
        int loged_time_seconds = (int) get_seconds(op.getLoged_time());
        int loged_time_value = (int) get_seconds(loged_time_9);
        int delay_seconds;
        if(op.getShift()==Shift.nine){
            delay_seconds = 32400-loged_time_seconds;
            if(delay_seconds<=loged_time_value)
                return positive;
            else {
                op.setBonus(op.getBonus()-500);
                return negative;
            }
        }

        if(op.getShift()==Shift.twelve){
            delay_seconds = 43200-loged_time_seconds;
            if(delay_seconds<=loged_time_value)
                return positive;
            else
            {
                op.setBonus(op.getBonus()-500);
                return negative;
            }
        }

        return positive;
    }

//    checking not ready time matching
    public String check_not_ready(Operator op){
        String positive = "соблюдено";
        String negative = "не соблюдено";
        int not_ready_time_seconds = (int) get_seconds(op.getLoged_out());
        int not_ready_value;
        if(op.getShift()==Shift.nine) {
            not_ready_value = (int) get_seconds(not_ready_9);
            if(not_ready_time_seconds<= not_ready_value)
                return positive;
            else
            {
                op.setBonus(op.getBonus()-500);
                return negative;
            }
        }
        if(op.getShift()==Shift.twelve) {
            not_ready_value = (int) get_seconds(not_ready_12);
            if(not_ready_time_seconds<= not_ready_value)
                return positive;
            else
            {
                op.setBonus(op.getBonus()-500);
                return negative;
            }
        }

        return positive;
    }

    //    checking 5 min talking duration
    public String check_talk_duration(Operator op){
        String positive = "соблюдено";
        String negative = "не соблюдено";
        int talk_duration_seconds = (int) get_seconds(op.getAverage_talk_duration());
        int talk_duration_value = (int) get_seconds(talk_duration);
        if(talk_duration_seconds<=talk_duration_value)
            return positive;
        else
        {
            op.setBonus(op.getBonus()-500);
            return negative;
        }


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

        ArrayList<String> talkDurationtpaths = new ArrayList();
        File file4 = new File("C:\\Users\\Jeremy\\Documents\\reports\\время разговора");
        File[] files4 = file4.listFiles();

        for (File f: files4
                ) {
            talkDurationtpaths.add(f.getAbsolutePath());
        }
        ReadCallsDuration readCallsDuration = new ReadCallsDuration(talkDurationtpaths,readCallsQuantity.get_operators_with_calls_quantity());


        String[] arguments = new String[]{"10-8","7-7","6-0","5:10","90:30","102:30","5:10","5:10"};
        Calculator calculator = new Calculator(arguments);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Operators_Salary");

        Map<String, Operator> map = readCallsDuration.get_operators_with_calls_duration();

        map = calculator.category_check(map);


        String[] list = {"ФИО","Добавочный","Ночь","Смен","Отработано часов","В сети за день","Не готов","Время разговора",
                "Звонков за день", "Звонков за час","Категория","Время в сети","Не готов","Время разговора","Бонус"};
        int cellnum = 0;
        Row row = sheet.createRow(0);
        for (String s : list) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(s);
        }


        int rownum = 1;
        String Categories =calculator.A+" || "+calculator.B+" || "+calculator.C;
        for (Operator op : map.values()) {
            row = sheet.createRow(rownum++);

            Object[] objArr = {op.getName(), op.getNumber(),op.getNight(),op.getShifts(), op.getTotal_loged_time(), op.getLoged_time(),
                    op.getLoged_out(), op.getAverage_talk_duration(),  op.getCalls_per_shift(), op.getCalls_per_hour(),
                    op.getCategory().toString(),calculator.check_loged_time(op),calculator. check_not_ready(op),
                    calculator.check_talk_duration(op),op.getBonus()};
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
