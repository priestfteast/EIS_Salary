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

public class Step4 {

        public View view ;

        public Step4(View view, Controller controller) {
            this.view = view;
            this.controller = controller;
        }
        public Controller controller;



    static ArrayList<String> paths = new ArrayList<>();
    static String path = "Путь к файлам со временем в сети указан :";
    static String path2;
    static Color blue_color = new Color(30,116,189);
    static Color green_color = new Color(53,200,16);
    static Color red_color = Color.red;

    static Font font = new Font("Sylfaen",2,14);
    static Font font2 = new Font("Sylfaen",1,14);


        // creating Container(JPanel) with components necessary for Step3 layout
        public JPanel getJPanel()  {

            //      creating jPanel and setting its background color to white and layout to null
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.white);
            jPanel.setLayout(null);

//      creating STEP4 image
            JLabel step_4 = new JLabel(new ImageIcon("E:\\reports\\images\\step4_img.png"));
            step_4.setBounds(240,20,280,60);
//      creating logo image
            JLabel logo = new JLabel(new ImageIcon("E:\\logo_small.png"));
            logo.setBounds(40,10,100,117);
//      creating browse calls quantity writing
            JLabel calls_quantity = new JLabel(new ImageIcon("E:\\reports\\images\\calls_quantity_img.png"));
            calls_quantity.setBounds(0,140,500,50);
//      creating calls quantity path string
            JLabel calls_quantity_path = new JLabel("Путь к файлам c количеством принятых звонков не указан");
            calls_quantity_path.setBounds(35,175,500,50);
            calls_quantity_path.setFont(font);
            calls_quantity_path.setForeground(red_color);

//      creating check mark image
            JLabel check_img = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
            check_img.setBounds(570,180,50,44);
            check_img.setVisible(false);
//      creating calculate calls quantity writing
            JLabel calculate_calls_quantity_img = new JLabel(new ImageIcon("E:\\reports\\images\\read_loged_time_img.png"));
            calculate_calls_quantity_img.setBounds(5,230,500,50);
            calculate_calls_quantity_img.setVisible(false);
            //        creating  calculate loged out time string
            JLabel calls_quantity_calculate = new JLabel("Считывание количества звонков не произведено");
            calls_quantity_calculate.setBounds(35,265,500,50);
            calls_quantity_calculate.setFont(font);
            calls_quantity_calculate.setForeground(red_color);
            calls_quantity_calculate.setVisible(false);
//        creating check mark for operators calculated
            JLabel check_img2 = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
            check_img2.setVisible(false);
            check_img2.setBounds(570,265,50,44);
//        creating process to step 4 writing
            JLabel processToFithStep_img = new JLabel(new ImageIcon("E:\\reports\\images\\if_correct_img.png"));
            processToFithStep_img.setBounds(0,315,500,50);
            processToFithStep_img.setVisible(false);
//      creating to step 5 button and adding actionListener
            JButton toFithStep_button = new JButton("Далее");
            toFithStep_button.setBounds(550,325,100,30);
            toFithStep_button.setVisible(false);
            toFithStep_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    view.setStepFourCompleted(true);
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
                        controller.set_operators_with_calls_quantity(paths);
                        check_img2.setVisible(true);
                        calls_quantity_calculate.setText("Считывание количества звонков произведено");
                        calls_quantity_calculate.setForeground(green_color);
                        jPanel.revalidate();
                        jTextArea.append(String.format("Загружены данные по операторам: %d.%nИз них %d операторов с нулевыми показателями: ",controller.getOperatorsSize(),
                                controller.getZeroCallsOperators().length));
                        jTextArea.append(Arrays.toString( controller.getZeroCallsOperators()));
                        toFithStep_button.setVisible(true);
                        processToFithStep_img.setVisible(true);
                        controller.set_categories_values();




                    } catch (Exception e1) {
                        calls_quantity_calculate.setText("Считывание количества звонков не произведено");
                        calls_quantity_calculate.setForeground(red_color);
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
                    calls_quantity_path.setText("Путь к файлам cо временем в сети не указан");
                    calls_quantity_path.setForeground(red_color);
                    jTextArea.setText("");
                    reset_button.setVisible(false);
                    check_img.setVisible(false);
                    check_img2.setVisible(false);
                    calculate_button.setVisible(false);
                    calculate_calls_quantity_img.setVisible(false);
                    toFithStep_button.setVisible(false);
                    calls_quantity_calculate.setVisible(false);
                    processToFithStep_img.setVisible(false);
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
                                calls_quantity_path.setText(path + "(файлов " + paths.size() + ")");

                            }


                            if(paths.size()>1) {
                                jTextArea.setForeground(Color.BLACK);
                                jTextArea.setText(path + "(файлов " + paths.size() + ")");
                                calls_quantity_path.setText("Путь к фалам указан ("+paths.size()+")");
                            }
                        }

                        reset_button.setVisible(true);
                        calls_quantity_path.setText(path);
                        calls_quantity_path.setForeground(green_color);
                        check_img.setVisible(true);
                        calculate_calls_quantity_img.setVisible(true);
                        calls_quantity_calculate.setVisible(true);
                        calculate_button.setVisible(true);
                        jPanel.revalidate();
                    }
                }
            });

//adding all elements to jPanel
            jPanel.add(step_4);
            jPanel.add(browse_button);
            jPanel.add(check_img2);
            jPanel.add(calculate_button);
            jPanel.add(processToFithStep_img);
            jPanel.add(toFithStep_button);
            jPanel.add(logo);
            jPanel.add(reset_button);
            jPanel.add(check_img);
            jPanel.add(calls_quantity_calculate);
            jPanel.add(calls_quantity);
            jPanel.add(jTextArea);
            jPanel.add(calculate_calls_quantity_img);
            jPanel.add(calls_quantity_path);






            return jPanel;
        }




    }


