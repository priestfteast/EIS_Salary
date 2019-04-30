package MVC.Steps;

public class Test {
    public static void main(String[] args) {
        int max =20;
        String A = max+"-"+(int)Math.round(max*0.75);
        String B= (int)(Math.round(max*0.75)-1)+"-"+(int) Math.round(max*0.65);
        String C = (int) Math.round(max*0.65)+"-"+0;
        System.out.println(A+" "+B+" "+C);
    }
}
