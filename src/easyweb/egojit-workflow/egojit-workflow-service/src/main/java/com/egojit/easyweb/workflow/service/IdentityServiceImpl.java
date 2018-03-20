package com.egojit.easyweb.workflow.service;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工作流用户认证实现
 */
@Transactional
@Service
public class IdentityServiceImpl implements IdentityService{
    @Override
    public User newUser(String s) {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public UserQuery createUserQuery() {
        return null;
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        return null;
    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public Group newGroup(String s) {
        return null;
    }

    @Override
    public GroupQuery createGroupQuery() {
        return null;
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery() {
        return null;
    }

    @Override
    public void saveGroup(Group group) {

    }

    @Override
    public void deleteGroup(String s) {

    }

    @Override
    public void createMembership(String s, String s1) {

    }

    @Override
    public void deleteMembership(String s, String s1) {

    }

    @Override
    public boolean checkPassword(String s, String s1) {
        return false;
    }

    @Override
    public void setAuthenticatedUserId(String s) {

    }

    @Override
    public void setUserPicture(String s, Picture picture) {

    }

    @Override
    public Picture getUserPicture(String s) {
        return null;
    }

    @Override
    public void setUserInfo(String s, String s1, String s2) {

    }

    @Override
    public String getUserInfo(String s, String s1) {
        return null;
    }

    @Override
    public List<String> getUserInfoKeys(String s) {
        return null;
    }

    @Override
    public void deleteUserInfo(String s, String s1) {

    }
}
