(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-f3d8bde0"],{1781:function(t,e,a){t.exports=a.p+"static/img/shebei.ecdc5a3e.png"},2546:function(t,e,a){"use strict";var i=a("967e"),s=a.n(i);s.a},"967e":function(t,e,a){},b690:function(t,e,a){"use strict";a.r(e);var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-card",{ref:"content",staticClass:"card-list"},[a("a-list",{attrs:{grid:{gutter:24,lg:4,md:3,sm:2,xs:1},"data-source":t.dataSource},scopedSlots:t._u([{key:"renderItem",fn:function(e){return a("a-list-item",{},[null===e?[a("a-button",{staticClass:"new-btn",attrs:{type:"dashed"},on:{click:t.handleCreate}},[a("a-icon",{attrs:{type:"plus"}}),t._v("\n          新增设备\n        ")],1)]:[a("a-card",{attrs:{hoverable:!0}},[a("a-card-meta",[a("div",{staticStyle:{"margin-left":"10px","margin-bottom":"3px"},attrs:{slot:"title"},slot:"title"},[t._v("\n              "+t._s(e.deviceInfo)+"\n              "),a("a-popconfirm",{attrs:{title:"Are you sure delete this task?","ok-text":"Yes","cancel-text":"No"},on:{confirm:t.confirm,cancel:t.cancel}},[a("a",{staticStyle:{float:"right"}},[a("a-icon",{attrs:{type:"delete"}})],1)])],1),t._v(" "),a("div",{staticStyle:{float:"right","margin-left":"10px","margin-bottom":"3px"},attrs:{slot:"description"},slot:"description"},[a("a-button",{attrs:{shape:"circle"},on:{click:function(a){return t.handleUpdate(e)}}},[a("a-icon",{attrs:{type:"edit"}})],1)],1),t._v(" "),a("a-avatar",{staticClass:"card-avatar",attrs:{slot:"avatar",src:t.imgSrc,size:"large"},slot:"avatar"}),t._v(" "),a("div",{staticClass:"meta-content",attrs:{slot:"description"},slot:"description"},[t._v(t._s(e.deviceTag))])],1),t._v(" "),a("template",{staticClass:"ant-card-actions",slot:"actions"},[a("a",{on:{click:function(a){return t.statisticJump(e)}}},[t._v("统计信息")]),t._v(" "),a("a",{on:{click:function(e){t.dialogFormVisible=!0}}},[t._v("流量监测")]),t._v(" "),a("a",{on:{click:function(a){return t.handleJump(e)}}},[t._v("策略配置")])])],2)]],2)}}])}),t._v(" "),a("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[a("el-form",{ref:"dataForm",staticStyle:{"margin-left":"50px"},attrs:{"status-icon":"",model:t.temp,"label-position":"left","label-width":"100px"}},[a("el-form-item",{attrs:{label:"设备名称",prop:"deviceName"}},[a("el-input",{staticStyle:{width:"50%"},model:{value:t.temp.deviceInfo,callback:function(e){t.$set(t.temp,"deviceInfo",e)},expression:"temp.deviceInfo"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"设备MAC",prop:"deviceMac"}},[a("el-input",{staticStyle:{width:"50%"},model:{value:t.temp.deviceMac,callback:function(e){t.$set(t.temp,"deviceMac",e)},expression:"temp.deviceMac"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"设备IP",prop:"deviceTag"}},[a("el-input",{staticStyle:{width:"50%"},model:{value:t.temp.deviceTag,callback:function(e){t.$set(t.temp,"deviceTag",e)},expression:"temp.deviceTag"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"设备类型",prop:"deviceType"}},[a("el-select",{staticClass:"filter-item",staticStyle:{width:"50%"},attrs:{placeholder:"协议类型","default-first-option":""},model:{value:t.temp.deviceType,callback:function(e){t.$set(t.temp,"deviceType",e)},expression:"temp.deviceType"}})],1)],1),t._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:t.cancelClick}},[t._v("\n        取 消\n      ")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){"create"===t.dialogStatus?t.createData():t.updateData()}}},[t._v("\n        确 定\n      ")])],1)],1)],1)},s=[],c=a("aa98"),o=a("1781"),n=a.n(o),l={name:"CardList",data:function(){return{visible:!1,imgSrc:n.a,dataSource:[],temp:{deviceNumber:"",deviceInfo:"",deviceTag:"",deviceMac:""},dialogFormVisible:!1,dialogStatus:"",textMap:{update:"编辑",create:"创建"}}},created:function(){this.getList()},methods:{getList:function(){var t=this,e=this.$store.getters.gplotId;this.listLoading=!0,0===e?(this.dataSource=[],this.dataSource.unshift(null),this.listLoading=!1):(Object(c["b"])(e).then(function(e){t.dataSource=e.data,t.dataSource.unshift(null)}),this.listLoading=!1)},handleJump:function(t){this.$router.push({name:"Page1",params:{id:t.device_id,number:t.deviceNumber,tag:t.deviceTag}})},statisticJump:function(t){this.$router.push({name:"Statistic",params:{id:t.deviceNumber}})},monitorJump:function(t){this.$router.push({name:"SpeedConfig",params:{id:t.deviceNumber}})},resetTemp:function(){this.temp={deviceNumber:"",deviceInfo:"",deviceTag:"",deviceMac:""}},handleCreate:function(){var t=this;this.resetTemp(),this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick(function(){t.$refs["dataForm"].clearValidate()})},createData:function(){var t=this;this.$refs["dataForm"].validate(function(e){e&&(t.dialogFormVisible=!1,t.$notify({title:"成功",message:"创建成功",type:"success",duration:2e3}))})},handleUpdate:function(t){var e=this;this.temp=Object.assign({},t),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick(function(){e.$refs["dataForm"].clearValidate()})},updateData:function(){var t=this;this.$refs["dataForm"].validate(function(e){e&&(t.dialogFormVisible=!1,t.$notify({title:"成功",message:"更新成功",type:"success"}))})},handleDelete:function(t){var e=this.dataSource.indexOf(t);this.tableData.splice(e,1),this.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3})},cancelClick:function(){this.dialogFormVisible=!1,this.$refs["dataForm"].resetFields()},confirm:function(t){console.log(t),this.$message.success("Click on Yes")},cancel:function(t){console.log(t),this.$message.error("Click on No")}}},r=l,d=(a("2546"),a("2877")),u=Object(d["a"])(r,i,s,!1,null,"d14d2d10",null);e["default"]=u.exports}}]);