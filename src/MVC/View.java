package MVC;

import MVC.Steps.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends JFrame {

    static Controller controller  = new Controller();
    static boolean stepOneCompleted = false;
    static boolean stepTwoCompleted = false;
    static boolean stepThreeCompleted = false;
    static boolean stepFourCompleted = false;
    static boolean stepFiveCompleted = false;
    static boolean stepSixCompleted = false;

    public static void setStepFiveCompleted(boolean stepFiveCompleted) {
        View.stepFiveCompleted = stepFiveCompleted;
    }

    public static void setStepSixCompleted(boolean stepSixCompleted) {
        View.stepSixCompleted = stepSixCompleted;
    }



    public static void setStepTwoCompleted(boolean stepTwoCompleted) {
        View.stepTwoCompleted = stepTwoCompleted;
    }

    public static void setStepThreeCompleted(boolean stepThreeCompleted) {
        View.stepThreeCompleted = stepThreeCompleted;
    }

    public static void setStepFourCompleted(boolean stepFourCompleted) {
        View.stepFourCompleted = stepFourCompleted;
    }

    public void setStepOneCompleted(boolean stepOneCompleted) {
        this.stepOneCompleted = stepOneCompleted;
    }

    public View(String title) throws HeadlessException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super(title);
    }

    public View() throws HeadlessException {

    }

    public static void run () throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        View jFrame = new View("Расчет премий ЕИС");
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds((int)dimension.getWidth()/2-400,(int)dimension.getHeight()/2-350,800,700);
//        jFrame.setBounds(50,50,700,700);
        jFrame.setIconImage(new ImageIcon("E:\\robot.png").getImage() );

//      creating on close dialog window
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                String[] options = { "ДА", "Нет"};
                int x = JOptionPane.showOptionDialog(null, "Вы уверены, что хотите покинуть этот ШЕДЕВР!!??",
                        null,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if(x==0)
                    jFrame.dispose();


            }
        });
        JPanel stepOnePanel = new Step1(jFrame, controller).getJPanel();
        jFrame.add(stepOnePanel);

        JPanel stepThreePanel;
        JPanel stepFourPanel ;
        JPanel stepFivePanel ;
        JPanel stepSixPanel ;


        jFrame.setVisible(true);


        Thread StepOnethread = new Thread(){

            @Override
            public void run() {
                while (true){

                    try {
                        if(stepOneCompleted) {
                            jFrame.getContentPane().remove(stepOnePanel);
                            jFrame.add(new Step2(jFrame, controller).getJPanel());
                            jFrame.revalidate();
                            jFrame.repaint();
                            break;

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        StepOnethread.setDaemon(true);
        StepOnethread.start();

        Thread StepTwothread = new Thread(){

            @Override
            public void run() {
                while (true){

                    try {
                        if(stepTwoCompleted) {

                            jFrame.getContentPane().removeAll();
                            jFrame.add(new Step3(jFrame, controller).getJPanel());
                            jFrame.revalidate();
                            jFrame.repaint();
                            break;

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        StepTwothread.setDaemon(true);
        StepTwothread.start();
        Thread StepThreeThread = new Thread(){

            @Override
            public void run() {
                while (true){

                    try {
                        if(stepThreeCompleted) {

                            jFrame.getContentPane().removeAll();
                            jFrame.add(new Step4(jFrame, controller).getJPanel());
                            jFrame.revalidate();
                            jFrame.repaint();
                            break;

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        StepThreeThread.setDaemon(true);
        StepThreeThread.start();
              Thread StepFourThread = new Thread(){

            @Override
            public void run() {
                while (true){

                    try {
                        if(stepFourCompleted) {
                            jFrame.getContentPane().removeAll();
                            jFrame.add(new Step5(jFrame, controller).getJPanel());
                            jFrame.revalidate();
                            jFrame.repaint();
                            break;

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        StepFourThread.setDaemon(true);
        StepFourThread.start();

        Thread StepFiveThread = new Thread(){

            @Override
            public void run() {
                while (true){

                    try {
                        if(stepFiveCompleted) {
                            jFrame.getContentPane().removeAll();
                            jFrame.add(new Step6(jFrame, controller).getJPanel());
                            jFrame.revalidate();
                            jFrame.repaint();
                            break;

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        StepFiveThread.setDaemon(true);
        StepFiveThread.start();


    }


        }



