package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vladimir on 18.12.16.
 */
public class TaskManager {
    private int taskCount;
   // private ArrayList<Integer> begTaskList;
   // private ArrayList<Integer> endTaskList;
    private ArrayList<Task> taskList;
    private ArrayList<Integer> criticalPath;
    private int criticalPathTime;


    {
        taskList = new ArrayList<Task>();
       // begTaskList = new ArrayList<Integer>();
       // endTaskList = new ArrayList<Integer>();
        criticalPath = new ArrayList<Integer>();
    }

    public TaskManager(Task _firstTask){
        taskList.add(_firstTask);
        criticalPathTime = _firstTask.getExecTime();
        taskCount = 1;
    }
    public TaskManager(){
        criticalPathTime = 0;
        taskCount = 0;
    }



    public int GetCriticalPathTime() {
        return criticalPathTime;
    }

    public int GetTaskCount() {
        return taskCount;
    }

    public void AddTask(Task nextTask){
        taskList.add(nextTask);
//        if(nextTask.getPrevTasks().isEmpty())
//            begTaskList.add(taskList.size()-1);
//        else if(nextTask.getNextTasks().isEmpty())
//            endTaskList.add(taskList.size()-1);
        taskCount++;
    }

    public ArrayList<Integer> GetBegArray(){
        ArrayList<Integer> begTasks = new ArrayList<>();
        for (Integer i = 0; i < taskList.size(); i++){
            if (taskList.get(i).getPrevTasks().isEmpty()) {
                begTasks.add(i);
            }
        }
        return begTasks;
    }

    public ArrayList<Integer> GetEndArray(){
        ArrayList<Integer> endTasks = new ArrayList<>();
        for (Integer i = 0; i < taskList.size(); i++){
            if (taskList.get(i).getNextTasks().isEmpty()) {
                endTasks.add(i);
            }
        }
        return endTasks;
    }

    public ArrayList<Integer> CriticalPath(){
        criticalPathTime = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>( new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return taskList.get(o1).getStartTime() - taskList.get(o2).getStartTime();
            }
        });
        queue.addAll(GetBegArray());
        System.out.println("Dq: " + queue.size());

        //Прямой ход
        while(!queue.isEmpty()){
            Task curTask = taskList.get(queue.poll());
            int curEnd = curTask.getStartTime() + curTask.getExecTime();
            for(Integer sub : curTask.getNextTasks()) {
                int subStart = taskList.get(sub).getStartTime();
                if(subStart < curEnd || subStart == 0)
                    taskList.get(sub).setStartTime(curEnd);
                queue.add(sub);
            }
        }

        //Обратный ход, начиная с самой длинной вершины
        ArrayList<Integer> endTaskList = GetEndArray();
        Integer lastTask = endTaskList.get(0);
        Integer endTime = taskList.get(lastTask).getExecTime() + taskList.get(lastTask).getStartTime();
        for(Integer i : endTaskList ){
            Integer cEndTime = taskList.get(i).getExecTime() + taskList.get(i).getStartTime();
            if(cEndTime > endTime)
            {
                lastTask = i;
                endTime = cEndTime;
            }
        }

        for(Integer i : endTaskList)
            taskList.get(i).setEndTime(endTime);

        criticalPath.add(lastTask);
        criticalPathTime += taskList.get(lastTask).getExecTime();

        queue.addAll(endTaskList);
        System.out.println("DqRev: " + queue.size());
        while(!queue.isEmpty()){
            Task curTask = taskList.get(queue.poll());
            int curStart = curTask.getStartTime();
            for(Integer sub : curTask.getPrevTasks()) {
                int subExec = taskList.get(sub).getExecTime();
                int newSubStart = curStart - subExec;

                if((newSubStart == taskList.get(sub).getStartTime()) && !taskList.get(sub).isCritical()) {
                    criticalPath.add(sub);
                    criticalPathTime += subExec;
                    taskList.get(sub).setCritical(true);
                    taskList.get(sub).setReserve(0);
                }else if (!taskList.get(sub).isCritical()){
                    int delta = newSubStart - taskList.get(sub).getStartTime();
                    taskList.get(sub).setReserve(delta);
                    taskList.get(sub).setEndTime(curStart);
                }
                queue.add(sub);
            }
        }
        return criticalPath;
    }

    public void PrintCriticalPath(){
        if(taskCount == 0){
            System.out.println("No tasks in the list.");
            return;
        }

        ArrayList<Integer> crPath = CriticalPath();
        Integer crTime = GetCriticalPathTime();

        System.out.println("Project duration: " + crTime);

       // if(!crPath.isEmpty()) {
            System.out.print("Critical path: \n { ");
            for (int i = 0; i < crPath.size() - 1; i++)
                System.out.print(crPath.get(i) + ", ");
            System.out.println(crPath.get(crPath.size() - 1) + " }");
       // }else{
        //    System.out.println("Critical path is empty.");
       // }
    }

    public Task CreateTask(String prevVec, String dur){

        Integer duration = Integer.parseInt(dur);

        Task task = new Task(duration);


        if(!prevVec.equals("")) {
            String[] prevs = prevVec.split(",");
            for(String t : prevs){
                Integer taskNumb = Integer.parseInt(t);
                task.AddPrevTask(taskNumb);
                taskList.get(taskNumb).AddNextTask(taskCount);
            }
        }


//Deprecated
//        if(!nextVec.equals("")){
//            String[] nexts = nextVec.split(",");
//            for(String t : nexts)
//                task.AddNextTask(Integer.parseInt(t));
//        }

        return task;
    }

     public void GetTasks(String filePath) throws IOException {

        String cmd;
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("Input tasks...");
            //System.out.println("Systax is: sample.Task({afterTasks},{beforeTasks}, duration)");
            cmd = reader.readLine();
            while (!cmd.equals("end")) {
                Pattern p = Pattern.compile("Task\\(\\{((?:,?\\s*[0-9]+)*)\\},\\s*([1-9][0-9]*)\\)");
                Matcher m = p.matcher(cmd);
                //System.out.println(m.groupCount());
                if ( m.find() ) {
                    //System.out.println("{1}:" + m.group(1) + "{2}:" + m.group(2) + "{3}:" + m.group(3));
                    Task task = CreateTask(m.group(1), m.group(2));
                    AddTask(task);
                    System.out.println("[" + (GetTaskCount()-1) + "] " +
                            cmd + " was added.");
                } else
                    System.out.println("Unknown command. Systax is: sample.Task({afterTasks}, duration)");
                cmd = reader.readLine();
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
