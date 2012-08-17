package org.rm.core;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>Title:分页管理 </p>
 */
public class pagemanager {
  private Statement st = null;
  private ResultSet rs = null;
  private String PageFooter="";
  private int intCountTopic=0;
  private int intPageSize =10; //每页显示记录总数
  private int intPageCount= 0; //总页数
  private int intPage = 0; //当前页数
  private String QueryDb[][] = null;
  private String SQLwhere = ""; //只得到SQL的条件

  public pagemanager() {
  }
  /**
   *
   * @param request HttpServletRequest从页面传如入request
   * @param SQLstr String从页面传如入SQL
   * @param HttpFile String从页面传如入分页的页面名称一般是自身
   * @return String[][]//返回一个数据集合的2维数组
   * 说明：用2维数组的目的是，在多人开发中程序员不用知道后台数据库管理那块，能
   * 快速开发
   */
  public void SetintPageSize(int i){
	  intPageSize=i;
  }
  public String[][] DBCenPageResult(HttpServletRequest request,
                                   String SQLstr,
                                   String HttpFile) {
 
   if (request.getParameter("pages")==null){intPage=1;}
   else
     {
        try {
           intPage=Integer.parseInt(request.getParameter("pages"));
        }
        catch (Exception ex) {
          intPage=1;//转换错误时候 (防止客户直接在用地址拦输入)
        }

     }
   int index = 0;
   try {
	 Connection con = dbpool.getConMasterDB();
	 st = con.createStatement();
     index = SQLstr.toLowerCase().indexOf("from");
     SQLwhere = SQLstr.substring(index, SQLstr.length());
     SQLwhere = "select count(*) " + SQLwhere;
     rs = st.executeQuery(SQLwhere);
     if (rs.next()) intCountTopic = rs.getInt(1); // 得到总记录数
     rs.close();
    intPageCount = (intCountTopic+intPageSize-1) / intPageSize;//得到总页数
     ////调整待显示的页码
    if(intPage>intPageCount) intPage = intPageCount;
    if(intPage<0)intPage=1;
     try {

       if (intPage <= 1) {
         intPage = 1;
       }
       if (intPage >= intPageCount) {
         intPage = intPageCount;
       } //判断当前页数合法
     }
     catch (Exception ex) {
       intPage = 1;
     }

     rs = st.executeQuery(SQLstr);
     int cols = rs.getMetaData().getColumnCount(); //得到SQL的列数目
     QueryDb = new String[intPageSize][cols];//new 一个2维数组
     for (int i = 0; i < intPageSize*(intPage-1); i++) {//用循环到当前页
       if (rs.next()){
       };
     }
     for (int j = 0; j < intPageSize; j++) {
       if (rs.next()){
         for (int  k= 0; k < cols; k++) {
         QueryDb[j][k] = rs.getString(1);
       }
       }
       else{
         for (int  k= 0; k < cols; k++) {
       QueryDb[j][k] = "";
     }

       }
     }
   }
   catch (Exception ex) {
    
      log.error(getClass(), ex.toString());
   }
   finally {
     close();
   }
   PageFooter=GetPageFooter(intCountTopic,intPageSize,intPageCount,intPage,HttpFile);
   return QueryDb;
 }
 /*
 *页底
 */
public String GetPageFooter(){
return this.PageFooter;
 }
 
 /**
  * 关闭资源
  */
 private void close() {
      try {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception ex) {
      }
      try {
        if (st != null) {
          st.close();
        }
      }
      catch (Exception ex) {
      }
    }
 /**
 *
 * @param intCountTopic int 主题总数,即select选出的、库中所有记录总数
 * @param intPageSize int 每页显示主题数,即每页显示的记录总数
 * @param intPageCount int 总页数
 * @param intPage int 当前页数
 * @param HttpFile String 一般是本页
 * @return String
 */

 public static String GetPageFooter(int intCountTopic,
                                    int intPageSize,
                                    int intPageCount,
                                    int intPage,
                                    String HttpFile ) {
           int next, prev;
           String str="";
           prev=intPage-1;
           next=intPage+1;
           str=str+"<font style='font-size: 9pt'>总计<font color='red'>"+intCountTopic+"</font>条记录,"+
                   "【共<font  color='red'>"+intPageCount+"</font>页】";
           str=str+"【条"+intPageSize+"/页】 当前第<font color='red'>"+intPage+"</font>页(列出第"+
                   ((intPageSize*intPage+1)-intPageSize)+"到第"+(intPage*intPageSize)+"条) &nbsp; &nbsp; ";
           if(intPage>1)
           str += " <A href=" + HttpFile + "?pages=1"+">第一页</A> ";
           else str += " 第一页 ";
           if(intPage>1)
           str += " <A href=" + HttpFile + "?pages=" + prev + ">上一页</A> ";
           else str += " 上一页 ";

           if(intPage<intPageCount)
           str += " <A href=" + HttpFile + "?pages=" + next + ">下一页</A> ";
           else str += " 下一页 ";

           if(intPageCount>1&&intPage!=intPageCount)
           str += " <A href=" + HttpFile + "?pages=" + intPageCount + ">最后页</A>";
           else str += " 最后页 </font>";
           str +=" 转到<INPUT TYPE='text'NAME='pages' size='2'>页  <input type='submit' name='Submit' value='go'>";
           return str;
           }
}
