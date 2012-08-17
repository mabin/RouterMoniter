<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ExtTop - Desktop Sample App</title>

    <link rel="stylesheet" type="text/css" href="../ExtJS/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="css/desktop.css" />

    <!-- GC -->
 	<!-- LIBS -->
 	<script type="text/javascript" src="../ExtJS/adapter/ext/ext-base.js"></script>
 	<script type="text/javascript" src="../ExtJS/ext-lang-zh_CN.js"></script>
 	<!-- ENDLIBS -->

    <script type="text/javascript" src="../ExtJS/ext-all-debug.js"></script>

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
    <a href="http://extjs.com" target="_blank" style="margin:5px; float:right;"><img src="images/powered.gif" /></a>

    <dl id="x-shortcuts">
        <dt id="router-grid-win-shortcut">
            <a href="#"><img src="images/grid32x32.gif" />
            <div>Router Grid Window</div></a>
        </dt>
        <dt id="grid-win-shortcut">
            <a href="#"><img src="images/s.gif" />
            <div>Grid Window</div></a>
        </dt>
        <dt id="acc-win-shortcut">
            <a href="#"><img src="images/s.gif" />
            <div>Accordion Window</div></a>
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
