<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台管理系统</title>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<link href="Gaobei_style.css" rel="stylesheet" type="text/css">
<STYLE type=text/css>.style2 {
	FONT-WEIGHT: bold; COLOR: #ffffff
    }
    .style3 {
	FONT-SIZE: 12px
    }
    .style4 {
	FONT-WEIGHT: bold; FONT-SIZE: 12px; COLOR: #ffffff
    }
    BODY {
	MARGIN: 0px
    }
</STYLE>
<BODY>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" bgColor=#00309c border=0>
  <TBODY>
  <TR>
    <TD vAlign=center height="26%">　</TD></TR>
  <TR>
    <TD height=2>
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD 
          style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr='#5A7EDC', EndColorStr='#C7DDFF')" 
          width="31%" height=2></TD>
          <TD 
          style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr='#C7DDFF', EndColorStr='#5A7EDC')" 
          width="69%" height=2></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD bgColor=#5a7edc height="48%">
      <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD style="PADDING-RIGHT: 12px" align=right width="57%">
            <DIV style="PADDING-RIGHT: 40px"></DIV>
            <DIV style="FONT-WEIGHT: bold; FONT-SIZE: 14pt"><BR>
            <TABLE cellSpacing=3 cellPadding=5 width="505" border=0>
              <TBODY>
              <TR>
                <TD width="489" colspan="2"><font color="#FFFFFF"><b>===欢迎使用===</b></font></TD></TR>
              <TR>
                <TD width="214">
                  <DIV align=left>
<script language="JavaScript">
    <!--
    // Randomizer 
    rnd.today=new Date();
    rnd.seed=rnd.today.getTime();
    function rnd() {
    rnd.seed = (rnd.seed*9301+49297) % 233280;
    return rnd.seed/(233280.0);
    };
    function rand(number) {
    return Math.ceil(rnd()*number);
    };
    // end randomizer. -->
</script>
                </DIV></TD>
                <TD width="262" valign="bottom">
                  <p align="center">
		<!-- <IMG height=56 alt=网站系统 src="xplog.bmp" width=164 align="right"> --></TD></TR>
              <TR>
                <TD width="489" colspan="2">
                  <DIV align=center><font color="#FFFFFF" size='12'>Router Moniter </font></DIV></TD></TR>
              <TR>
                <TD width="489" colspan="2">
				<p align="right"><font color="#FFFFFF">RouterMoniter 网络设备管理与预警系统</font></TD></TR>
              <TR>
                <TD width="489" colspan="2">　</TD></TR></TBODY></TABLE><SPAN 
            class=style4><BR><BR></SPAN></DIV></TD>
          <TD width=1>
            <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" 
            border=0>
              <TBODY>
              <TR>
                <TD 
                style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#5A7EDC', EndColorStr='#C7DDFF')" 
                height="50%"></TD></TR>
              <TR>
                <TD 
                style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#C7DDFF', EndColorStr='#5A7EDC')" 
                height="50%"><IMG height=1 
                  src="" 
                  width=1> </TD></TR></TBODY></TABLE></TD>
          <TD id=td0 width="43%">
            <form name="login" method="post" action="servlet" target="_top">
				<IMG height=15 alt=网站系统 src="footc.jpg"  width=1> 
            <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
              <TBODY>
              <TR>
                <TD vAlign=center align=middle>
                  <div align="center">
                  <TABLE id=table1 style="MARGIN: 20px" cellSpacing=3 
                  cellPadding=5 width="93%" border=0 
                  valign="middle">
                    <TBODY>
                    <TR>
                      <TD align=middle colSpan=3></TD></TR>
                    <TR>
                      <TD noWrap align=right>
                        <DIV class=style4 align=right>帐号：</DIV></TD>
                      <TD colSpan=2>
                      <input name="actionname" type="hidden" value="org.rm.action.logincation"/>
                      <input name="loginname"  type="text"  id="loginname" style="BORDER-RIGHT: 1px solid; BORDER-TOP: 1px solid; BORDER-LEFT: 1px solid; WIDTH: 110px; BORDER-BOTTOM: 1px solid" 
 maxlength="20" class="login"></TD></TR>
                    <TR>
                      <TD align=right>
                        <DIV class=style4 align=right>密码：</DIV></TD>
                      <TD colSpan=2><input name="password"  type="password" id="password" maxlength="20" class="login" style="BORDER-RIGHT: 1px solid; BORDER-TOP: 1px solid; BORDER-LEFT: 1px solid; WIDTH: 110px; BORDER-BOTTOM: 1px solid">
                      </TD></TR>
                    <TR vAlign=center align=middle>
                      <TD colSpan=3 height=40>&nbsp;&nbsp;&nbsp;&nbsp;<STRONG> 
                      <input type="submit" name="Submit3" value=" 登陆 " style="BORDER-RIGHT: #5a7edc 1px; BORDER-TOP: #5a7edc 1px; BORDER-LEFT: #5a7edc 1px; WIDTH: 40px; COLOR: white; BORDER-BOTTOM: #5a7edc 1px; BACKGROUND-COLOR: #5a7edc">
                      <input type="reset" name="Submit2" value=" 重置 " style="BORDER-RIGHT: #5a7edc 1px; BORDER-TOP: #5a7edc 1px; BORDER-LEFT: #5a7edc 1px; WIDTH: 70px; COLOR: white; BORDER-BOTTOM: #5a7edc 1px; BACKGROUND-COLOR: #5a7edc">
                        </STRONG>
                        <DIV align=center></DIV></TD></TR></TBODY></TABLE></div>
				</TD></TR></TBODY></TABLE></FORM></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD>
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD 
          style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr='#0C3CA1', EndColorStr='#F49538')" 
          width="31%" height=2></TD>
          <TD 
          style="FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr='#F49538', EndColorStr='#0C3CA1')" 
          width="69%" height=2></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD vAlign=bottom align=right height="26%">
      <DIV align=right>
      </DIV></TD></TR></TBODY></TABLE></BODY></HTML>