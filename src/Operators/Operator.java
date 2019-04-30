package Operators;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Operator {

    private String name;
    private String number;
    private String loged_time;
    private String total_loged_time;
    private String totalLoged_out;
    private String loged_out;
    private String average_talk_duration;
    private Boolean night;
    private Shift shift;
    private int calls_quantity;
    private double shifts;
    private Category category;
    private String Category_explanation;
    private  int bonus;
    private String bonus_explanation="";
    private int calls_per_shift;
    private int calls_per_hour;
    private ArrayList<Date> timeOff = new ArrayList();
    private ArrayList<String> talk_duration = new ArrayList();
    public ArrayList<Date> getTimeOff() {
        return timeOff;
    }

    public ArrayList<String> getTalk_duration() {
        return talk_duration;
    }

    public void setTalk_duration(ArrayList<String> talk_duration) {
        this.talk_duration = talk_duration;
    }
    public void setTimeOff(ArrayList<Date> timeOff) {
        this.timeOff = timeOff;
    }

    public int getCalls_per_hour() {
        return calls_per_hour;
    }

    public void setCalls_per_hour(int calls_per_hour) {
        this.calls_per_hour = calls_per_hour;
    }
    public int getCalls_per_shift() {
        return calls_per_shift;
    }

    public void setCalls_per_shift(int calls_per_shift) {
        this.calls_per_shift = calls_per_shift;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operator)) return false;
        Operator operator = (Operator) o;
        return Objects.equals(getName(), operator.getName()) &&
                Objects.equals(getNumber(), operator.getNumber());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getNumber());
    }

    public String getBonus_explanation() {
        return bonus_explanation;
    }

    public void setBonus_explanation(String bonus_explanation) {
        this.bonus_explanation = bonus_explanation;
    }



    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public double getShifts() {
        return shifts;
    }

    public void setShifts(double shifts) {
        this.shifts = shifts;
    }


    public String getCategory_explanation() {
        return Category_explanation;
    }

    public void setCategory_explanation(String category_explanation) {
        Category_explanation = category_explanation;
    }

    public String getTotalLoged_out() {
        return totalLoged_out;
    }

    public void setTotalLoged_out(String totalLoged_out) {
        this.totalLoged_out = totalLoged_out;
    }



    public Operator() {

        shift = Shift.nine;
        shifts =0;
        calls_quantity = 0;
        calls_per_shift=0;
        night = false;
        totalLoged_out = "0:0:0";
        loged_out = "0:0:0";
        total_loged_time = "0:0:0";
        loged_time = "0:0:0";
        average_talk_duration= "0:0:0";
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setLoged_time(String loged_time) {
        this.loged_time = loged_time;
    }
    public String getTotal_loged_time() {
        return total_loged_time;
    }
    public Boolean getNight() {
        return night;
    }

    public void setNight(Boolean night) {
        this.night = night;
    }

    public void setLoged_out(String loged_out) {
        this.loged_out = loged_out;
    }
    public void setTotal_loged_time(String total_loged_time) {
        this.total_loged_time = total_loged_time;
    }

    public void setAverage_talk_duration(String average_talk_duration) {
        this.average_talk_duration = average_talk_duration;
    }
    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setCalls_quantity(int calls_quantity) {
        this.calls_quantity = calls_quantity;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {

        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getLoged_time() {
        return loged_time;
    }

    public String getLoged_out() {
        return loged_out;
    }

    public String getAverage_talk_duration() {
        return average_talk_duration;
    }

    public int getCalls_quantity() {
        return calls_quantity;
    }

    public Category getCategory() {
        return category;
    }
}
