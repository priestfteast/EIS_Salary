package MVC.Steps;

import MVC.Controller;
import MVC.View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class Step1 {

    public View view ;

    public Step1(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    public Controller controller;



    static String path ;
        static String path2;
        static Color blue_color = new Color(30,116,189);
        static Color green_color = new Color(53,200,16);
        static Color red_color = Color.red;

        static Font font = new Font("Sylfaen",2,14);
        static Font font2 = new Font("Sylfaen",1,14);


        //  creating Container(JPanel) with components necessary for Step1 layout
       public JPanel getJPanel(){

            //      creating jPanel and setting its background color to white and layout to null
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.white);
            jPanel.setLayout(null);




//      creating STEP1 image
            JLabel step_1 = new JLabel(new ImageIcon("E:\\reports\\images\\step1_img.png"));
            step_1.setBounds(280,20,170,60);
//      creating logo image
            JLabel logo = new JLabel(new ImageIcon("E:\\logo_small.png"));
            logo.setBounds(40,10,100,117);
//      creating browse operators writing
            JLabel browse_operators_img = new JLabel(new ImageIcon("E:\\reports\\images\\Operators_img.png"));
            browse_operators_img.setBounds(0,140,500,50);

//      creating operators path string
            JLabel operators_path = new JLabel("Путь к файлу c операторами не указан");
            operators_path.setBounds(35,175,500,50);
            operators_path.setFont(font);
            operators_path.setForeground(red_color);

//      creating check mark image
            JLabel check_img = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
            check_img.setBounds(570,180,50,44);
            check_img.setVisible(false);
//      creating calculate operators writing
            JLabel calculate_operators_img = new JLabel(new ImageIcon("E:\\reports\\images\\read_operators_img.png"));
            calculate_operators_img.setBounds(5,230,500,50);
            calculate_operators_img.setVisible(false);
            //        creating operators calculate info string
            JLabel operators_calculate = new JLabel("Считывание операторов не произведено");
            operators_calculate.setBounds(35,265,500,50);
            operators_calculate.setFont(font);
            operators_calculate.setForeground(red_color);
            operators_calculate.setVisible(false);
//        creating check mark for operators calculated
            JLabel check_img2 = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
            check_img2.setVisible(false);
            check_img2.setBounds(570,265,50,44);
//        creating process to step 2 writing
            JLabel processToSecondStep_img = new JLabel(new ImageIcon("E:\\reports\\images\\if_correct_img.png"));
            processToSecondStep_img.setBounds(0,315,500,50);
            processToSecondStep_img.setVisible(false);
//      creating to step 2 button and adding actionListener
            JButton toSecondStep_button = new JButton("Далее");
            toSecondStep_button.setBounds(550,325,100,30);
            toSecondStep_button.setVisible(false);
            toSecondStep_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   view.setStepOneCompleted(true);
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
                        controller.set_raw_operators(path2);
                        check_img2.setVisible(true);
                        operators_calculate.setText("Считывание операторов произведено");
                        operators_calculate.setForeground(green_color);
                        jPanel.revalidate();
                        jTextArea.append(String.format("Загружено операторов: %d.%nИз них %d ночников: ",controller.getOperatorsSize(),
                                controller.getNightOperators().length));
                        jTextArea.append(Arrays.toString(controller.getNightOperators()));
                        toSecondStep_button.setVisible(true);
                        processToSecondStep_img.setVisible(true);
                    } catch (Exception e1) {
                        operators_calculate.setText("Считывание операторов не произведено");
                        operators_calculate.setForeground(red_color);
                        jTextArea.setForeground(Color.red);
                        jTextArea.append(e1.getMessage());

                    }


                }
            });

//           creating reset button and adding actionListener
           JButton reset_button = new JButton("Сброс");
           reset_button.setBounds(670,150,100,30);
           reset_button.setVisible(false);
           reset_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   operators_path.setText( "Путь к файлу c операторами не указан");
                   operators_path.setForeground(red_color);
                   check_img.setVisible(false);
                   check_img2.setVisible(false);
                   calculate_operators_img.setVisible(false);
                   operators_calculate.setVisible(false);
                   calculate_button.setVisible(false);
                   reset_button.setVisible(false);
                   jTextArea.setText("");
                   operators_calculate.setVisible(false);
                   toSecondStep_button.setVisible(false);
                   processToSecondStep_img.setVisible(false);
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
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int ret = jFileChooser.showDialog(null, "Загрузить");

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = jFileChooser.getSelectedFile();
                        path2 = file.getAbsolutePath();
                        path = "Путь к списку операторов указан: ..."+file.getName();
                        jTextArea.setForeground(Color.BLACK);
                        jTextArea.setText("");


                        operators_path.setText(path);
                        operators_path.setForeground(green_color);
                        check_img.setVisible(true);
                        calculate_operators_img.setVisible(true);
                        operators_calculate.setVisible(true);
                        calculate_button.setVisible(true);
                        reset_button.setVisible(true);
                        jPanel.revalidate();
                    }
                }
            });



//adding all elements to jPanel
            jPanel.add(step_1);
            jPanel.add(browse_button);
            jPanel.add(check_img2);
            jPanel.add(calculate_button);
            jPanel.add(processToSecondStep_img);
            jPanel.add(reset_button);
            jPanel.add(toSecondStep_button);
            jPanel.add(logo);
            jPanel.add(check_img);
            jPanel.add(operators_calculate);
            jPanel.add(browse_operators_img);
            jPanel.add(jTextArea);
            jPanel.add(calculate_operators_img);
            jPanel.add(operators_path);






            return jPanel;
        }




    }


