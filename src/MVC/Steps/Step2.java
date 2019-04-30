package MVC.Steps;

import MVC.Controller;
import MVC.View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Step2 {
    public View view ;

    public Step2(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }
    private Controller controller;


    static ArrayList<String> paths = new ArrayList<>();
    static String path = path = "Путь к файлам со временем в сети указан :";
    static Color blue_color = new Color(30,116,189);
    static Color green_color = new Color(53,200,16);
    static Color red_color = Color.red;

    static Font font = new Font("Sylfaen",2,14);
    static Font font2 = new Font("Sylfaen",1,14);


    //  creating Container(JPanel) with components necessary for Step3 layout
    public JPanel getJPanel()  {



        //      creating jPanel and setting its background color to white and layout to null
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.white);
        jPanel.setLayout(null);

//      creating STEP2 image
        JLabel step_2 = new JLabel(new ImageIcon("E:\\reports\\images\\step2_img.png"));
        step_2.setBounds(280,20,170,60);
//      creating logo image
        JLabel logo = new JLabel(new ImageIcon("E:\\logo_small.png"));
        logo.setBounds(40,10,100,117);
//      creating browse loged time writing
        JLabel loged_time_img = new JLabel(new ImageIcon("E:\\reports\\images\\Aread_loged_time_img.png"));
        loged_time_img.setBounds(0,140,500,50);
//      creating loged time path string
        JLabel loged_time_path = new JLabel("Путь к файлам cо временем в сети не указан");
        loged_time_path.setBounds(35,175,500,50);
        loged_time_path.setFont(font);
        loged_time_path.setForeground(red_color);

//      creating check mark image
        JLabel check_img = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
        check_img.setBounds(570,180,50,44);
        check_img.setVisible(false);
//      creating calculate loged time writing
        JLabel calculate_loged_time_img = new JLabel(new ImageIcon("E:\\reports\\images\\read_loged_time_img.png"));
        calculate_loged_time_img.setBounds(0,230,500,50);
        calculate_loged_time_img.setVisible(false);
        //        creating operators calculate loged time string
        JLabel loged_time_calculate = new JLabel("Считывание времени в сети не произведено");
        loged_time_calculate.setBounds(35,265,500,50);
        loged_time_calculate.setFont(font);
        loged_time_calculate.setForeground(red_color);
        loged_time_calculate.setVisible(false);
//        creating check mark for operators calculated
        JLabel check_img2 = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
        check_img2.setVisible(false);
        check_img2.setBounds(570,265,50,44);
//        creating process to step 3 writing
        JLabel processToThirdStep_img = new JLabel(new ImageIcon("E:\\reports\\images\\if_correct_img.png"));
        processToThirdStep_img.setBounds(0,315,500,50);
        processToThirdStep_img.setVisible(false);
//      creating to step 3 button and adding actionListener
        JButton toThirdStep_button = new JButton("Далее");
        toThirdStep_button.setBounds(550,325,100,30);
        toThirdStep_button.setVisible(false);
        toThirdStep_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setStepTwoCompleted(true);
            }
        });
//        creating JTextArea, applying its size, position, border
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setFont(font2);
        jTextArea.setSize(200,200);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        jTextArea.setBorder(border);
        jTextArea.setBounds(25,400,730,230);
        jTextArea.setVisible(true);

//      creating calculate oparators button with action listener
        JButton calculate_button = new JButton("Считать");
        calculate_button.setBounds(550,238,100,30);
        calculate_button.setVisible(false);
        calculate_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jTextArea.setForeground(Color.BLACK);
                    jTextArea.setText("");
                    controller.set_operators_with_loged_time(paths);
                    check_img2.setVisible(true);
                    loged_time_calculate.setText("Считывание времени в сети произведено");
                    loged_time_calculate.setForeground(green_color);
                    jPanel.revalidate();
                    jTextArea.append(String.format("Загружены данные по операторам: %d.%nИз них %d операторов с нулевыми показателями: ",controller.getOperatorsSize(),
                            controller.getNotLogedOperators().length));
                    jTextArea.append(Arrays.toString( controller.getNotLogedOperators()));
                    toThirdStep_button.setVisible(true);
                    processToThirdStep_img.setVisible(true);
                } catch (Exception e1) {
                    loged_time_calculate.setText("Считывание времени в сети не произведено");
                    loged_time_calculate.setForeground(red_color);
                    jTextArea.setForeground(Color.red);
                    jTextArea.append(e1.getMessage());
                }
            }
        });
// creating reset button
        JButton reset_button = new JButton("Сбросить");
        reset_button.setBounds(670,150,100,30);
        reset_button.setVisible(false);
        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = "Путь к файлам со временем в сети указан :";
                paths.removeAll(paths);
                loged_time_path.setText("Путь к файлам cо временем в сети не указан");
                loged_time_path.setForeground(red_color);
                jTextArea.setText("");
                reset_button.setVisible(false);
                check_img.setVisible(false);
                check_img2.setVisible(false);
                calculate_button.setVisible(false);
                calculate_loged_time_img.setVisible(false);
                loged_time_calculate.setVisible(false);
                toThirdStep_button.setVisible(false);
                processToThirdStep_img.setVisible(false);
                jPanel.revalidate();

            }
        });

//      creating browse button and adding actionListener with file chooser
        JButton browse_button = new JButton("Обзор");
        browse_button.setBounds(550,150,100,30);
        browse_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setSize(500,500);
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jFileChooser.setMultiSelectionEnabled(true);

                 int ret = jFileChooser.showDialog(null, "Загрузить");

                if (ret == JFileChooser.APPROVE_OPTION) {
                   File[] files = jFileChooser.getSelectedFiles();

                    for (File file: files
                         ) {
                        paths.add(file.getAbsolutePath());
                        System.out.println(String.valueOf(paths.size()));
                        path += file.getName()+"  ";
                        if(paths.size()==1) {
                            jTextArea.setForeground(Color.BLACK);
                            jTextArea.setText("");
                            loged_time_path.setText(path + "(файлов " + paths.size() + ")");
                        }


                        if(paths.size()>1) {
                            jTextArea.setForeground(Color.BLACK);
                             jTextArea.setText(path + "(файлов " + paths.size() + ")");
                            loged_time_path.setText("Путь к фалам указан ("+paths.size()+")");
                        }
                    }


                    reset_button.setVisible(true);
                    loged_time_path.setForeground(green_color);
                    check_img.setVisible(true);
                    calculate_loged_time_img.setVisible(true);
                    loged_time_calculate.setVisible(true);
                    calculate_button.setVisible(true);




                    jPanel.revalidate();
                }
            }
        });



//adding all elements to jPanel
        jPanel.add(step_2);
        jPanel.add(browse_button);
        jPanel.add(check_img2);
        jPanel.add(calculate_button);
        jPanel.add(processToThirdStep_img);
        jPanel.add(toThirdStep_button);
        jPanel.add(logo);
        jPanel.add(check_img);
        jPanel.add(loged_time_calculate);
        jPanel.add(loged_time_img);
        jPanel.add(jTextArea);
        jPanel.add(calculate_loged_time_img);
        jPanel.add(loged_time_path);
        jPanel.add(reset_button);







        return jPanel;
    }






}




