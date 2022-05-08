package com.first.ateez.Models;

import java.io.Serializable;
import java.util.HashMap;

public class Task implements Serializable {
    int taskId;
    String taskTitle,date,taskDescription,firstAlarmTime,secondAlarmTime,lastAlarm,event;
    boolean isComplete;

    public Task() {

    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getFirstAlarmTime() {
        return firstAlarmTime;
    }

    public void setFirstAlarmTime(String firstAlarmTime) {
        this.firstAlarmTime = firstAlarmTime;
    }

    public String getSecondAlarmTime() {
        return secondAlarmTime;
    }

    public void setSecondAlarmTime(String secondAlarmTime) {
        this.secondAlarmTime = secondAlarmTime;
    }

    public String getLastAlarm() {
        return lastAlarm;
    }

    public void setLastAlarm(String lastAlarm) {
        this.lastAlarm = lastAlarm;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public HashMap<String,String> toFirebaseObject(){
        HashMap<String,String> task= new HashMap<String,String>();
        task.put("taskTitle",taskTitle);
        task.put("date",date);
        task.put("taskDescription",taskDescription);
        task.put("event",event);
        task.put("Task_Id",String.valueOf(taskId));

        return task;
    }


}
