package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        //launch(args);

        TaskManager taskManager = new TaskManager();

        GetTasks(taskManager);

        PrintCriticalPath(taskManager);

    }

    static public void PrintCriticalPath(TaskManager taskManager){
        ArrayList<Integer> crPath = taskManager.CriticalPath();
        Integer crTime = taskManager.GetCriticalPathTime();

        System.out.println("Project duration: " + crTime);

        if(!crPath.isEmpty()) {
            System.out.print("Critical path: \n { ");
            for (int i = 0; i < crPath.size() - 1; i++)
                System.out.print(crPath.get(i) + ", ");
            System.out.println(crPath.get(crPath.size() - 1) + " }");
        }else{
            System.out.println("Critical path is empty.");
        }
    }

    static public void GetTasks(TaskManager taskManager) throws IOException {

        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String cmd = new String();
        try(BufferedReader reader = new BufferedReader(new FileReader("src/sample/Input.txt"))) {
            System.out.println("Input tasks...");
            System.out.println("Systax is: sample.Task({afterTasks},{beforeTasks}, duration)");
            cmd = reader.readLine();
            while (!cmd.equals("end")) {
                Pattern p = Pattern.compile("Task\\(\\{(.*)\\},\\{(.*)\\},([1-9][0-9]*)\\)");
                Matcher m = p.matcher(cmd);
                if (m.find()) {
                    //System.out.println("{1}:" + m.group(1) + "{2}:" + m.group(2) + "{3}:" + m.group(3));
                    Task task = CreateTask(m.group(1), m.group(2), m.group(3));
                    taskManager.AddTask(task);
                    System.out.println("[" + (taskManager.GetTaskCount()-1) + "] " +
                            cmd + " was added.");
                } else
                    System.out.println("Unknown command. Systax is: sample.Task({afterTasks},{beforeTasks}, duration)");
                cmd = reader.readLine();
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    static public Task CreateTask(String prevVec, String nextVec, String dur){

        Integer duration = Integer.parseInt(dur);

        Task task = new Task(duration);


        if(!prevVec.equals("")) {
            String[] prevs = prevVec.split(",");
            for(String t : prevs)
                task.AddPrevTask(Integer.parseInt(t));
        }



        if(!nextVec.equals("")){
            String[] nexts = nextVec.split(",");
            for(String t : nexts)
                task.AddNextTask(Integer.parseInt(t));
        }

        return task;
    }
}
