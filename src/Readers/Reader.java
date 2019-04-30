package Readers;

import Operators.Operator;
import Operators.Shift;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reader {

    public int XlsCheck (String filename){
        if(filename.endsWith(".xls"))
            return 1;
        if(filename.endsWith(".xlsx"))
            return 2;
        else return 3;
    }

    public boolean checkNameFormat(String name){
        return name.matches("^[Ёёа-яА-Я]+\\s[Ёёа-яА-Я]+\\s?$");

    }
    public boolean checkNumberFormat(String name){
        return name.matches("^\\s?user[0-9]+\\s?$");

    }

    public void closeReadBooks(Workbook readbookXls, XSSFWorkbook readbookXlsx) throws IOException {
        if( readbookXls!=null)
            readbookXls.close();
        if( readbookXlsx!=null)
            readbookXlsx.close();

    }


    public void CheckoperatorsFullness(Map<String,Operator> map, ArrayList<String> list) throws Exception {
        ArrayList<String> missingOperators = new ArrayList<>();
        for (String number:map.keySet()
                ) {
            if(!list.contains(number))
                missingOperators.add(number);
        }
        if(missingOperators.size()>0)
            throw new Exception(String.format("В загруженных файлах нет информации об оператораторах (%d): %s",
                    missingOperators.size(), missingOperators));
    }

    public void CheckCalender (ArrayList<Calendar> days) throws Exception {
        ArrayList<Calendar> calendars = new ArrayList<>();
        ArrayList<Date> missingDates = new ArrayList<>();

        int max = days.get(0).getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= max; i++) {
            calendars.add(new GregorianCalendar(days.get(0).get(1),days.get(0).get(2),i));
        }
        for (Calendar c:calendars
                ) {

            if(!days.contains(c))
                missingDates.add(c.getTime());
        }

        if(missingDates.size()>0)
            throw new Exception(String.format("В загруженных файлах нет информации по датам (%d): %s",
                    missingDates.size(), missingDates));

    }
    public double count_night_shifts (ArrayList<String> list){
        double count = 0;

        for (String a: list
                ) {
            if(get_seconds(a)<30600)
                count+=0.5;
            else
                count+=1;
        }
//        System.out.println(count);
        return count;
    }
    public double get_seconds(String time) {
        String[] list = time.split(":");

        double seconds = 0;
        if(list.length==3) {
            seconds += Integer.parseInt(list[0]) * 60 * 60;
            seconds += Integer.parseInt(list[1]) * 60;
            seconds += Integer.parseInt(list[2]);
        }
        if(list.length==2)
        {
            seconds += Integer.parseInt(list[0]) * 60;
            seconds += Integer.parseInt(list[1]);
        }

        return seconds;

    }


    public int get_calls_quantity(ArrayList<String> list){
        int result = 0;
        for (String s: list
             ) {
            result+=Integer.parseInt(s);
        }
        return result;
    }

    public String get_Time(int seconds) {


        int final_hours = (int) (seconds/3600);
        int seconds_left = (int)seconds%3600;
        int minutes_final = (int)seconds_left/60;
        int seconds_final =(int) seconds_left%60;
        String Time = String.valueOf(final_hours)+":"+String.valueOf(minutes_final)+":"+seconds_final;

        return Time;
    }

    public String get_Time(ArrayList<String> list) {
        int seconds = 0;
        for (String string: list
                ) {
            seconds+=get_seconds(string);
        }
        int final_hours = (int) (seconds/3600);
        int seconds_left = (int)seconds%3600;
        int minutes_final = (int)seconds_left/60;
        int seconds_final =(int) seconds_left%60;
        String time = String.valueOf(final_hours)+":"+String.valueOf(minutes_final)+":"+seconds_final;

        return time;
    }


    public Shift check_shift (String hours){
        String h = hours.substring(0,hours.indexOf(":"));

        if(Double.parseDouble(h)>10)
            return Shift.twelve;
        else
            return Shift.nine;
    }





    public boolean checkDayOff(Sheet readsheet, Cell cell,  Map<String, Operator> operators) throws ParseException {
//        From a cell & readsheet we get date and operators number. Then we check matching in operators dayOff list.
        String cell_address = String.valueOf(cell.getColumnIndex());
        String op_number;
        try{
            op_number = readsheet.getRow(0).getCell(Integer.parseInt(cell_address)).getStringCellValue();
        }
        catch (NullPointerException ex){
            op_number = readsheet.getRow(1).getCell(Integer.parseInt(cell_address)).getStringCellValue();
        }
        Row row = readsheet.getRow( cell.getRowIndex());
        Cell date_cell = row.getCell(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");
        DataFormatter dataFormatter = new DataFormatter();
        String cell_data = dataFormatter.formatCellValue(date_cell);
        Date date = simpleDateFormat.parse(cell_data);
        if(operators.containsKey(op_number) && operators.get(op_number).getTimeOff().contains(date)){
            System.out.println(op_number+" contains "+date_cell);
            return false;}

        if(operators.containsKey(op_number) && (date.getDay()==0||date.getDay()==6))
            return false;

        return true;

    }
}
