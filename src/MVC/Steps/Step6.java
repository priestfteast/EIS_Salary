package MVC.Steps;

import MVC.Controller;
import MVC.View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Step6 {
    public View view ;

    public Step6(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }
    public Controller controller;






    static Color green_color = new Color(53,200,16);
    static Color blue_color = new Color(30,116,189);
    static Color red_color = Color.red;
    static String save_path = "";

    static Font font = new Font("Sylfaen",2,14);
    static Font font2 = new Font("Sylfaen",1,14);
    static Font font3 = new Font("Sylfaen",1,16);



    // creating Container(JPanel) with components necessary for Step6 layout
    public JPanel getJPanel()  {



        //      creating jPanel and setting its background color to white and layout to null
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.white);
        jPanel.setLayout(null);


//      creating STEP6 image
        JLabel step_6 = new JLabel(new ImageIcon("E:\\reports\\images\\step6_img.png"));
        step_6.setBounds(240,20,280,60);
//      creating logo image
        JLabel logo = new JLabel(new ImageIcon("E:\\logo_small.png"));
        logo.setBounds(40,10,100,117);
//      creating specify values img
        JLabel specify_values = new JLabel(new ImageIcon("E:\\reports\\images\\specify_values.png"));
        specify_values.setBounds(0,140,500,50);
//      creating specify values  string
        JLabel values_string = new JLabel("Значения для расчетов не сохранены");
        values_string.setBounds(35,175,500,50);
        values_string.setFont(font);
        values_string.setForeground(red_color);

//      creating check mark image
        JLabel check_img = new JLabel(new ImageIcon("E:\\reports\\images\\check.png"));
        check_img.setBounds(570,180,50,44);
        check_img.setVisible(false);
//      creating calculate calls duration writing
        JLabel save_img = new JLabel(new ImageIcon("E:\\reports\\images\\save_path.png"));
        save_img.setBounds(0,400,500,50);
        save_img.setVisible(false);

        //        creating
        JLabel save_writing_ = new JLabel("Зарплатный проект не сохранен");
        save_writing_.setBounds(35,435,500,50);
        save_writing_.setFont(font);
        save_writing_.setForeground(red_color);
        save_writing_.setVisible(false);

        JButton save_values_button = new JButton("Сохранить значения");

//        creating JTextArea, applying its size, position, border
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setFont(font2);
        jTextArea.setSize(200,100);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        jTextArea.setBorder(border);
        jTextArea.setBounds(25,500,730,130);
        jTextArea.setVisible(true);


//      creating browse button and adding actionListener with file chooser
        JButton browse_button = new JButton("Обзор");
        browse_button.setBounds(605,410,100,30);
        browse_button.setVisible(false);
        browse_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setSize(500,500);
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


                int ret = jFileChooser.showDialog(null, "Загрузить");

                if (ret == JFileChooser.APPROVE_OPTION) {
                    save_path = jFileChooser.getSelectedFile().getAbsolutePath();
                    controller.setPath(save_path);
                    System.out.println(controller.getPath());

                    controller.write_file();
                }
                save_writing_.setForeground(green_color);
                save_writing_.setText("Зарплатный проект сохранен в папке: "+save_path);
            }
        });

        // creating reset button
        JButton reset_button = new JButton("Сбросить");
        reset_button.setBounds(603,150,100,30);
        reset_button.setVisible(false);
        reset_button.addActionListener(new ActionListener() {
                                           @Override
                                           public void actionPerformed(ActionEvent e) {

                                               save_values_button.setVisible(true);
                                               reset_button.setVisible(false);
                                               save_img.setVisible(false);
                                               values_string.setForeground(red_color);
                                               values_string.setText("Значения для расчетов не сохранены");
                                               save_writing_.setVisible(false);
                                               browse_button.setVisible(false);
                                               save_writing_.setForeground(red_color);
                                               save_writing_.setText("Зарплатный проект не сохранен");
                                           }
                                       }
        );




//        creating category A writing
        JLabel Cat_A = createJlabel("Кат. А");
        Cat_A.setBounds(35,215,80,50);


//        creating category A jText
        JTextArea Cat_A_text = createJtext(40,22);
        Cat_A_text.setBounds(90,228,40,22);

        Cat_A_text.setText(controller.getCategories_values()[0]);



        //        creating category B writing
        JLabel Cat_B = createJlabel("Кат. B");
        Cat_B.setBounds(135,215,80,50);

//        creating category B jText
        JTextArea Cat_B_text = createJtext(40,22);
        Cat_B_text.setBounds(188,228,40,22);
        Cat_B_text.setText(controller.getCategories_values()[1]);



        //        creating category C writing
        JLabel Cat_C = createJlabel("Кат. С");
        Cat_C.setBounds(235,215,100,50);

//        creating category С jText
        JTextArea Cat_C_text = createJtext(50,22);
        Cat_C_text.setBounds(290,228,50,22);
        Cat_C_text.setText(controller.getCategories_values()[2]);


        //        creating talk duration writing
        JLabel talk_duration = createJlabel("Время консультации (мин.:сек.)");
        talk_duration.setBounds(390,215,250,50);

//        creating talk duration jText
        JTextArea talk_duration_text = createJtext(50,22);
        talk_duration_text.setBounds(652,228,50,22);
        talk_duration_text.setText("5:10");

        //        creating not ready 9 hours
        JLabel not_ready_9 = createJlabel("Не готов смена 9 ч. (мин.:сек)");
        not_ready_9.setBounds(35,270,250,50);

//        creating not ready 9 hours jText
        JTextArea not_ready_9_text = createJtext(50,22);
        not_ready_9_text.setBounds(290,282,50,22);
        not_ready_9_text.setText("90:30");

        //        creating not ready 12 hours
        JLabel not_ready_12 = createJlabel("Не готов смена 12 ч. (мин.:сек)");
        not_ready_12.setBounds(390,270,250,50);

//        creating not ready 12 hours jText
        JTextArea not_ready_12_text = createJtext(50,22);
        not_ready_12_text.setBounds(652,282,50,22);
        not_ready_12_text.setText("102:30");
        //        creating loged time 9 hours
        JLabel loged_time_9 = createJlabel("В сети для смены 9 ч. (мин.:сек)");
        loged_time_9.setBounds(35,325,250,50);

//        creating loged time 9 hours jText
        JTextArea loged_time_9_text = createJtext(50,22);
        loged_time_9_text.setBounds(290,337,50,22);
        loged_time_9_text.setText("5:10");


        //        creating loged time 12 hours
        JLabel loged_time_12 = createJlabel("В сети для смены 12 ч. (мин.:сек)");
        loged_time_12.setBounds(390,325,250,50);

//        creating loged time 12 hours jText
        JTextArea loged_time_12_text = createJtext(50,22);
        loged_time_12_text.setBounds(652,337,50,22);
        loged_time_12_text.setText("5:10");

        // creating save_values_button


        save_values_button.setBounds(542,150,160,30);
        save_values_button.setVisible(true);
        save_values_button.addActionListener(new ActionListener() {
                                                 @Override
                                                 public void actionPerformed(ActionEvent e) {

                                                     save_values_button.setVisible(false);
                                                     reset_button.setVisible(true);
                                                     save_img.setVisible(true);
                                                     values_string.setForeground(green_color);
                                                     values_string.setText("Значения для расчетов успешно сохранены");
                                                     save_writing_.setVisible(true);
                                                     browse_button.setVisible(true);
                                                     controller.setArgs(new String[]{Cat_A_text.getText(),Cat_B_text.getText(),
                                                     Cat_C_text.getText(), talk_duration_text.getText(), not_ready_9_text.getText(),
                                                     not_ready_12_text.getText(),loged_time_9_text.getText(), loged_time_12_text.getText()});
                                                     System.out.println(Arrays.asList(controller.getArgs()));
                                                 }
                                             }
        );
//adding all elements to jPanel
        jPanel.add(step_6);
        jPanel.add(Cat_A);
        jPanel.add(Cat_A_text);
        jPanel.add(Cat_B);
        jPanel.add(Cat_B_text);
        jPanel.add(Cat_C);
        jPanel.add(Cat_C_text);
        jPanel.add(talk_duration);
        jPanel.add(talk_duration_text);
        jPanel.add(not_ready_9);
        jPanel.add(not_ready_9_text);
        jPanel.add(not_ready_12);
        jPanel.add(not_ready_12_text);
        jPanel.add(loged_time_9);
        jPanel.add(loged_time_9_text);
        jPanel.add(loged_time_12);
        jPanel.add(loged_time_12_text);
        jPanel.add(browse_button);
        jPanel.add(logo);
        jPanel.add(reset_button);
        jPanel.add(check_img);
        jPanel.add(save_writing_);
        jPanel.add(specify_values);
        jPanel.add(jTextArea);
        jPanel.add(save_img);
        jPanel.add(values_string);
        jPanel.add(save_values_button);
        jPanel.add(save_img);







        return jPanel;
    }

    public static JTextArea createJtext( int width, int height){
        JTextArea jtext = new JTextArea();
        jtext.setLineWrap(true);
        jtext.setWrapStyleWord(true);
        jtext.setFont(font2);
        jtext.setSize(width,height);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        jtext.setBorder(border);
        jtext.setVisible(true);
        return jtext;
    }

    public static JLabel createJlabel( String name) {
        JLabel label = new JLabel(name);
        label.setFont(font3);
        label.setForeground(blue_color);
        return label;
    }



}
