package sample;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by vladimir on 18.12.16.
 */
public class TaskManager {
    private int taskCount;
    private ArrayList<Integer> begTaskList;
    private ArrayList<Integer> endTaskList;
    private ArrayList<Task> taskList;
    private ArrayList<Integer> criticalPath;
    private int criticalPathTime;


    {
        taskList = new ArrayList<Task>();
        begTaskList = new ArrayList<Integer>();
        endTaskList = new ArrayList<Integer>();
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
        if(nextTask.getPrevTasks().isEmpty())
            begTaskList.add(taskList.size()-1);
        else if(nextTask.getNextTasks().isEmpty())
            endTaskList.add(taskList.size()-1);
        taskCount++;
    }

    public ArrayList<Integer> CriticalPath(){
        criticalPathTime = 0;
        ArrayDeque<Integer> deque = new ArrayDeque<Integer>(begTaskList);
        System.out.println("Dq: " + deque.size());
        while(!deque.isEmpty()){
            Task curTask = taskList.get(deque.pop());
            int curEnd = curTask.getStartTime() + curTask.getExecTime();
            for(Integer sub : curTask.getNextTasks()) {
                int subStart = taskList.get(sub).getStartTime();
                if(subStart < curEnd || subStart == 0)
                    taskList.get(sub).setStartTime(curEnd);
                deque.addLast(sub);
            }
        }

        Integer longerTask = endTaskList.get(0);
        Integer lduration = taskList.get(longerTask).getExecTime();
        for(Integer i : endTaskList ){
            Integer cduration = taskList.get(i).getExecTime();
            if(cduration > lduration)
            {
                longerTask = i;
                lduration = cduration;
            }
        }
        criticalPath.add(longerTask);
        criticalPathTime += lduration;

        deque.addAll(endTaskList);
        System.out.println("Dq: " + deque.size());
        while(!deque.isEmpty()){
            Task curTask = taskList.get(deque.pop());
            int curStart = curTask.getStartTime();
            for(Integer sub : curTask.getPrevTasks()) {
                int subExec = taskList.get(sub).getExecTime();
                int subStart = curStart - subExec;
                if(subStart == taskList.get(sub).getStartTime()) {
                    criticalPath.add(sub);
                    criticalPathTime += subExec;
                    deque.addLast(sub);
                }
            }
        }
        return criticalPath;
    }


}
