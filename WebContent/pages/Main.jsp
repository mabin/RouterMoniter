<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title>Router Moniter</title>

    <link rel="stylesheet" type="text/css" href="../ExtJS/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="css/desktop.css" />

    <!-- GC -->
 	<!-- LIBS -->
 	<script type="text/javascript" src="../ExtJS/adapter/ext/ext-base.js"></script>
 	<script type="text/javascript" src="../ExtJS/ext-lang-zh_CN.js"></script>
 	<!-- ENDLIBS -->

    <script type="text/javascript" src="../ExtJS/ext-all-debug.js"></script>
    <script type="text/javascript" src="js/uxcom.js"></script>
	<!-- Datetime tools -->
	<script type="text/javascript" src="js/datefield/DateTimeField.js"></script>
	<script type="text/javascript" src="js/datefield/Spinner.js"></script>
	<script type="text/javascript" src="js/datefield/SpinnerField.js"></script>
	<link rel="stylesheet" type="text/css" href="js/datefield/Spinner.css"/>
    <!-- DESKTOP -->
    <script type="text/javascript" src="js/StartMenu.js"></script>
    <script type="text/javascript" src="js/TaskBar.js"></script>
    <script type="text/javascript" src="js/Desktop.js"></script>
    <script type="text/javascript" src="js/App.js"></script>
    <script type="text/javascript" src="js/Module.js"></script>
    <script type="text/javascript" src="Main.js"></script>
</head>
<body scroll="no">

<div id="x-desktop">

    <dl id="x-shortcuts">
        <dt id="router-grid-win-shortcut">
            <a href="#"><img src="images/router.png" />
            <div>路由管理</div></a>
        </dt>
        <dt id="context-grid-win-shortcut">
            <a href="#"><img src="images/mail.png" />
            <div>Context管理</div></a>
        </dt>
        <dt id="router-grid-win-shortcut">
            <a href="#"><img src="images/mail.png" />
            <div>邮件管理</div></a>
        </dt>
        <dt id="router-grid-win-shortcut">
            <a href="#"><img src="images/phone.png" />
            <div>短信管理</div></a>
        </dt>
        <dt id="router-grid-win-shortcut">
            <a href="#"><img src="images/s.gif" />
            <div>人员管理</div></a>
        </dt>
    </dl>
</div>

<div id="ux-taskbar">
	<div id="ux-taskbar-start"></div>
	<div id="ux-taskbuttons-panel"></div>
	<div class="x-clear"></div>
</div>

</body>
</html>
