package com.weaver.sage;

import com.api.doc.detail.service.DocSaveService;
import com.gbasedbt.base64.BASE64Decoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.DocViewer;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.file.ImageFileManager;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.resource.ResourceComInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DocImageUtil extends BaseBean {

    static private final Log log = LogFactory.getLog(DocImageUtil.class);


    /**
     * 创建文档
     *
     * @param title      标题
     * @param Content    内容
     * @param userID     创建用户id
     * @param categoryid 子目录id
     * @param code       文档编号
     * @return
     */
    public static String createDoc(String title, String Content, String userID, String categoryid, String code) {

        String docids = "0"; // 文件id

        int filesize = 0; // 文件大小

        String locapath = "";

        DocServiceImpl serviceImpl = new DocServiceImpl(); // 创建文档服务

        int maincategoryid = 0; // 文档主目录
        int subcategoryid = 0; // 文档分目录
        int seccategoryid = 0; // 文档子目录

        seccategoryid = Util.getIntValue(categoryid, 0);

        try {

            DocInfo doc = new DocInfo();

            String DocSubject = title;
            String docContent = Content;

            doc.setId(0);
            doc.setDocSubject(DocSubject);
            doc.setDoccontent(docContent);

            doc.setMaincategory(maincategoryid);
            doc.setSubcategory(subcategoryid);
            doc.setSeccategory(seccategoryid);
            // doc.setDocType(2);
            doc.setIsreply("0");
            doc.setDocCode(code);

            // writeLog("用户封装["+userID+"]");
            User user = getUser(Util.getIntValue(userID, 0));
            // writeLog("用户封装end["+userID+"]");

            docids = serviceImpl.createDocByUser(doc, user) + "";
            // docids = serviceImpl.createDoc(doc, session)+"";
            // writeLog("文档创建id"+docids);
            if (Util.getIntValue(docids, 0) <= 0) {
                docids = "0";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("文档创建异常!");
            log.error(e.toString());
            log.error("文档创建异常!end");
            return "0";
        }
        // bean.writeLog("文档id:"+docids);

        return docids;
    }

    /**
     * 创建文档
     *
     * @param title         标题
     * @param Content       内容
     * @param userID        创建用户id
     * @param categoryid    子目录id
     * @param code          文档编号
     * @param imageFileName 附件名称
     * @param fileRealPath  附件文件路径
     * @return
     */
    public String createDocNewAtta(String title, String Content, String userID, String categoryid, String code,
                                   String imageFileName, String fileRealPath) {

        String docids = "0"; // 文件id

        int filesize = 0; // 文件大小

        String locapath = "";

        DocServiceImpl serviceImpl = new DocServiceImpl(); // 创建文档服务

        int maincategoryid = 0; // 文档主目录
        int subcategoryid = 0; // 文档分目录
        int seccategoryid = 0; // 文档子目录

        seccategoryid = Util.getIntValue(categoryid, 0);

        try {

            DocInfo doc = new DocInfo();

            String DocSubject = title;
            String docContent = Content;

            DocAttachment da = new DocAttachment();
            da.setDocid(0);
            da.setImagefileid(0);
            // da.setFilecontent(Base64.encode(content));
            da.setFilename("" + imageFileName);
            da.setFilerealpath(fileRealPath);
            da.setIszip(1);
            da.setImagefilesize(0);

            doc.setId(0);
            doc.setDocSubject(DocSubject);
            doc.setDoccontent(docContent);
            doc.setAttachments(new DocAttachment[]{da});
            doc.setMaincategory(maincategoryid);
            doc.setSubcategory(subcategoryid);
            doc.setSeccategory(seccategoryid);
            // doc.setDocType(2);
            doc.setIsreply("0");
            doc.setDocCode(code);

            // writeLog("用户封装["+userID+"]");
            User user = this.getUser(Util.getIntValue(userID, 0));
            // writeLog("用户封装end["+userID+"]");

            docids = serviceImpl.createDocByUser(doc, user) + "";
            // docids = serviceImpl.createDoc(doc, session)+"";
            // writeLog("文档创建id"+docids);
            if (Util.getIntValue(docids, 0) <= 0) {
                docids = "0";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writeLog("文档创建异常!");
            writeLog(e.toString());
            writeLog("文档创建异常!end");
            return "0";
        }
        // bean.writeLog("文档id:"+docids);

        return docids;
    }

    /**
     * 创建文档
     *
     * @param title      标题
     * @param Content    内容
     * @param userID     创建用户id
     * @param categoryid 子目录id
     * @param code       文档编号
     * @param atts       附件数组对象 DocAttachment[]
     * @return
     */
    public String createDocAttas(String title, String Content, String userID, String categoryid, String code,
                                 DocAttachment[] atts) {

        String docids = "0"; // 文件id

        int filesize = 0; // 文件大小

        String locapath = "";

        DocServiceImpl serviceImpl = new DocServiceImpl(); // 创建文档服务

        int maincategoryid = 0; // 文档主目录
        int subcategoryid = 0; // 文档分目录
        int seccategoryid = 0; // 文档子目录

        seccategoryid = Util.getIntValue(categoryid, 0);

        try {

            DocInfo doc = new DocInfo();

            String DocSubject = title;
            String docContent = Content;

            doc.setId(0);
            doc.setDocSubject(DocSubject);
            doc.setDoccontent(docContent);
            if (atts != null) {
                doc.setAttachments(atts);
            }
            doc.setMaincategory(maincategoryid);
            doc.setSubcategory(subcategoryid);
            doc.setSeccategory(seccategoryid);
            // doc.setDocType(2);
            doc.setIsreply("0");
            doc.setDocCode(code);

            // writeLog("用户封装["+userID+"]");
            User user = getUser(Util.getIntValue(userID, 0));
            // writeLog("用户封装end["+userID+"]");

            docids = serviceImpl.createDocByUser(doc, user) + "";
            // docids = serviceImpl.createDoc(doc, session)+"";
            // writeLog("文档创建id"+docids);
            if (Util.getIntValue(docids, 0) <= 0) {
                docids = "0";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writeLog("文档创建异常!");
            writeLog(e.toString());
            writeLog("文档创建异常!end");
            return "0";
        }
        // bean.writeLog("文档id:"+docids);

        return docids;
    }

    /**
     * 用户封装
     *
     * @param userid
     * @return
     */
    private static User getUser(int userid) {
        User user = new User();
        try {
            ResourceComInfo rc = new ResourceComInfo();
            user.setUid(userid);
            user.setLoginid(rc.getLoginID("" + userid));
            user.setFirstname(rc.getFirstname("" + userid));
            user.setLastname(rc.getLastname("" + userid));
            user.setLogintype("1");
            // user.setAliasname(rc.getAssistantID(""+userid));
            // user.setTitle(rs.getString("title"));
            // user.setTitlelocation(rc.getLocationid(""+userid));
            user.setSex(rc.getSexs("" + userid));
            user.setLanguage(7);
            // user.setTelephone(rc);
            // user.setMobile(rc.getm);
            // user.setMobilecall(rs.getString("mobilecall"));
            user.setEmail(rc.getEmail("" + userid));
            // user.setCountryid();
            user.setLocationid(rc.getLocationid("" + userid));
            user.setResourcetype(rc.getResourcetype("" + userid));
            // user.setStartdate(rc.gets);
            // user.setEnddate(rc.gete);
            // user.setContractdate(rc.getc);
            user.setJobtitle(rc.getJobTitle("" + userid));
            // user.setJobgroup(rs.getString("jobgroup"));
            // user.setJobactivity(rs.getString("jobactivity"));
            user.setJoblevel(rc.getJoblevel("" + userid));
            user.setSeclevel(rc.getSeclevel("" + userid));
            user.setUserDepartment(Util.getIntValue(rc.getDepartmentID("" + userid), 0));
            // user.setUserSubCompany1(Util.getIntValue(rc.get,0));
            // user.setUserSubCompany2(Util.getIntValue(rs.getString("subcompanyid2"),0));
            // user.setUserSubCompany3(Util.getIntValue(rs.getString("subcompanyid3"),0));
            // user.setUserSubCompany4(Util.getIntValue(rs.getString("subcompanyid4"),0));
            user.setManagerid(rc.getManagerID("" + userid));
            user.setAssistantid(rc.getAssistantID("" + userid));
            // user.setPurchaselimit(rc.getPropValue(""+userid));
            // user.setCurrencyid(rc.getc);
            // user.setLastlogindate(rc.get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 获取流程附件目录存放 目录id
     *
     * @param workflowid
     * @return
     */
    public static int getCategory(String workflowid, String selVal) {
        int id = 0;
        RecordSet rs = new RecordSet();
        String sql = "select catelogtype,selectedcatelog,doccategory from workflow_base where id = ?";
        rs.executeQuery(sql, Util.getIntValue(workflowid, 0));
        if (rs.next()) {
            int type = rs.getInt("catelogtype");
            String sid = rs.getString("selectedcatelog");
            String doccategory = rs.getString("doccategory");
            if (type == 0) {
                String[] doccategorys = doccategory.split(",");
                if (doccategorys.length > 0) {
                    id = Util.getIntValue(doccategorys[doccategorys.length - 1], 0);
                }
            } else {
                rs.executeQuery("select doccategory,selectvalue from workflow_selectitem where fieldid = ? and selectvalue=?", sid, selVal);
                if (rs.next()) {
                    String[] doccategorys = Util.null2String(rs.getString("doccategory")).split(",");
                    if (doccategorys.length > 0) {
                        id = Util.getIntValue(doccategorys[doccategorys.length - 1], 0);
                    }
                }
            }
        }
        return id;
    }

    /**
     * 获取流程附件目录存放 目录id
     *
     * @param workflowid
     * @return
     */
    public int getCategory(String workflowid, String selVal, boolean flag) {

        int id = 0;
        RecordSet rs = new RecordSet();
        String sql = "select catelogtype,selectedcatelog,doccategory from workflow_base where id = ?";
        rs.executeQuery(sql, Util.getIntValue(workflowid, 0));
        if (rs.next()) {

            int type = rs.getInt("catelogtype");
            String sid = rs.getString("selectedcatelog");
            String doccategory = rs.getString("doccategory");
            if (!flag) {
                String[] doccategorys = doccategory.split(",");
                if (doccategorys.length > 0) {
                    id = Util.getIntValue(doccategorys[doccategorys.length - 1], 0);
                }
//				if (doccategorys.length > 2){
//					id = Util.getIntValue(doccategorys[2], 0);
//				}
            } else {
                rs.executeQuery("select doccategory,selectvalue from workflow_selectitem where fieldid = ? and selectvalue=?", sid, selVal);
                if (rs.next()) {

                    String[] doccategorys = Util.null2String(rs.getString("doccategory")).split(",");
                    if (doccategorys.length > 0) {
                        id = Util.getIntValue(doccategorys[doccategorys.length - 1], 0);
                    }
//					if (doccategorys.length > 2){
//						id = Util.getIntValue(doccategorys[2], 0);
//					}
                }
            }
        }
        return id;
    }

    /**
     * 文档授权（非默认共享）
     *
     * @param sharetype     共享类型 1:人力资源 2:分部 3:部门 4:角色 5:所有人
     * @param objectid      对象id
     * @param seclevelMin   安全级别从
     * @param seclevelMax   安全级别到
     * @param sharelevel    共享级别 1：查看 2：编辑 3.完全控制
     * @param downloadlevel 下载： 0:否 1：是
     * @param sharesource   来源 1:表示从流程那边过来 0：表示非流程部分过来
     * @param rolelevel     角色级别 0：部门 1：分部 2：总部
     * @param contains      包含下级 （部门） 0：不包含 1：包含
     */
    public static void docShare(int docid, int sharetype, int objectid, int seclevelMin, int seclevelMax, int sharelevel,
                                int downloadlevel, int sharesource, int rolelevel, int contains) {

        RecordSet rs = new RecordSet();
        int content = 1;
        int opuser = 0;

        int userid = 0;
        int subcompanyid = 0;
        int departmentid = 0;
        int roleid = 0;
        int foralluser = 0;
        int includesub = 0;
        if (sharetype == 5) {// 所有人
            content = 1;
            opuser = 0;

            foralluser = 1;
            rolelevel = 0;

        } else if (sharetype == 4) {// 4:角色
            content = Util.getIntValue(objectid + "" + rolelevel, 0);
            opuser = objectid;

            roleid = objectid;
            foralluser = 0;

        } else if (sharetype == 3) {// 3:部门
            content = objectid;
            opuser = objectid;

            departmentid = objectid;
            foralluser = 0;
            rolelevel = 0;

            if (contains == 1) {
                sharesource = 2;
                includesub = 1;
            }

        } else if (sharetype == 2) {// 2:分部
            content = objectid;
            opuser = objectid;

            subcompanyid = objectid;
            foralluser = 0;
            rolelevel = 0;
        } else if (sharetype == 1) {// 1:人力资源
            content = objectid;
            opuser = objectid;
            seclevelMin = 0;
            seclevelMax = 255;

            userid = objectid;
            foralluser = 0;
            rolelevel = 0;
        }

//		String shsql1 = "insert into ShareinnerDoc (sourceid,type,content,seclevel,sharelevel,srcfrom,opuser,sharesource,downloadlevel,seclevelmax)"
//				+ " values (" + docid + "," + sharetype + "," + content + "," + seclevelMin + "," + sharelevel + ","
//				+ sharetype + "," + opuser + "," + sharesource + "," + downloadlevel + "," + seclevelMax + ")";
//		rs.executeSql(shsql1);

        // writeLog("docshareSql:"+shsql1);

        String shsql2 = "insert into DocShare(docid,sharetype,seclevel,rolelevel,sharelevel,userid,subcompanyid,departmentid,roleid,foralluser,crmid,sharesource,orgGroupId,downloadlevel,seclevelmax,includesub)"
                + " values (" + docid + "," + sharetype + "," + seclevelMin + "," + rolelevel + "," + sharelevel + ","
                + userid + "," + subcompanyid + "," + departmentid + "," + roleid + "," + foralluser + ",0,"
                + sharesource + ",0," + downloadlevel + "," + seclevelMax + "," + includesub + ")";
        rs.executeSql(shsql2);

        // writeLog("docshareSql2:"+shsql2);

        DocViewer docViewer = new DocViewer();
        try {
            docViewer.setDocShareByDoc(docid + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 文档授权（非默认共享）
     *
     * @param sharetype     共享类型 1:人力资源 2:分部 3:部门 4:角色 5:所有人
     * @param objectid      对象id
     * @param seclevelMin   安全级别从
     * @param seclevelMax   安全级别到
     * @param sharelevel    共享级别 1：查看 2：编辑 3.完全控制
     * @param downloadlevel 下载： 0:否 1：是
     * @param sharesource   来源 1:表示从流程那边过来 0：表示非流程部分过来
     * @param rolelevel     角色级别 0：部门 1：分部 2：总部
     * @param contains      包含下级 （部门） 0：不包含 1：包含
     * @param secCategory   目录id
     */
    public void docShare(int docid, int sharetype, int objectid, int seclevelMin, int seclevelMax, int sharelevel,
                         int downloadlevel, int sharesource, int rolelevel, int contains, int secCategory) {

        RecordSet rs = new RecordSet();
        int content = 1;
        int opuser = 0;

        int userid = 0;
        int subcompanyid = 0;
        int departmentid = 0;
        int roleid = 0;
        int foralluser = 0;
        int includesub = 0;
        if (sharetype == 5) {// 所有人
            content = 1;
            opuser = 0;

            foralluser = 1;
            rolelevel = 0;

        } else if (sharetype == 4) {// 4:角色
            content = Util.getIntValue(objectid + "" + rolelevel, 0);
            opuser = objectid;

            roleid = objectid;
            foralluser = 0;

        } else if (sharetype == 3) {// 3:部门
            content = objectid;
            opuser = objectid;

            departmentid = objectid;
            foralluser = 0;
            rolelevel = 0;

            if (contains == 1) {
                sharesource = 2;
                includesub = 1;
            }

        } else if (sharetype == 2) {// 2:分部
            content = objectid;
            opuser = objectid;

            subcompanyid = objectid;
            foralluser = 0;
            rolelevel = 0;
        } else if (sharetype == 1) {// 1:人力资源
            content = objectid;
            opuser = objectid;
            seclevelMin = 0;
            seclevelMax = 255;

            userid = objectid;
            foralluser = 0;
            rolelevel = 0;
        }

//		String shsql1 = "insert into ShareinnerDoc (sourceid,type,content,seclevel,sharelevel,srcfrom,opuser,sharesource,downloadlevel,seclevelmax, sec_category)"
//				+ " values (" + docid + "," + sharetype + "," + content + "," + seclevelMin + "," + sharelevel + ","
//				+ sharetype + "," + opuser + "," + sharesource + "," + downloadlevel + "," + seclevelMax + ", "+ secCategory +")";
//		rs.executeSql(shsql1);

        // writeLog("docshareSql:"+shsql1);

        String shsql2 = "insert into DocShare(docid,sharetype,seclevel,rolelevel,sharelevel,userid,subcompanyid,departmentid,roleid,foralluser,crmid,sharesource,orgGroupId,downloadlevel,seclevelmax,includesub)"
                + " values (" + docid + "," + sharetype + "," + seclevelMin + "," + rolelevel + "," + sharelevel + ","
                + userid + "," + subcompanyid + "," + departmentid + "," + roleid + "," + foralluser + ",0,"
                + sharesource + ",0," + downloadlevel + "," + seclevelMax + "," + includesub + ")";
        rs.executeSql(shsql2);

        // writeLog("docshareSql2:"+shsql2);

        DocViewer docViewer = new DocViewer();
        try {
            docViewer.setDocShareByDoc(docid + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] inputStream2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 本地文件上传到附件上并且生成文档id
     *
     * @param bytes    字节码
     * @param fileName 文件名 带后缀
     * @param user     用户
     * @param dirId    目录id
     * @return 文档id
     * @throws Exception 异常
     */
    public static int enclosureImageUpload(byte[] bytes, String fileName, User user, int dirId) throws Exception {
        ImageFileManager imageFileManager = new ImageFileManager();
        imageFileManager.setImagFileName(fileName);
        imageFileManager.setData(bytes);
        int imageFileId = imageFileManager.saveImageFile();
        DocSaveService docSaveService = new DocSaveService();
        int docId = docSaveService.accForDoc(dirId, imageFileId, user);
        DocViewer docViewer = new DocViewer();
        docViewer.setDocShareByDoc("" + docId);
        return docId;
    }

    /**
     * 本地文件上传到附件上并且生成文档id
     *
     * @param filePath 本地文件路径
     * @param fileName 文件名带后缀
     * @param user     用户
     * @param dirId    目录id
     * @return 文档id
     * @throws Exception 异常
     */
    public static int enclosureImageUpload(String filePath, String fileName, User user, int dirId) throws Exception {
        byte[] bytes = inputStream2ByteArray(filePath);
        return enclosureImageUpload(bytes, fileName, user, dirId);
    }

    /**
     * 输入流上传到附件上并且生成文档id
     *
     * @param inputStream 输入流
     * @param fileName    文件名带后缀
     * @param user        用户
     * @param dirID       目录id
     * @return 文档id
     */
    public static int uploadFileStream(InputStream inputStream, String fileName, User user, int dirID) {
        int result = 0;
        try {
            byte[] data = toByteArray(inputStream);
            int bytesRead = data.length;
            String suffix = "";
            if (bytesRead >= 2 && data[0] == (byte) 0x89 && data[1] == (byte) 0x50) {
                suffix = ".png";
            } else if (bytesRead >= 3 && data[0] == (byte) 0x47 && data[1] == (byte) 0x49 && data[2] == (byte) 0x46) {
                suffix = ".gif";
            } else if (bytesRead >= 4 && data[0] == (byte) 0x42 && data[1] == (byte) 0x4D) {
                suffix = ".bmp";
            } else {
                suffix = ".jpeg";
            }
            result = enclosureImageUpload(data, fileName + suffix, user, dirID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 文件链接上传到附件生成文档id
     *
     * @param fileName 文件名
     * @param fileUrl  文件下载链接
     * @param user     用户
     * @param dirID    目录id
     * @return 文档id
     */
    public static int downloadFile(String fileName, String fileUrl, User user, int dirID) {
        BaseBean baseBean = new BaseBean();
        int result = 0;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            result = uploadFileStream(inputStream, fileName, user, dirID);
        } catch (Exception e) {
            baseBean.writeLog(e.getMessage());
        }
        return result;
    }

    /**
     * 解压文件并返回InputStream
     *
     * @param docid
     * @param rs
     * @return
     */
    public static InputStream unzipFile(Integer docid, RecordSet rs) throws Exception {
        InputStream inputStream = null;
        String filePathSql = "select f.filerealpath " +
                "from imagefile f " +
                "left join docimagefile d on f.IMAGEFILEID = d.IMAGEFILEID " +
                "where d.docid = ?";
        rs.executeQuery(filePathSql, docid);
        if (rs.next()) {
            String fileRealPath = rs.getString("filerealpath");
            log.info("fileRealPath:" + fileRealPath);
            if (StringUtils.isNotBlank(fileRealPath)) {
                // 解压
                ZipFile zipFile = new ZipFile(fileRealPath);
                Enumeration<?> entries = zipFile.entries();
                if (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    inputStream = zipFile.getInputStream(entry);
                }
                zipFile.close();
                log.info("解压完毕！");
            }
        }
        return inputStream;
    }

    /**
     * 获取文件名
     *
     * @param docId
     * @param rs
     * @return
     */
    public static String getFileName(Integer docId, RecordSet rs) {
        String sql = "select imagefilename from docimagefile where docid = ?";
        rs.executeQuery(sql, docId);
        if (rs.next()) {
            return Util.null2String(rs.getString("imagefilename"));
        } else {
            return null;
        }
    }

    /**
     * 通过docid，获取文件的base64编码
     *
     * @param docId
     * @param rs
     * @return
     * @throws IOException
     */
    public static String getBase64Code(Integer docId, RecordSet rs) {
        BaseBean baseBean = new BaseBean();
        String base64File = "";

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {


            InputStream inputStream = null;
            String filePathSql = "select f.filerealpath " +
                    "from imagefile f " +
                    "left join docimagefile d on f.IMAGEFILEID = d.IMAGEFILEID " +
                    "where d.docid = ?";
            rs.executeQuery(filePathSql, docId);
            if (rs.next()) {
                String fileRealPath = rs.getString("filerealpath");
                baseBean.writeLog("fileRealPath:" + fileRealPath);
                if (StringUtils.isNotBlank(fileRealPath)) {
                    // 解压
                    ZipFile zipFile = new ZipFile(fileRealPath);
                    Enumeration<?> entries = zipFile.entries();
                    if (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        inputStream = zipFile.getInputStream(entry);
                    }
                    baseBean.writeLog("解压完毕！");
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    base64File = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                    zipFile.close();
                    inputStream.close();
                }
            }

        } catch (Exception e) {
            baseBean.writeLog("文件转换base64失败！" + e.getMessage());
        }
        return base64File;
    }

    /**
     * 通过docId生产新的文档
     *
     * @param docId
     * @param fileName 文件名
     * @param user     创建文件的用户
     * @param dirID    目录id
     * @return
     */
    public static Integer generateNewDocument(Integer docId, String fileName, User user, int dirID, RecordSet rs) {
        String filePathSql = "select f.filerealpath " +
                "from imagefile f " +
                "left join docimagefile d on f.IMAGEFILEID = d.IMAGEFILEID " +
                "where d.docid = ?";
        rs.executeQuery(filePathSql, docId);
        int result = 0;
        if (rs.next()) {
            String fileRealPath = rs.getString("filerealpath");
            if (StringUtils.isNotBlank(fileRealPath)) {
                InputStream inputStream = null;
                // 解压
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(fileRealPath);
                    Enumeration<?> entries = zipFile.entries();
                    if (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        inputStream = zipFile.getInputStream(entry);
                        log.info("解压完毕！!!!!!!!!!!!!!!");
                        byte[] data = toByteArray(inputStream);
                        log.info("data size:" + data.length);
                        result = enclosureImageUpload(data, fileName, user, dirID);
                    }
                } catch (Exception e) {
                    log.error("uploadFileStream error: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                        zipFile.close();
                    } catch (IOException e) {
                        log.error("uploadFileStream close error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * base64转文档
     *
     * @param base64Code 文件base64编码数据
     * @param fileName   文件名
     * @param user       创建文件用户
     * @param dirID      文档目录
     * @param type       文档类型，0：正文，1：附件
     * @return
     * @throws Exception
     */
    public static Integer decoderBase64File2Doc(String base64Code, String fileName, User user, int dirID, String type, RecordSet rs) throws Exception {
        byte[] data = new BASE64Decoder().decodeBuffer(base64Code);

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[2048];
            ZipEntry entry = zipInputStream.getNextEntry();

            if (entry == null) {
                throw new IOException("No ZIP entry found");
            }

            int length;
            while ((length = zipInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            int docId = enclosureImageUpload(outputStream.toByteArray(), fileName, user, dirID);
            if ("0".equals(type)) {
                //更新docimagefile、docdetail，把文件类型改成正文
                String sql = "update docimagefile set docfiletype = 13, isextfile = null where docid = ?";
                rs.executeUpdate(sql, docId);

                sql = "update docdetail set doctype = 13 where id = ?";
                rs.executeUpdate(sql, docId);
            }

            return docId;
        } catch (Exception e) {
            log.error("-------base64转文档-------" + e.getMessage());
            return null;
        }

    }

    public static Integer copyDoc(Integer docId, String fileName, User user, Integer dirID) throws Exception {

        DocManager docManager = new DocManager();
        docManager.setId(docId);
        docManager.setUserid(user.getUID());
        docManager.setUsertype(user.getLogintype());
        docManager.setDocsubject(fileName);
        docManager.setSeccategory(dirID);

        return docManager.copyDoc();
    }

}