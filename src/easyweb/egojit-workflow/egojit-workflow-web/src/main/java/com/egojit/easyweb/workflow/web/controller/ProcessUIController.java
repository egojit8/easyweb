package com.egojit.easyweb.workflow.web.controller;

import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.FileUtil;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.workflow.model.Process;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/workflow/processui")
@Api(value = "工作流管理", description = "工作流管理")
public class ProcessUIController extends BaseWebController {


    @Autowired
    FormService formService;

    @Autowired
    TaskService taskService;

    /*
     * 启动流程 启动流程，只考虑首次登录。 首次登录：启动工作流，并且更新/{processDefinitionId} @RequestMapping(value = "get-form/start/{processDefinitionId}")
     */
    @RequestMapping(value = "/start/{processDefinitionId}")
    public String start(@PathVariable("processDefinitionId") String processDefinitionId, HttpServletRequest request) throws Exception {

        try {
            // 定义map用于往工作流数据库中传值。
            Map<String, String> formProperties = new HashMap<String, String>();
            //启动流程-何静媛-2015年5月24日--processDefinitionId,
            ProcessInstance processInstance = formService
                    .submitStartFormData(processDefinitionId,
                            formProperties);
            // 返回到显示用户信息的controller
            _log.debug("开始流程", processInstance);
            return "redirect:/workflow/processui/get-form/task/" + processInstance.getId();

        } catch (Exception e) {
            throw e;
        } finally {
//            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * 读取Task的表单
     *
     * @RequestMapping(value = "get-form/task/{processDefinitionkey}")
     * @PathVariable("processDefinitionkey") String processDefinitionkey
     */
    @RequestMapping(value = "/get-form/task/{processInstanceId}")
    public String findTaskForm (
            @PathVariable("processInstanceId") String processInstanceId,
            HttpServletRequest request, Model model) throws Exception {
        // 获取当前登陆人信息。
        /* User user = UserUtil.getUserFromSession(request.getSession()); */
        TaskQuery taskQuery = taskService.createTaskQuery()
                .processInstanceId(processInstanceId).orderByProcessInstanceId().desc();
        List<Task> tasks = taskQuery.list();
        if (tasks.size() == 0) {
            return "/workflow/processui/finish";
        }
        Task task = tasks.get(0);
        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        System.out.println(renderedTaskForm.toString());
        model.addAttribute("renderedTaskForm",renderedTaskForm.toString() );//整个页面，参数已经赋值（整个页面是什么时候赋上值的？）
        model.addAttribute("taskId", task.getId());
        model.addAttribute("processInstanceId", processInstanceId);
        return "/workflow/processui/apply";
    }


    /**
     * 办理任务，提交task的并保存form
     */
    @RequestMapping(value = "/task/complete/{taskId}/{processInstanceId}")
    @SuppressWarnings("unchecked")
    @ResponseBody
    public BaseResult completeTask(@PathVariable("taskId") String taskId, @PathVariable("processInstanceId") String processInstanceId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Map<String, String> formProperties = new HashMap<String, String>();
        // 从request中读取参数然后转换
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();
            /*
             * 参数结构：fq_reason，用_分割 fp的意思是form paremeter 最后一个是属性名称
             */
            if (StringUtils.defaultString(key).startsWith("fp_")) {
                String[] paramSplit = key.split("_");
                formProperties.put(paramSplit[1], entry.getValue()[0]);
            }
        }
        _log.debug("start form parameters: {}", formProperties);
        try {
            formProperties.put("applyUserId","高露");
            formService.submitTaskFormData(taskId, formProperties);
        } finally {
//            identityService.setAuthenticatedUserId(null);
        }
        redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);

        return  new BaseResult(BaseResultCode.SUCCESS,"/workflow/processui/get-form/task/" + processInstanceId);

//        return "redirect:/workflow/processui/get-form/task/" + processInstanceId;

    }


}
