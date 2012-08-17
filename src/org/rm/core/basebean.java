package org.rm.core;

import java.lang.reflect.Field;

public   abstract class basebean{
  private StringBuffer FilterFilds=null; //过滤javabean 在创建SQL语句时候不需要的字段 （比如自动ID、和更新表数据的时候其他字段）
  private StringBuffer AllFilds=null;
  private int FilterCount=0;//总的过滤字段条目数
  
  private int FilterCurrent=0;//当前已经查找了几次  加这几个属性的目的为了，提高性能
  /*
   * 要过滤的字段
   */
  public void AddFilterFild(String Field){
	  if (FilterFilds==null )
	  {
		  FilterFilds =new StringBuffer();
	  }
	  FilterFilds.append("["+Field.toLowerCase()+"],"); //用来分开
	  FilterCount++;
  }
  /*
   * 只保留的字段
   */
  public void AddReserveFild(String Field){
	  //把所有字段放进去 第一次的时候用来创建和初始化所有字段
	  if (AllFilds==null){
		  AllFilds=new StringBuffer();
		  Field[] fields = this.getClass().getDeclaredFields();
		  for (int i = 0; i < fields.length; i++) {
			  AllFilds.append("["+fields[i].getName().toLowerCase()+"],");
		 }
		  FilterCount=fields.length;//总的要过滤的字段数据
	  }
	  int indexbegin=AllFilds.indexOf("["+Field.toLowerCase()+"]");
	  int indexend=indexbegin+Field.length()+3;
	  if (indexbegin>=0){
		  AllFilds.delete(indexbegin, indexend) ;
		  //剔除剩下要过滤的字段数
		  FilterCount=FilterCount-1;
	  }
  }
  
  public int FilterIndexOf(String Field)
  {   
	  //这里如果AllFilds有值，只做一次后 设置为NULL
	  if (AllFilds!=null ){
		  if (FilterFilds==null){FilterFilds =new StringBuffer();}
		  FilterFilds.append(AllFilds.toString());
		  AllFilds=null;
	  }
	  int returnVaule=-1;//-1:代表过滤字符串中没有你要查找的字符串
	  if (FilterCurrent<FilterCount){ 
		  if (FilterFilds!=null){
		   returnVaule= FilterFilds.indexOf("["+Field.toLowerCase()+"]") ;
		   if (returnVaule>=0)  FilterCurrent++; //如果找到过一次，就自动+1
		  }
	  }
	 return returnVaule;
  }
  /*
   * 清除计数器
   */
  public void ClearFilterCurrent() 
  {
	  FilterCurrent=0;
  }
  public abstract String getTableName(); //表名称

}
