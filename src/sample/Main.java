package sample;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        String filePath = new String("src/sample/Input.txt");

        TaskManager taskManager = new TaskManager();

        taskManager.GetTasks(filePath);

        taskManager.PrintCriticalPath();

        /*
        JFrame frame = new JFrame();

        //GraficComponent grComp = new GraficComponent();

        SchedulerForm scForm = new SchedulerForm(taskManager);
        //scForm.getSchemePanel().add(grComp);

        frame.setContentPane(scForm);

        //frame.setLayout(new BorderLayout(1,1));


        frame.setMinimumSize(new Dimension(600, 600));

        frame.setLocationRelativeTo(null);//отцентрировать окно

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Scheduler");
        frame.setVisible(true);
        */
    }
}

