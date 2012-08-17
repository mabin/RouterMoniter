package org.rm.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rm.bean.t_systemenu;
import org.rm.core.*;

/*
 *系统菜单树
 */
public class DBSysMenutree {
	private int firstflg = 0;// 第一次标记
	private List<t_systemenu> nodeList = new ArrayList<t_systemenu>();
	StringBuffer returnStr = new StringBuffer();

	/*
	 * 数据库获取表数据
	 */
	public void SetNodeList() {
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		dbquery db=null;
		try {
			db=new dbquery();
			db.SetMasterDB();
        	con = db.GetCon();
			st = con.createStatement();
			rs = st.executeQuery("select ID,PARENTID,NAME,CODE,DESCRIPTION,JSURL,JSCOM,JSPURL,SYSFLG,VALIDSTATE,XTYPE from T_SYSTEMENU order by ID, SEQUENCE ");
			while (rs.next()) {

				t_systemenu bean = new t_systemenu();
				bean.setId(rs.getInt("ID"));
				bean.setParentid(rs.getInt("PARENTID"));
				bean.setName(rs.getString("NAME"));
				bean.setCode(rs.getString("CODE"));
				bean.setDescription(rs.getString("DESCRIPTION"));
				bean.setJspurl(rs.getString("JSPURL"));
				bean.setJsurl(rs.getString("JSURL"));
				bean.setJscom(rs.getString("JSCOM"));
				bean.setSysflg(rs.getString("SYSFLG"));
				bean.setValidstate(rs.getString("VALIDSTATE"));
				bean.setXtype(rs.getString("XTYPE"));
				if (bean.getSysflg().equals("Y")) {
					bean.setSysflg("是");
					bean.setIconCls("icon-f");
				} else {
					bean.setSysflg("否");
					bean.setIconCls("icon-f");
				}

				if (bean.getValidstate().equals("Y")) {
					bean.setValidstate("是");
				} else {
					bean.setValidstate("否");
				}

				nodeList.add(bean);
			}
		} catch (SQLException e) {
		} finally {
			db.freecon(con, st,null);
        	db=null;
		}
	}
	public DBSysMenutree() {
		SetNodeList();
		t_systemenu bean = new t_systemenu();
		bean.setId(0);
		bean.setParentid(-1);
		bean.setName("菜单、功能权限树");
		bean.setIconCls("icon-f");

		returnStr.append("[");
		CreateTree(nodeList, bean);
		returnStr.append("]");

	}

	public void CreateTree(List<t_systemenu> list, t_systemenu node) {
		firstflg = firstflg + 1;
		List<t_systemenu> childList = getChildList(list, node);
		if (childList.size() > 0) {
			if (firstflg > 1) {
				returnStr.append("{id:");
				returnStr.append(node.getId());
				returnStr.append(",parentid:");
				returnStr.append("'" + node.getParentid() + "'");
				returnStr.append(",text:");
				returnStr.append("'" + node.getName() + "'");
				returnStr.append(",code:");
				returnStr.append("'" + node.getCode() + "'");
				returnStr.append(",jsurl:");
				returnStr.append("'" + node.getJsurl() + "'");
				returnStr.append(",jscom:");
				returnStr.append("'" + node.getJscom() + "'");
				
				returnStr.append(",jspurl:");
				returnStr.append("'" + node.getJspurl() + "'");
				returnStr.append(",description:");
				returnStr.append("'" + node.getDescription() + "'");
				returnStr.append(",xtype:");
				returnStr.append("'" + node.getXtype() + "'");
				returnStr.append(",sysflg:");
				returnStr.append("'" + node.getSysflg() + "'");

				returnStr.append(",iconCls:");
				returnStr.append("'" + node.getIconCls() + "'");

				returnStr.append(",uiProvider:");
				returnStr.append("'col'");

				returnStr.append(",validstate:");
				returnStr.append("'" + node.getValidstate() + "'");

				returnStr.append(",children:[");
			}
			Iterator<t_systemenu> it = childList.iterator();
			while (it.hasNext()) {
				t_systemenu n = it.next();
				CreateTree(list, n);
			}

			if (firstflg > 1) {
				returnStr.append("]},");
			}

		} else {
			if (firstflg > 1) {
				returnStr.append("{id:");
				returnStr.append(node.getId());
				returnStr.append(",parentid:");
				returnStr.append("'" + node.getParentid() + "'");
				returnStr.append(",text:");
				returnStr.append("'" + node.getName() + "'");
				returnStr.append(",code:");
				returnStr.append("'" + node.getCode() + "'");
				returnStr.append(",jsurl:");
				returnStr.append("'" + node.getJsurl() + "'");
				returnStr.append(",jscom:");
				returnStr.append("'" + node.getJscom() + "'");
				
				returnStr.append(",jspurl:");
				returnStr.append("'" + node.getJspurl() + "'");
				returnStr.append(",description:");
				returnStr.append("'" + node.getDescription() + "'");
				returnStr.append(",xtype:");
				returnStr.append("'" + node.getXtype() + "'");
				returnStr.append(",sysflg:");
				returnStr.append("'" + node.getSysflg() + "'");
				returnStr.append(",iconCls:");
				returnStr.append("'" + node.getIconCls() + "'");
				returnStr.append(",uiProvider:");
				returnStr.append("'col'");
				returnStr.append(",validstate:");
				returnStr.append("'" + node.getValidstate() + "'");

				returnStr.append(",leaf:true},");
			}
		}
	}

	public boolean hasChild(List<t_systemenu> list, t_systemenu node) { // 判断是否有子节点
		return getChildList(list, node).size() > 0 ? true : false;
	}

	public List<t_systemenu> getChildList(List<t_systemenu> list, t_systemenu node) { // 得到子节点列表
		List<t_systemenu> li = new ArrayList<t_systemenu>();
		synchronized (list) {
			Iterator<t_systemenu> it = list.iterator();
			while (it.hasNext()) {
				t_systemenu n = it.next();
				if (n.getParentid() == node.getId()) {
					li.add(n);
					it.remove(); // // list 作为全局变量， 这里remove所以要线程同步 ，
					// 如果不remove则不要线程同步也可。
				}
			}
		}
		return li;
	}

	public String GetJSONString() {
		String returnValue = returnStr.toString().replaceAll(",]", "]");
		return returnValue.substring(0, returnValue.length() - 2);
	}

}