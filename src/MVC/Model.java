package MVC;

import Operators.Operator;
import Readers.*;

import java.util.ArrayList;
import java.util.Map;

public class Model {

    private Map<String, Operator> operators;


    public ReadOperators readOperators ;

    public ReadLogedTime readLogedTime;
    public ReadLogedOutTime readLogedOutTime;
    public ReadCallsQuantity readCallsQuantity;
    public ReadCallsDuration readCallsDuration;
    public Calculator calculator;



    public Map<String, Operator> ReadOperators(String path) throws Exception {
        readOperators = new ReadOperators(path);
        return readOperators.get_operators();
    }

    public Map<String, Operator> ReadOperatorsWithLogedTime(ArrayList<String> paths, Map<String, Operator> operators) throws Exception {
        readLogedTime = new ReadLogedTime(paths,operators);
        return readLogedTime.get_operators_with_loged_time();
    }


    //    sending categories values
    public String[] get_categories_values(){
        return readCallsQuantity.get_categories_values();
    }

//    writing final salary project
    public void write_file(String path,Map<String, Operator> operators, String...args){
        calculator = new Calculator(args);
        calculator.write_file(path,operators,args);
    }




    public Map<String, Operator> getOperators() {
        return operators;
    }

    public void setOperators(Map<String, Operator> operators) {
        this.operators = operators;
    }


    public static void main(String[] args) throws Exception {
        Model model = new Model();
        ArrayList<String> list = new ArrayList<>();
        list.add("E:\\Reports\\loged_time\\loged_time.xls");
        model.ReadOperatorsWithLogedTime(list,model.ReadOperators("C:\\Users\\Jeremy\\Documents\\Список.xls"));

    }

    public Map<String,Operator> ReadOperatorsWithLogedOutTime(ArrayList<String> paths, Map<String,Operator> operators) throws Exception {
        readLogedOutTime = new ReadLogedOutTime(paths,operators);
        return readLogedOutTime.get_operators_with_loged_out__time();
    }

    public Map<String,Operator> ReadOperatorsWithCallsQuantity(ArrayList<String> paths, Map<String,Operator> operators) throws Exception {
        readCallsQuantity = new ReadCallsQuantity(paths,operators);
        return readCallsQuantity.get_operators_with_calls_quantity();
    }

    public Map<String,Operator> ReadOperatorsWithTalkDuration(ArrayList<String> paths, Map<String,Operator> operators) throws Exception {
        readCallsDuration = new ReadCallsDuration(paths,operators);
        return readCallsDuration.get_operators_with_calls_duration();
    }
}
