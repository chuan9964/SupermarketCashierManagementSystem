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
        url: "/order/pageOrder",
        contentType: "application/x-www-form-urlencoded",
        dataType:"json",
        method:"post",
        height:tableHeight(),//高度调整
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,//是否分页
        // dataField: "res",//获取数据的别名，先省略，则为你返回的
        sidePagination: 'server',//在服务器分页
        queryParamsType:'limit',
        queryParams:queryParams,
        pageSize: 20,//单页记录数
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
                field: 'oid',
                visible: false
            },
            {
                title: '订单编号',
                field: 'ono',
                sortable: false
            },
            {
                title: '订单价格',
                field: 'price',
                sortable: false,
            },
            {
                title: '创建时间',
                field: 'createTime',
                sortable: false
            },
            {
                title: '操作人',
                field: 'operator',
                sortable: false
            },

            {
                title: '状态',
                field: 'status',
                sortable: false,
                formatter:function (value,item,index){
                    if (value == 1){
                        return '未完成';
                    }else{
                        return '已完成';
                    }
                },
                cellStyle:function (value,item,index){
                    if (value == '未完成'){
                        return {css:{"color":"red"}};
                    }else{
                        return {css:{"color":"green"}};
                    }
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
        $('#exampleTableEvents').bootstrapTable('refresh', {url: '/order/pageOrder'});//url为后台action
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
            //隐藏模板
            $("#myModal5").modal("hide");
            //刷新表格
            $('#exampleTableEvents').bootstrapTable('refresh', {url: '/goods/pageGoods'});
        }
    })

}

function tableHeight() {
    return $(window).height() - 150;
}

//列表行‘操作’按钮
    function AddFunctionAlty(value, row, index) {
        return '<button id="edit" type="button" class="btn btn-default">详情</button>'

    }
//请求服务数据时所传查询参数
    function queryParams(params){
        return{
            pageSize: params.limit,
            pageNum:parseInt(params.offset/params.limit)+1,

            ono:$("#ono_search").val(),
            status:$('#stauts_search').val(),
            minPrice:$('#minPrice_search').val(),
            maxPrice:$('#maxPrice_search').val(),
            startTime:$('#startTime_search').val(),
            endTime:$('#endTime_search').val(),
            operator:$('#operator_search').val()

        }
    }
//点击新增按钮事件
    window.operateEvents = {
        "click #edit": function (e, value, row, index) {
            /*详情*/
            console.log(row);
            $("#title").html("订单详情");
            $("#oid").val(row.oid);
            $("#ono").text(row.ono);
            $("#price").text("￥"+row.price);
            $("#createTime").text(row.createTime);
            $("#operator").text(row.operator);
            let status = row.status==1?'未完成':'已完成';
            $("#status").text(status);
            $.ajax("/order/selectOrderByOId", {
                headers: {
                    contentType: "application/x-www-form-urlencoded",
                },
                data: {"oid":row.oid},
                method: "get",
                success(res) {
                    //渲染数据
                    let list = res.data;
                    console.log(list);
                    if (list) {
                        $("#goodsOrderDetail").html("");
                        let total = 0;
                        for (let vo of list) {
                            total += vo.number * vo.price;
                            let el = "<tr code=" + vo.code + " >" +
                                "<th data-field='code' style='color: #9c9c9c'>" + vo.code + "</th>" +
                                "<th><img style='border-radius: 50%' width='40px' height='40px' gageaxax-height: 100%;' src=" + vo.img + "></th>" +
                                "<th data-field='name'>" + vo.name + "</th>" +
                                "<th data-field='price' data-width='80'>&yen;" + vo.price + "</th>" +
                                "<th data-field='number' data-width='200' data-align='center'>" + vo.number + "</th>" +
                                "<th data-field='total' data-width='80'>&yen;" + total + "</th></tr>";
                            $("#goodsOrderDetail").append(el);
                        }
                    }
                }
            });
            $("#myModal5").modal("show");
            // window.location.href = "/getOneCadreInfo/" + row.id;//跳转新增页面
        }
    }

