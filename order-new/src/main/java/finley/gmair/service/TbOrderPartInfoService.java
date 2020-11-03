package finley.gmair.service;

import finley.gmair.util.ResultData;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 14:08
 * @description: TbOrderPartInfoService
 */
public interface TbOrderPartInfoService {

    /**
     * 将用户上传的Excel文件保存至服务器
     * @param fileParentPath 保存文件的父路径
     * @param filename 文件名
     * @param content 文件内容
     * @return 保存是否成功
     */
    boolean saveOrderPartInfoExcel(String fileParentPath, String filename, byte[] content);

    /**
     * 从Excel表中读取所需的部分数据
     * @param filePath Excel表存储路径
     * @param password 读取Excel表时所需的密码
     * @return 所需数据集
     */
    ResultData getTbOrderPartInfo(String filePath, String password);
}
