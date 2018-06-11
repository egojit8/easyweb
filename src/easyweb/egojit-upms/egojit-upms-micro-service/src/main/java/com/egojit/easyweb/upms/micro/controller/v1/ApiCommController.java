package com.egojit.easyweb.upms.micro.controller.v1;

import com.egojit.easyweb.common.base.BaseApiController;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.utils.FileUtil;
import com.egojit.easyweb.common.utils.FtpUtil;
import com.egojit.easyweb.common.utils.IdGen;
import com.egojit.easyweb.upms.common.config.FtpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Description：通用接口
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@RestController
@RequestMapping("/api/upms/v1/comm")
public class ApiCommController extends BaseApiController {

    @Autowired
    FtpConfig ftpConfig;

    /**
     * 上传资料
     *
     * @return
     */
    @PostMapping("/upload")
    public BaseResult upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        _log.debug("进入文件上传控制器");
        BaseResult result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败!");
        if (!file.isEmpty()) {
            String fileOldName = file.getOriginalFilename();
            //获取文件后缀名
            String prefix = fileOldName.substring(fileOldName.lastIndexOf(".") + 1);
            String fileName = IdGen.uuid() + "." + prefix;
            _log.debug("文件名："+fileName);
            try {
                _log.debug("开始上传文件");
                InputStream inputStream = file.getInputStream();
                boolean isOk = FtpUtil.uploadFile(ftpConfig.getHost(), ftpConfig.getUser(), ftpConfig.getPwd(), "upload", "/files", fileName, inputStream);
                if (isOk) {
                    result = new BaseResult(BaseResultCode.SUCCESS, "upload/files/" + fileName);
                }
//                FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            } catch (Exception e) {
                // TODO: handle exception
                _log.error("文件上传失败！"+e.getMessage());
                result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败");
            }
        } else {
            _log.error("不允许上传空文件");
            result = new BaseResult(BaseResultCode.EXCEPTION, "上传失败，因为文件是空的");
        }
        return result;
    }
}
