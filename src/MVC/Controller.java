package MVC;

import Operators.Operator;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Controller {
    public static void setOperators(Map<String, Operator> operators) {
        Controller.operators = operators;
    }

    static View view = new View();

    Model model = new Model();


    private static Map<String, Operator> operators = new TreeMap<>();

    public String[] getCategories_values() {
        return categories_values;
    }
//    creating array for storing values of categories (10-8 etc.)
    String[] categories_values = new String[3];

//    creating array for storing values nececcery for final calculations
    String[] args = new String[8];

//    creating path for salary project file
    String path;

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public  Map<String, Operator> getOperators() {
        return operators;
    }

    public int getOperatorsSize(){
        return operators.size();
    }

    public Object[] getNightOperators(){
        ArrayList<String> nightoperators = new ArrayList<>();
        for (Operator op: operators.values()
             ) {
            if(op.getNight()==true)
                nightoperators.add(op.getName());
              }

              return  nightoperators.toArray();
    }

    public Object[] getNotLogedOperators(){
        ArrayList<String> operators2 = new ArrayList<>();
        for (Operator op: operators.values()
                ) {
            if(op.getTotal_loged_time().equals("0:0:0"))
                operators2.add(op.getName());
        }

        return  operators2.toArray();
    }

    public Object[] getNotLogedOutOperators(){
        ArrayList<String> operators2 = new ArrayList<>();
        for (Operator op: operators.values()
                ) {
            if(op.getTotalLoged_out().equals("0:0:0"))
                operators2.add(op.getName());
        }

        return  operators2.toArray();
    }

    public Object[] getZeroCallsOperators(){
        ArrayList<String> operators2 = new ArrayList<>();
        for (Operator op: operators.values()
                ) {
            if(op.getCalls_quantity() == 0)
                operators2.add(op.getName());
        }

        return  operators2.toArray();
    }

    public Object[] getZeroTalkDuration(){
        ArrayList<String> operators2 = new ArrayList<>();
        for (Operator op: operators.values()
                ) {
            if(op.getAverage_talk_duration().equals("0:0:0"))
                operators2.add(op.getName());
        }

        return  operators2.toArray();
    }

    //    sending categories values
    public void set_categories_values(){
         categories_values= model.get_categories_values();


    }

    //    writing final salary project
    public void write_file(){
        model.write_file(this.path,this.getOperators(),this.args);
    }


    public String[] getArgs() {
        return args;
    }

    public String getPath() {
        return path;
    }

    public static void main(String[] args) throws Exception {
       view.run();

//        Controller controller = new Controller();
//        ArrayList<String> list = new ArrayList<>();
//        list.add("E:\\Reports\\loged_time\\loged_time.xls");
//        controller.set_operators_with_loged_time(list);
    }

    public void set_raw_operators(String path) throws Exception{
        operators = model.ReadOperators(path);

    }
    public void set_operators_with_loged_time(ArrayList<String> paths) throws Exception{
      operators = model.ReadOperatorsWithLogedTime(paths,getOperators());

    }

    public void set_operators_with_loged_out_time(ArrayList<String> paths) throws Exception{
        operators = model.ReadOperatorsWithLogedOutTime(paths,getOperators());

    }

    public void set_operators_with_calls_quantity(ArrayList<String> paths) throws Exception{
        operators = model.ReadOperatorsWithCallsQuantity(paths,getOperators());

    }

    public void set_operators_with_talk_duration(ArrayList<String> paths) throws Exception{
        operators = model.ReadOperatorsWithTalkDuration(paths,getOperators());

    }




}
