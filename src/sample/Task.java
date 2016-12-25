package sample;

import java.util.ArrayList;

/**
 * Created by vladimir on 17.12.16.
 */
public class Task {
    private boolean critical;
    private ArrayList<Integer> prevTasks;
    private ArrayList<Integer> nextTasks;
    private int execTime;
    private int startTime;

    {
        prevTasks = new ArrayList<>();
        nextTasks = new ArrayList<>();
    }

    public Task(int eTime){

        this.execTime = eTime;
        startTime = 0;
        critical = false;
    }

    public void AddPrevTask(Integer i){ prevTasks.add(i);}

    public void AddNextTask(Integer i){ nextTasks.add(i);}

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public ArrayList<Integer> getPrevTasks() {
        return prevTasks;
    }

    public void setPrevTasks(ArrayList<Integer> prevTask) {
        this.prevTasks = prevTask;
    }

    public ArrayList<Integer> getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(ArrayList<Integer> nextTask) {
        this.nextTasks = nextTask;
    }

    public int getExecTime() {
        return execTime;
    }

    public void setExecTime(int execTime) {
        this.execTime = execTime;
    }

    public boolean IsCritical(){return critical;}
}
