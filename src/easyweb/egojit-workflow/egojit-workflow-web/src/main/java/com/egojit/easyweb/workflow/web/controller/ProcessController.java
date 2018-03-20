package com.egojit.easyweb.workflow.web.controller;


import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.FileUtil;
import com.egojit.easyweb.workflow.model.Process;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/admin/workflow/process")
@Api(value = "工作流管理", description = "工作流管理")
public class ProcessController extends BaseWebController {


    @Autowired
    RepositoryService repositoryService;

    @RequestMapping("/index")
    @ApiOperation(value = "工作流管理")
    public String index() {
        return "/workflow/process/index";
    }


    @ResponseBody
    @PostMapping("/index")
    @ApiOperation(value = "工作流管理")
    public Object index(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
        Page<Process> pg = new Page<Process>(request, response);
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().listPage(pg.getPage() - 1, pg.getPageSize());
        long count = repositoryService.createProcessDefinitionQuery().count();
        List<Process> list = new ArrayList<Process>();
        if (definitions != null) {
            for (ProcessDefinition item : definitions) {
                Process p = new Process();
                BeanUtils.copyProperties(p, item);
                list.add(p);
            }
        }
        pg.setList(list);
        pg.setRecords(count);
        return pg;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "流程添加界面")
    public String add() {
        return "/workflow/process/edit";
    }

    @ApiOperation(value = "流程添加接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(HttpServletRequest request, Process process) {
        BaseResult result = new BaseResult(BaseResultCode.SUCCESS, "部署成功！");
        try {
            String filePath = request.getSession().getServletContext().getRealPath("process/");
            File file = new File(filePath + process.getResourceName());
            //部署
            InputStream inputStream = new FileInputStream(file);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream); // 实例化zip输入流对象
//            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//            RepositoryService repositoryService = processEngine.getRepositoryService();
            repositoryService.createDeployment()
                    .name(process.getName())
                    .category(process.getCategory())
                    .key(process.getKey())
                    .addZipInputStream(zipInputStream).deploy();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = new BaseResult(BaseResultCode.EXCEPTION, "部署失败");
        } catch (IOException e) {
            e.printStackTrace();
            result = new BaseResult(BaseResultCode.EXCEPTION, "部署失败");
        }

        return result;
    }

    @ApiOperation(value = "流程添加接口")
    @PostMapping("/processfile")
    @ResponseBody
    public BaseResult prcessFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        BaseResult result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败，因为文件是空的");
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = request.getSession().getServletContext().getRealPath("process/");
            try {
                FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                result = new BaseResult(BaseResultCode.SUCCESS, fileName);
            } catch (Exception e) {
                // TODO: handle exception
                result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败");
            }
        } else {
            result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败，因为文件是空的");
        }
        return result;
    }
//    @ResponseBody
//    @PostMapping("/delete")
//    @ApiOperation(value = "用户管理接口")
//    public BaseResult delete(String ids){
//        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
//        List<String> idList= JSON.parseArray(ids,String.class);
//        int count= userService.deleteByIds(idList);
//        _log.info("删除了："+count+"数据");
//        return result;
//    }


}
