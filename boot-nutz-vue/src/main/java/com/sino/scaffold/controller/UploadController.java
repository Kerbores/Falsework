package com.sino.scaffold.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sino.scaffold.config.QiniuAutoConfiguration.QiniuUploader;
import com.sino.scaffold.config.QiniuAutoConfiguration.R;
import com.sino.scaffold.utils.Result;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("upload")
public class UploadController {
	@Autowired
	QiniuUploader qiniuUploader;

	@PostMapping("test")
	public Result test(MultipartFile file) throws IOException {
		R r = qiniuUploader.upload(file.getInputStream());
		return r == null ? Result.fail("上传失败!") : Result.success().addData("url", r.getUrl());
	}
}
