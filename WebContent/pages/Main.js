/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

// Sample desktop configuration

//公用变量
var servletPath="../servlet";
var GridPageSize=20;
var itemsheight=25; //表单行高
var msg = {"1":"服务器返回：数据提交成功！"};
var contextid='';
var routerid = '';
MyDesktop = new Ext.app.App({
	init :function(){
		Ext.QuickTips.init();
	},

	getModules : function(){
		return [
			new MyDesktop.RouterGridWindow(),
			new MyDesktop.ContextGridWindow(),
			new MyDesktop.PeopleGridWindow()
		];
	},

    // config for the start menu
    getStartConfig : function(){
        return {
            title: 'Jack Slocum',
            iconCls: 'user',
            toolItems: [{
                text:'Settings',
                iconCls:'settings',
                scope:this
            },'-',{
                text:'Logout',
                iconCls:'logout',
                scope:this
            }]
        };
    }
});


/*
 * 路由器信息显示window
 */
MyDesktop.RouterGridWindow = Ext.extend(Ext.app.Module, {
    id:'router-grid-win',
    init : function(){
        this.launcher = {
            text: 'Grid Window',
            iconCls:'icon-grid',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('router-grid-win');
        if(!win){
            win = desktop.createWindow({
                id: 'router-grid-win',
                title:'Grid Window',
                width:950,
                height:480,
                iconCls: 'icon-grid',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
				loadMask:{msg:'正在加载数据,请稍候.....'},
				closeable:true,
                layout: 'fit',
                items:	new Ext.grid.GridPanel({
							//header:true, region:'center', split:true, loadMask:true,
							//border : true,stripeRows : false, enableHdMenu:false,
							store : RouterStore,
							columns: [
								{header:'序号',dataIndex:'id',width:30,hidden:false},
								{header:'设备类型',dataIndex:'devicetype',sortable:true,width:77},
								{header:'主机名',dataIndex:'hostname',sortable:true,width:77},
								{header:'IP地址',dataIndex:'deviceip',sortable:true,width:77},
								{header:'登陆名',dataIndex:'loginname',sortable:true,width:77},
								{header:'密码',dataIndex:'password',sortable:true,width:77},
								{header:'状态',dataIndex:'status',sortable:true,width:37},
								{header:'最后在线时间',dataIndex:'lastonline',sortable:true,width:157},
								{header:'维护人',dataIndex:'devicepurpose',sortable:true,width:77},
								{header:'设备位置',dataIndex:'location',sortable:true,width:77},
								{header:'设备归属',dataIndex:'devicedep',sortable:true,width:77},
								{header:'操作',width:80, xtype:'actioncolumn',
									items:[{icon   : 'images/grid.png', tooltip: '初始化',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = RouterStore.getAt(rowIndex);
			                                     var init_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "正在初始化路由器信息，请等待..."});
			                            	       	routerid = OBJ.get('id');
			                            	       	init_MyMask.show();
															Ext.Ajax.request({ 
															    url : servletPath,  
															    method : 'POST',
								                                params:{
								                                actionname:'org.rm.action.RouterAction',
								                                actioncmd:'init',
								               		            routerid:routerid
								                                },
								                                callback : function(options, success, response) {
								                	       				init_MyMask.hide();
								                	       				if (success){
								                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
								                	       					if (JSONOBJ.success==true){
								                	       						RouterAdd_Window.hide();
								                	       						Ext.MessageBox.alert('提示','路由器信息初始化成功');
								                	       						RouterStore.reload();
								                	       					}else{
								                	       						Ext.MessageBox.alert('错误','路由器信息初始化失败');
								                	       					}
								                	       				}else{
								                	       					Ext.MessageBox.alert('错误','数据库操作超时');
								                	       				}
								                	       			}})
														}
			                            	     },'','','','','','','','','','','','','','','',
										{icon   : 'images/edit.png', tooltip: '修改',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = RouterStore.getAt(rowIndex);
			                            	       	RouterEditform(OBJ,RouterStore);
			                            	       
			                            	     }},'','','','','','','','','','','','','','','',
			                            	{icon:'images/delete.png', tooltip: '删除',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = RouterStore.getAt(rowIndex);
			                            	       if (confirm()==true){
			                            	       	var OfferGrid_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
			                            	       		Ext.Ajax.request({url:servletPath , mthod:'POST',
			                            	       			params:{
			                            	       				actionname:'org.rm.action.RouterAction',
								                                actioncmd:'deletebyid',
								               		            fguid : OBJ.get('fguid')
			                            	       			},
			                            	       			callback:function(options,success,response){
			                            	       				OfferGrid_MyMask.hide();
			                            	       				if (success){
			                            	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
			                            	       					if (JSONOBJ.success==true){
			                            	       						//OfferStore.remove(OBJ);
			                            	       					}else{
			                            	       						//alert(Error.msg[JSONOBJ.msg]);
			                            	       					}
			                            	       				}else{
			                            	       					// alert(Error.msg['10000']);
			                            	       				}
			                            	       			}
			                            	       			});
			                            	       }
			                            	     }}
			                            	]
								}
							],
							tbar : [{text : '添加路由器',tooltip : '添加路由器',iconCls : 'add',handler : function() {
								     RouterAddform(RouterStore);
									 }} ]//,
							//bbar: Offerbbar
						})
            });
        }
        win.show();
        RouterStore.load();
    }
});

MyDesktop.ContextGridWindow = Ext.extend(Ext.app.Module, {
    id:'context-grid-win',
    init : function(){
        this.launcher = {
            text: 'Context Window',
            iconCls:'icon-grid',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('context-grid-win');
        if(!win){
            win = desktop.createWindow({
                id: 'context-grid-inwin',
                title:'Context Window',
                width:650,
                height:480,
                iconCls: 'icon-grid',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
				loadMask:{msg:'正在加载数据,请稍候.....'},
				closeable:true,
                layout: 'fit',
                items:	new Ext.grid.GridPanel({
							//header:true, region:'center', split:true, loadMask:true,
							//border : true,stripeRows : false, enableHdMenu:false,
							store : ContextStore,
							columns: [
								{header:'序号',dataIndex:'id',width:30,hidden:false},
								{header:'路由ID',dataIndex:'deviceid',sortable:true,width:77,hidden:true},
								{header:'ContextName',dataIndex:'contextname',sortable:true,width:77},
								{header:'ContextID',dataIndex:'contextid',sortable:true,width:77},
								{header:'VPN-RD',dataIndex:'vpnrd',sortable:true,width:77},
								{header:'Description',dataIndex:'description',sortable:true,width:77},
								{header:'操作',width:100, xtype:'actioncolumn',
									items:[{icon   : 'images/grid.png', tooltip: '查看主机',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = ContextStore.getAt(rowIndex);
			                            	       	contextid = OBJ.get('id');
			                            	       	HostShowform(OBJ);
			                            	     }},'','','','','','','','','','','','','','','',
										{icon   : 'images/edit.png', tooltip: '修改',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = ContextStore.getAt(rowIndex);
			                                     if (routerid == ''){
													Ext.MessageBox.alert('警告','请选择路由器后再执行添加操作');
													return ;
												}
			                            	       	ContextEditform(OBJ,ContextStore);
			                            	       
			                            	     }},'','','','','','','','','','','','','','','',
			                            	{icon:'images/delete.png', tooltip: '删除',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = ContextStore.getAt(rowIndex);
			                            	       if (confirm()==true){
			                            	       	var OfferGrid_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
			                            	       		Ext.Ajax.request({url:servletPath , mthod:'POST',
			                            	       			params:{
			                            	       				actionname:'org.rm.action.RouterAction',
								                                actioncmd:'deletebyid',
								               		            fguid : OBJ.get('fguid')
			                            	       			},
			                            	       			callback:function(options,success,response){
			                            	       				OfferGrid_MyMask.hide();
			                            	       				if (success){
			                            	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
			                            	       					if (JSONOBJ.success==true){
			                            	       						//OfferStore.remove(OBJ);
			                            	       					}else{
			                            	       						//alert(Error.msg[JSONOBJ.msg]);
			                            	       					}
			                            	       				}else{
			                            	       					// alert(Error.msg['10000']);
			                            	       				}
			                            	       			}
			                            	       			});
			                            	       }
			                            	     }}
			                            	]
								}
							],
							tbar : [
							'选择路由:',contextCombo
							,{
								text : '查询',
								iconCls : 'icon-find',
								handler : function() {
									ContextStore.removeAll();
									ContextStore.baseParams = {
										actionname:'org.rm.action.ContextAction',
 										actioncmd:'querybyrouter',
 										routerid:routerid
 									};
									ContextStore.load();
								}
							},{text : '添加Context',tooltip : '添加Context',iconCls : 'add',
									handler : function() {
										if (routerid == ''){
											Ext.MessageBox.alert('警告','请选择路由器后再执行添加操作');
											return ;
										}
								     ContextAddform(ContextStore);
									}
							}
							]//,
							//bbar: Offerbbar
						})
            });
        }
        win.show();
        RouterStore.load();
    }
});

//人员管理

MyDesktop.PeopleGridWindow = Ext.extend(Ext.app.Module, {
    id:'people-grid-win',
    init : function(){
        this.launcher = {
            text: 'People Window',
            iconCls:'icon-grid',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('people-grid-win');
        if(!win){
            win = desktop.createWindow({
                id: 'people-grid-inwin',
                title:'People Window',
                width:650,
                height:480,
                iconCls: 'icon-grid',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
				loadMask:{msg:'正在加载数据,请稍候.....'},
				closeable:true,
                layout: 'fit',
                items:	new Ext.grid.GridPanel({
							store : PeopleStore,
							columns: [
								{header:'序号',dataIndex:'id',width:30,hidden:false},
								{header:'姓名',dataIndex:'name',sortable:true,width:77},
								{header:'手机号码',dataIndex:'mobile',sortable:true,width:77},
								{header:'邮箱地址',dataIndex:'email',sortable:true,width:77},
								{header:'设备Id',dataIndex:'device',sortable:true,width:77},
								{header:'操作',width:100, xtype:'actioncolumn',
									items:[{icon   : 'images/grid.png', tooltip: '查看主机',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = PeopleStore.getAt(rowIndex);
			                            	       	contextid = OBJ.get('id');
			                            	       	HostShowform(OBJ);
			                            	     }},'','','','','','','','','','','','','','','',
										{icon   : 'images/edit.png', tooltip: '修改',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = PeopleStore.getAt(rowIndex);
			                                     if (routerid == ''){
													Ext.MessageBox.alert('警告','请选择路由器后再执行添加操作');
													return ;
												}
			                            	       	ContextEditform(OBJ,ContextStore);
			                            	       
			                            	     }},'','','','','','','','','','','','','','','',
			                            	{icon:'images/delete.png', tooltip: '删除',  
			                                    handler: function(grid, rowIndex, colIndex) {
			                                     var OBJ = PeopleStore.getAt(rowIndex);
			                            	       if (confirm()==true){
			                            	       	var OfferGrid_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
			                            	       		Ext.Ajax.request({url:servletPath , mthod:'POST',
			                            	       			params:{
			                            	       				actionname:'org.rm.action.RouterAction',
								                                actioncmd:'deletebyid',
								               		            fguid : OBJ.get('fguid')
			                            	       			},
			                            	       			callback:function(options,success,response){
			                            	       				OfferGrid_MyMask.hide();
			                            	       				if (success){
			                            	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
			                            	       					if (JSONOBJ.success==true){
			                            	       						//OfferStore.remove(OBJ);
			                            	       					}else{
			                            	       						//alert(Error.msg[JSONOBJ.msg]);
			                            	       					}
			                            	       				}else{
			                            	       					// alert(Error.msg['10000']);
			                            	       				}
			                            	       			}
			                            	       			});
			                            	       }
			                            	     }}
			                            	]
								}
							],
							tbar : [{text : '添加管理人员',tooltip : '添加管理人员',iconCls : 'add',
									handler : function() {
								     ContextAddform(PeopleStore);
									}
							}
							]//,
							//bbar: Offerbbar
						})
            });
        }
        win.show();
        PeopleStore.load();
    }
});


//路由信息Store
var RouterStore = new Ext.data.JsonStore({
 				url:"../servlet",baseParams:{actionname:'org.rm.action.RouterAction',actioncmd:'query'},
 				listeners:{
 					clear: function(){
 						if ( bbar = RouterGrid.getBottomToolbar()){
 							bbar.updateInfo();
							bbar.next.setDisabled(true);
							bbar.prev.setDisabled(true);
							bbar.first.setDisabled(true);
							bbar.last.setDisabled(true);
							bbar.refresh.setDisabled(true);
 						}
 				}},
 				totalProperty:'result',root:'meta_device',
 				fields:[{name:'id', type:'string'},
 						{name:'devicename', type:'string'},
 						{name:'devicetype', type:'string'},
 						{name:'hostname', type:'string'},
 						{name:'deviceip', type:'string'},
 						{name:'loginway', type:'string'},
 						{name:'loginname', type:'string'},
 						{name:'password', type:'string'},
 						{name:'status', type:'string'},
 						{name:'lastonline', type:'string'},
 						{name:'devicepath', type:'string'},
 						{name:'deviceparent', type:'string'},
 						{name:'deviceinfo', type:'string'},
 						{name:'devicepurpose', type:'string'},
 						{name:'location', type:'string'},
 						{name:'devicedep', type:'string'},
 						{name:'onlinehosts', type:'string'}
 						]}
 						);
 						
//Context信息Store
//路由信息Store
var ContextStore = new Ext.data.JsonStore({
 				url:"../servlet",
 				baseParams:{
 				actionname:'org.rm.action.ContextAction',
 				actioncmd:'querybyrouter',
 				routerid:routerid
 				},

 				totalProperty:'result',root:'meta_context',
 				fields:[{name:'id', type:'string'},
 						{name:'deviceid', type:'string'},
 						{name:'contextname', type:'string'},
 						{name:'contextid', type:'string'},
 						{name:'vpnrd', type:'string'},
 						{name:'description', type:'string'}
 						]}
);

//路由器信息修改
var RouterEditform = function(RecordOBJ,store){
	     var RouterEditform_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
	     var RouterEdit_Form=new Ext.form.FormPanel({
				 	baseCls: 'x-plain',url:'',layout:'absolute',defaultType: 'textfield',
                    items: [
                            {x: 80,y: itemsheight*1,name: 'id', xtype:'textfield',value :RecordOBJ.get('id'), maxLength:10,minLength:1, allowBlank:true, anchor:'90%',hidden:true },
                            {x: 0, y: itemsheight*1,xtype:'label', text: '主机名:' },
                            {x: 80,y: itemsheight*1,name: 'hostname', xtype:'textfield',value :RecordOBJ.get('hostname'),format:'Y-m-d',maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*2,xtype:'label', text: 'IP地址:' },
                            {x: 80,y: itemsheight*2,name: 'deviceip', xtype:'textfield',value :RecordOBJ.get('deviceip'),maxLength:15,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*3,xtype:'label', text: '登录名:' },
                            {x: 80,y: itemsheight*3,name: 'loginname', xtype:'textfield',value :RecordOBJ.get('loginname'),maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*4,xtype:'label', text: '密码:' },
                            {x: 80,y: itemsheight*4,name: 'password', xtype:'textfield',value :RecordOBJ.get('password'),maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*5,xtype:'label', text: '状态:' },
                            {x: 80, y: itemsheight*5,name: 'status', xtype: 'combo',value: RecordOBJ.get('status'),fieldLabel: '状态',maxLength:64,minLength:1, anchor:'90%',mode: 'local',editable: true, triggerAction: 'all', valueField: 'value', displayField: 'text',
                            store: new Ext.data.SimpleStore({fields: ['value', 'text'], data: [['0', '离线'],['1', '在线']]})}, 
                            {x: 0, y: itemsheight*6,xtype:'label', text: '最后在线时间:' },
                            {x: 80,y: itemsheight*6,name: 'lastonline', xtype:'datetimefield',format: 'Y-m-d H:i:s', value :RecordOBJ.get('lastonline'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*7,xtype:'label', text: '设备归属:' },
                            {x: 80,y: itemsheight*7,name: 'devicedep', xtype:'textfield', value :RecordOBJ.get('devicedep'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*8,xtype:'label', text: '设备信息:' },
                            {x: 80,y: itemsheight*8,name: 'deviceinfo', xtype:'textfield', value :RecordOBJ.get('deviceinfo'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*9,xtype:'label', text: '设备维护人:' },
                            {x: 80,y: itemsheight*9,name: 'devicepurpose', xtype:'textfield', value :RecordOBJ.get('devicepurpose'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*10,xtype:'label', text: '设备位置:' },
                            {x: 80,y: itemsheight*10,name: 'location', xtype:'textfield', value :RecordOBJ.get('location'), allowBlank:true, anchor:'90%' }
                    ]});
                    
          var RouterEdit_Window=new Ext.Window({ title: '修改路由器信息',modal : true,plain: true,
                    width : 350,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : RouterEdit_Form,
					buttons : [{
						text : '保存',
						handler : function() {// 验证
							if (RouterEdit_Form.getForm().isValid()) {
							    Ext.Ajax.request({ 
							    url : servletPath,  
							    method : 'POST',
                                params:{
                                actionname:'org.rm.action.RouterAction',
                                actioncmd:'updatebyid',
               		            id : RouterEdit_Form.getForm().findField('id').getValue(),
               		            hostname : RouterEdit_Form.getForm().findField('hostname').getValue(),
               		            deviceip : RouterEdit_Form.getForm().findField('deviceip').getValue(),
               		            loginname : RouterEdit_Form.getForm().findField('loginname').getValue(),
               		            password : RouterEdit_Form.getForm().findField('password').getValue(),
               		            status : RouterEdit_Form.getForm().findField('status').getValue(),
               		            lastonline : RouterEdit_Form.getForm().findField('lastonline').getValue(),
               		            devicedep : RouterEdit_Form.getForm().findField('devicedep').getValue(),
               		            deviceinfo : RouterEdit_Form.getForm().findField('deviceinfo').getValue(),
               		            devicepurpose : RouterEdit_Form.getForm().findField('devicepurpose').getValue(),
               		            location : RouterEdit_Form.getForm().findField('location').getValue()
                                },
                                callback : function(options, success, response) {
                	       				RouterEditform_MyMask.hide();
                	       				if (success){
                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
                	       					if (JSONOBJ.success==true){
                	       						RouterEdit_Window.hide();
                	       						Ext.MessageBox.alert('提示','路由器信息更新成功');
                	       							RouterStore.reload();
                	       					}else{
                	       						Ext.MessageBox.alert('错误','路由器信息更新失败');
                	       					}
                	       				}else{
                	       					Ext.MessageBox.alert('错误','数据库操作超时');
                	       				}
                	       			}})}
           				}},{ text: '取消',handler:function(){ RouterEdit_Window.hide(); } }
					]});    
           RouterEdit_Window.show();
           };
//路由器信息增加
var RouterAddform = function(RecordOBJ,store){
	     var RouterAddform_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
	     var RouterAdd_Form=new Ext.form.FormPanel({
				 	baseCls: 'x-plain',url:'',layout:'absolute',defaultType: 'textfield',
                    items: [
                            {x: 80,y: itemsheight*1,name: 'id', xtype:'textfield', maxLength:10,minLength:1, allowBlank:true, anchor:'90%',hidden:true },
                            {x: 0, y: itemsheight*1,xtype:'label', text: '主机名:' },
                            {x: 80,y: itemsheight*1,name: 'hostname', xtype:'textfield',maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*2,xtype:'label', text: 'IP地址:' },
                            {x: 80,y: itemsheight*2,name: 'deviceip', xtype:'textfield',maxLength:15,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*3,xtype:'label', text: '登录名:' },
                            {x: 80,y: itemsheight*3,name: 'loginname', xtype:'textfield',maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*4,xtype:'label', text: '密码:' },
                            {x: 80,y: itemsheight*4,name: 'password', xtype:'textfield',maxLength:10,minLength:1, allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*5,xtype:'label', text: '状态:' },
                            {x: 80, y: itemsheight*5,name: 'status', xtype: 'combo',fieldLabel: '状态',maxLength:64,minLength:1, anchor:'90%',mode: 'local',editable: true, triggerAction: 'all', valueField: 'value', displayField: 'text',
                            store: new Ext.data.SimpleStore({fields: ['value', 'text'], data: [['0', '离线'],['1', '在线']]})}, 
                            {x: 0, y: itemsheight*6,xtype:'label', text: '最后在线时间:' },
                            {x: 80,y: itemsheight*6,name: 'lastonline', xtype:'datetimefield',format: 'Y-m-d H:i:s', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*7,xtype:'label', text: '设备归属:' },
                            {x: 80,y: itemsheight*7,name: 'devicedep', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*8,xtype:'label', text: '设备信息:' },
                            {x: 80,y: itemsheight*8,name: 'deviceinfo', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*9,xtype:'label', text: '设备维护人:' },
                            {x: 80,y: itemsheight*9,name: 'devicepurpose', xtype:'textfield',  allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*10,xtype:'label', text: '设备位置:' },
                            {x: 80,y: itemsheight*10,name: 'location', xtype:'textfield', allowBlank:true, anchor:'90%' }
                    ]});
                    
          var RouterAdd_Window=new Ext.Window({ title: '添加路由器信息',modal : true,plain: true,
                    width : 350,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : RouterAdd_Form,
					buttons : [{
						text : '保存',
						handler : function() {// 验证
							if (RouterAdd_Form.getForm().isValid()) {
							    Ext.Ajax.request({ 
							    url : servletPath,  
							    method : 'POST',
                                params:{
                                actionname:'org.rm.action.RouterAction',
                                actioncmd:'insert',
               		            hostname : RouterAdd_Form.getForm().findField('hostname').getValue(),
               		            deviceip : RouterAdd_Form.getForm().findField('deviceip').getValue(),
               		            loginname : RouterAdd_Form.getForm().findField('loginname').getValue(),
               		            password : RouterAdd_Form.getForm().findField('password').getValue(),
               		            status : RouterAdd_Form.getForm().findField('status').getValue(),
               		            lastonline : RouterAdd_Form.getForm().findField('lastonline').getValue(),
               		            devicedep : RouterAdd_Form.getForm().findField('devicedep').getValue(),
               		            deviceinfo : RouterAdd_Form.getForm().findField('id').getValue(),
               		            devicepurpose : RouterAdd_Form.getForm().findField('devicepurpose').getValue(),
               		            location : RouterAdd_Form.getForm().findField('location').getValue(),
               		            deviceinfo : RouterAdd_Form.getForm().findField('deviceinfo').getValue()
                                },
                                callback : function(options, success, response) {
                	       				RouterAddform_MyMask.hide();
                	       				if (success){
                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
                	       					if (JSONOBJ.success==true){
                	       						RouterAdd_Window.hide();
                	       						Ext.MessageBox.alert('提示','路由器信息添加成功');
                	       						RouterStore.reload();
                	       					}else{
                	       						Ext.MessageBox.alert('错误','路由器信息添加失败');
                	       					}
                	       				}else{
                	       					Ext.MessageBox.alert('错误','数据库操作超时');
                	       				}
                	       			}})}
           				}},{ text: '取消',handler:function(){ RouterAdd_Window.hide(); } }
					]});    
           RouterAdd_Window.show();
           };

//context下拉树
var contextCombo = new Ext.form.ComboBox({
                fieldLabel: 'Management Level',
                name:'contextCombo',
                forceSelection: true,
                listWidth: 150,
                store: RouterStore,
                valueField:'id',
                displayField:'deviceip',
                typeAhead: true,
                mode: 'local',
                triggerAction: 'all',
                selectOnFocus:true,
                allowBlank:false,
                 listeners:{
         			select  :function(combo,record,index){
              		routerid = combo.value;
        		 }
     }      
});

//Context信息修改
var ContextEditform = function(RecordOBJ,store){
	     var ContextEditform_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
	     var ContextEdit_Form=new Ext.form.FormPanel({
				 	baseCls: 'x-plain',url:'',layout:'absolute',defaultType: 'textfield',
                    items: [
                            {x: 80,y: itemsheight*1,name: 'id', xtype:'textfield',value :RecordOBJ.get('id'), maxLength:10,minLength:1, allowBlank:true, anchor:'90%',hidden:true },
                            {x: 0, y: itemsheight*1,xtype:'label', text: 'ContextName:' },
                            {x: 80,y: itemsheight*1,name: 'contextname', xtype:'textfield',value :RecordOBJ.get('contextname'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*2,xtype:'label', text: 'ContextId:' },
                            {x: 80,y: itemsheight*2,name: 'contextid', xtype:'textfield',value :RecordOBJ.get('contextid'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*3,xtype:'label', text: 'VPN-RD' },
                            {x: 80,y: itemsheight*3,name: 'vpnrd', xtype:'textfield',value :RecordOBJ.get('vpnrd'), allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*4,xtype:'label', text: 'Description:' },
                            {x: 80,y: itemsheight*4,name: 'description', xtype:'textfield',value :RecordOBJ.get('description'), allowBlank:true, anchor:'90%' }
                    ]});
                    
          var ContextEdit_Window=new Ext.Window({ title: '修改Context信息',modal : true,plain: true,
                    width : 350,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : ContextEdit_Form,
					buttons : [{
						text : '保存',
						handler : function() {// 验证
							if (ContextEdit_Form.getForm().isValid()) {
							    Ext.Ajax.request({ 
							    url : servletPath,  
							    method : 'POST',
                                params:{
                                actionname:'org.rm.action.ContextAction',
                                actioncmd:'updatebyid',
               		            id : ContextEdit_Form.getForm().findField('id').getValue(),
               		            contextname : ContextEdit_Form.getForm().findField('contextname').getValue(),
               		            contextid : ContextEdit_Form.getForm().findField('contextid').getValue(),
               		            vpnrd : ContextEdit_Form.getForm().findField('vpnrd').getValue(),
               		            description : ContextEdit_Form.getForm().findField('description').getValue(),
               		            deviceid:routerid
                                },
                                callback : function(options, success, response) {
                	       				ContextEditform_MyMask.hide();
                	       				if (success){
                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
                	       					if (JSONOBJ.success==true){
                	       							ContextEdit_Window.hide();
                	       							Ext.MessageBox.alert('提示','Context信息更新成功');
                	       							ContextStore.reload();
                	       					}else{
                	       						Ext.MessageBox.alert('错误','Context信息更新失败');
                	       					}
                	       				}else{
                	       					Ext.MessageBox.alert('错误','数据库操作超时');
                	       				}
                	       			}})}
           				}},{ text: '取消',handler:function(){ ContextEdit_Window.hide(); } }
					]});    
           ContextEdit_Window.show();
           };
//路由器信息修改
var ContextAddform = function(RecordOBJ,store){
	     var ContextAddform_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
	     var ContextAdd_Form=new Ext.form.FormPanel({
				 	baseCls: 'x-plain',url:'',layout:'absolute',defaultType: 'textfield',
                    items: [
                            {x: 0, y: itemsheight*1,xtype:'label', text: 'ContextName:' },
                            {x: 80,y: itemsheight*1,name: 'contextname', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*2,xtype:'label', text: 'ContextId:' },
                            {x: 80,y: itemsheight*2,name: 'contextid', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*3,xtype:'label', text: 'VPN-RD' },
                            {x: 80,y: itemsheight*3,name: 'vpnrd', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*4,xtype:'label', text: 'Description:' },
                            {x: 80,y: itemsheight*4,name: 'description', xtype:'textfield', allowBlank:true, anchor:'90%' }
                    ]});
                    
          var ContextAdd_Window=new Ext.Window({ title: '添加Context信息',modal : true,plain: true,
                    width : 350,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : ContextAdd_Form,
					buttons : [{
						text : '保存',
						handler : function() {// 验证
							if (ContextAdd_Form.getForm().isValid()) {
							    Ext.Ajax.request({ 
							    url : servletPath,  
							    method : 'POST',
                                params:{
                                actionname:'org.rm.action.ContextAction',
                                actioncmd:'insert',
               		            contextname : ContextAdd_Form.getForm().findField('contextname').getValue(),
               		            contextid : ContextAdd_Form.getForm().findField('contextid').getValue(),
               		            vpnrd : ContextAdd_Form.getForm().findField('vpnrd').getValue(),
               		            description : ContextAdd_Form.getForm().findField('description').getValue(),
               		            deviceid:routerid
                                },
                                callback : function(options, success, response) {
                	       				ContextAddform_MyMask.hide();
                	       				if (success){
                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
                	       					if (JSONOBJ.success==true){
                	       						ContextAdd_Window.hide();
                	       						Ext.MessageBox.alert('提示','Context信息添加成功');
                	       						ContextStore.reload();
                	       					}else{
                	       						Ext.MessageBox.alert('错误','Context信息添加失败');
                	       					}
                	       				}else{
                	       					Ext.MessageBox.alert('错误','数据库操作超时');
                	       				}
                	       			}})}
           				}},{ text: '取消',handler:function(){ ContextAdd_Window.hide(); } }
					]});    
           ContextAdd_Window.show();
           };

//查看Context下的主机
var HostShowform = function(OBJ,HostStore){
	     var HostShow_Grid=new Ext.grid.GridPanel({
							//header:true, region:'center', split:true, loadMask:true,
							//border : true,stripeRows : false, enableHdMenu:false,
							store : HostStore,
							columns: [
								{header:'Host Id',dataIndex:'id',width:30,hidden:false},
								{header:'Context Id',dataIndex:'contextid',sortable:true,width:77},
								{header:'IP Address',dataIndex:'ipaddr',sortable:true,width:77},
								{header:'Mac Address',dataIndex:'macaddr',sortable:true,width:77},
								{header:'TTL',dataIndex:'ttl',sortable:true,width:77},
								{header:'Type',dataIndex:'type',sortable:true,width:37},
								{header:'Circuit',dataIndex:'circuit',sortable:true,width:157},
								{header:'Status',dataIndex:'status',sortable:true,width:77}
							],
							tbar : [{text : '添加主机',tooltip : '添加主机',iconCls : 'add',handler : function() {
								    HostAddform(RouterStore);
									 }} ]//,
							//bbar: Offerbbar
		});
		
		var HostShow_Window=new Ext.Window({ title: '添加主机信息',modal : true,plain: true,
                    width : 750,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : HostShow_Grid
		});
		HostShow_Window.show();
		HostStore.reload();
};

var HostStore = new Ext.data.JsonStore({
 				url:"../servlet",
 				baseParams:{
	 				actionname:'org.rm.action.HostAction',
	 				actioncmd:'querybycontext',
	 				contextid:contextid
 				},
 				totalProperty:'result',root:'d_host',
 				fields:[{name:'id', type:'string'},
 						{name:'contextid', type:'string'},
 						{name:'ipaddr', type:'string'},
 						{name:'macaddr', type:'string'},
 						{name:'ttl', type:'string'},
 						{name:'type', type:'string'},
 						{name:'circuit', type:'string'},
 						{name:'status', type:'string'}
 						]}
);
//增加主机
var HostAddform = function(OBJ,HostStore){
	     var HostAddform_MyMask = new Ext.LoadMask(Ext.getBody(), { msg : "请等待..."});
	     var HostAdd_Form=new Ext.form.FormPanel({
				 	baseCls: 'x-plain',url:'',layout:'absolute',defaultType: 'textfield',
                    items: [
                            {x: 0, y: itemsheight*1,xtype:'label', text: 'IpAddr:' },
                            {x: 80,y: itemsheight*1,name: 'ipaddr', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*2,xtype:'label', text: 'MacAddr:' },
                            {x: 80,y: itemsheight*2,name: 'macaddr', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*3,xtype:'label', text: 'TTL' },
                            {x: 80,y: itemsheight*3,name: 'ttl', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*4,xtype:'label', text: 'Type:' },
                            {x: 80,y: itemsheight*4,name: 'type', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*5,xtype:'label', text: 'Circuit:' },
                            {x: 80,y: itemsheight*5,name: 'circuit', xtype:'textfield', allowBlank:true, anchor:'90%' },
                            {x: 0, y: itemsheight*6,xtype:'label', text: 'Status:' },
                            {x: 80,y: itemsheight*6,name: 'status', xtype:'textfield', allowBlank:true, anchor:'90%' }
                    ]});
                    
          var HostAdd_Window=new Ext.Window({ title: '添加Host信息',modal : true,plain: true,
                    width : 350,height : 350,resizable : false,layout : 'fit',closeAction:'hide',
					bodyStyle : 'padding:5px;',buttonAlign : 'center',items : HostAdd_Form,
					buttons : [{
						text : '保存',
						handler : function() {// 验证
							if (HostAdd_Form.getForm().isValid()) {
							    Ext.Ajax.request({ 
							    url : servletPath,  
							    method : 'POST',
                                params:{
                                actionname:'org.rm.action.HostAction',
                                actioncmd:'insert',
               		            ipaddr : HostAdd_Form.getForm().findField('ipaddr').getValue(),
               		            macaddr : HostAdd_Form.getForm().findField('macaddr').getValue(),
               		            ttl : HostAdd_Form.getForm().findField('ttl').getValue(),
               		            type : HostAdd_Form.getForm().findField('type').getValue(),
               		            circuit : HostAdd_Form.getForm().findField('circuit').getValue(),
               		            status : HostAdd_Form.getForm().findField('status').getValue(),
               		            contextid:contextid
                                },
                                callback : function(options, success, response) {
                	       				HostAddform_MyMask.hide();
                	       				if (success){
                	       					var JSONOBJ = Ext.util.JSON.decode(response.responseText);
                	       					if (JSONOBJ.success==true){
                	       						HostAdd_Window.hide();
                	       						Ext.MessageBox.alert('提示','Host信息添加成功');
                	       						HostStore.reload();
                	       					}else{
                	       						Ext.MessageBox.alert('错误','Host信息添加失败');
                	       					}
                	       				}else{
                	       					Ext.MessageBox.alert('错误','数据库操作超时');
                	       				}
                	       			}})}
           				}},{ text: '取消',handler:function(){ HostAdd_Window.hide(); } }
					]});    
           HostAdd_Window.show();
}

//人员管理
var PeopleStore = new Ext.data.JsonStore({
 				url:"../servlet",
 				baseParams:{
	 				actionname:'org.rm.action.PeopleAction',
	 				actioncmd:'query'
 				},
 				totalProperty:'result',root:'meta_people',
 				fields:[{name:'id', type:'string'},
 						{name:'name', type:'string'},
 						{name:'mobile', type:'string'},
 						{name:'email', type:'string'},
 						{name:'device', type:'string'}]
});

