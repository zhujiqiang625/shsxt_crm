<html>
<head>
<#include "common/common.header.ftl" >
</head>
<body style="margin: 15px;">
<div class="easyui-panel" title="客户基本信息" style="width:700px;height: 100px;padding: 10px">
    <table cellspacing="8px">
        <input type="hidden" id="cusId" name="cusId" value="${customer.id?c}"/>
        <tr>
            <td>客户编号：</td>
            <td><input type="text" id="khno" name="khno" readonly="readonly" value="${customer.khno?if_exists}"/></td>
            <td>客户名称：</td>
            <td><input type="text" id="name" name="name" readonly="readonly" value="${customer.name?default('')}" /></td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>

    </table>
</div>
<br/>
<table id="dg" title="联系人信息管理" style="width:700px;height:250px"
       toolbar="#toolbar" idField="id" rownumbers="true" fitColumns="true" singleSelect="true">
    <thead>
    <tr>
        <th field="id" width="20">编号</th>
        <th field="cusId" hidden="true"></th>
        <th field="linkName" width="60" editor="{type:'validatebox',options:{required:true}}">客户联系人</th>
        <th field

    </tr>
    </thead>
</table>
</body>
</html>