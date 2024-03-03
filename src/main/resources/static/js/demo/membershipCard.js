let $ = jQuery;
let reqUrl;
let subFlag = true;
$(function () {
    //根据窗口调整表格高度
    $(window).resize(function() {
        $('#exampleTableEvents').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })

    $('#exampleTableEvents').bootstrapTable({
        url: "/membershipCard/pageMembershipCard",
        contentType: "application/x-www-form-urlencoded",
        dataType:"json",
        method:"post",
        height:tableHeight(),//高度调整
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,//是否分页
        // dataField: "res",//获取数据的别名，先省略，则为你返回的
        search: true,
        sidePagination: 'server',//在服务器分页
        queryParamsType:'limit',
        queryParams:queryParams,
        pageSize: 20,//单页记录数
        showRefresh: true,//刷新按钮
        clickToSelect: true,//是否启用点击选中行
        showToggle: true,
        showColumns: true,
        iconSize: 'outline',
        toolbar: '#exampleTableEventsToolbar',
        icons: {
            refresh: 'glyphicon-repeat',
            toggle: 'glyphicon-list-alt',
            columns: 'glyphicon-list'
        },
        columns: [
            {
                title: 'ID',
                field: 'mid',
                visible: false
            },{
                title: '会员卡号',
                field: 'mno'
            },
            {
                title: '姓名',
                field: 'name',
                sortable: false
            },
            {
                title: '手机号',
                field: 'phone',
                sortable: false
            },
            {
                title: '余额',
                field: 'balance',
                sortable: false,
                formatter:function (value){
                    return '￥'+value;
                }
            },
            {
                title: '办卡时间',
                field: 'applyCardTime',
                sortable: false
            },
            {
                title: "是否启用",
                field: 'isEnable',
                formatter:function (value, row, index){
                    return value == 1?'是':'否'
                }
            },
            {
                title: '操作',
                field: 'Button',
                align: 'center',
                events: operateEvents,//事件
                formatter: AddFunctionAlty//添加按钮
            }
        ],
        locale: 'zh-CN',//中文支持,
    });
    //点击搜索
    $('#btn-search').click(function () {
        $('#exampleTableEvents').bootstrapTable('refresh', {url: '/membershipCard/pageMembershipCard'});//url为后台action
    })
    //点击保存

    $('#btn-submit').click(function () {
        if(!subFlag){
            return;
        }
        subFlag = false;
        let mid = $("#mid").val();
        let mno = $("#mno").val();
        let name = $("#name").val();
        let balance = $("#balance").val();
        let phone = $("#phone").val();
        req(reqUrl,{mid:mid,mno:mno,name:name,balance:balance,phone:phone});
    })

    //更新
    $('#btn-submit1').click(function () {
        if(!subFlag){
            return;
        }
        subFlag = false;
        let mid = $("#mid1").val();
        let mno = $("#mno1").text();
        let name = $("#name1").text();
        let balance = $("#balance1").val();
        let phone = $("#phone1").text();
        req(reqUrl,{mid:mid,mno:mno,name:name,balance:balance,phone:phone});
    })
    //添加
    $("#add").click(function () {
        $("#title").html("办理会员卡");
        reqUrl = "/membershipCard/addMembershipCard";
        //清除表单数据
        document.getElementById("myform").reset();
    })
    //显示隐藏扫码绑定二维码
    $("#bar").click(function () {
        let codeBtn = $("#codebtn").css("display");
        if ('none'== codeBtn){
            $("#barcode").hide();
            $("#codebtn").show();
        }else{
            $("#codebtn").hide();
            $("#barcode").show();
        }
    })


})

function req(url,obj) {
    $.ajax(url,{
        method:"post",
        headers:{
            contentType: "application/x-www-form-urlencoded",
        },
        data:obj,
        success:(res)=>{
            subFlag = true;
            if(res.status=="success"){
                swal("操作成功", res.msg, "success");
            }else{
                swal("操作失败！", res.msg, "error");
            }
            setTimeout(function () {
                swal.close()
            },3000)
            //清除表单数据
            document.getElementById("myform").reset();
            document.getElementById("myform1").reset();
            //隐藏模板
            $("#myModal5").modal("hide");
            $("#myModal6").modal("hide");
            //刷新表格
            $('#exampleTableEvents').bootstrapTable('refresh', {url: '/membershipCard/pageMembershipCard'});
        }
    })

}

function tableHeight() {
    return $(window).height() - 150;
}


//列表行‘操作’按钮
function AddFunctionAlty(value, row, index) {
    return '<button id="edit" type="button" class="btn btn-default">更新余额</button><button id="del" type="button" class="btn btn-default">删除</button>'

}
//请求服务数据时所传查询参数
function queryParams(params){
    return{
        pageSize: params.limit,
        pageNum:parseInt(params.offset/params.limit)+1,
        name:$("#name_search").val(),
        mno:$('#mno_search').val(),
        phone:$('#phone_search').val(),
        startTime:$('#startTime_search').val(),
        endTime:$('#endTime_search').val()
    }
}
//点击更新按钮事件
window.operateEvents = {
    "click #edit": function (e, value, row, index) {
        console.log(row);
        $("#title1").html("更新卡内余额");
        $("#mid1").val(row.mid);
        $("#mno1").text(row.mno);
        $("#name1").text(row.name);
        $("#balance1").val(row.balance);
        $("#phone1").text(row.phone);
        $("#applyCardTime1").text(row.applyCardTime);
        reqUrl = "membershipCard/updateMembershipCard"
        $("#myModal6").modal("show");
    },
    "click #del": function (e, value, row, index) {

        swal({
            title: "您确定要删除此会员卡："+row.mno +"吗？",
            text: "删除后将无法恢复，请谨慎操作！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            cancelButtonText: "取消",
            confirmButtonText: "删除",
            closeOnConfirm: false
        }, function () {
            del(row);
        });
    }
}
//删除
function del(row) {
    $.ajax("membershipCard/delMembershipCard",{
        headers:{
            contentType: "application/x-www-form-urlencoded",
        },
        method: "post",
        data:{mid:row.mid},
        success(){
            swal("删除成功！", "您已经永久删除了此"+row.mno+"。", "success");
            setTimeout(function () {
                swal.close();
            },1500)
            //刷新表格
            $('#exampleTableEvents').bootstrapTable('refresh', {url: 'membershipCard/pageMembershipCard'});
        }
    });
}

