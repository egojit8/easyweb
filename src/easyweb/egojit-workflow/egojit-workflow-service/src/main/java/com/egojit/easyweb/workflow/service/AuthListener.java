package com.egojit.easyweb.workflow.service;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

public class AuthListener implements TaskListener{
    @Override
    public void notify(DelegateTask delegateTask) {
        List<String> users=new ArrayList<>();
        users.add("egojit");
        users.add("gaolu");
        delegateTask.addCandidateUsers(users);
    }
}
