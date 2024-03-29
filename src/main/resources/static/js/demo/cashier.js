let $ = jQuery;
let uuid = "cahier_1";
let oid=0;
$(function () {
    //根据窗口调整表格高度
    $(window).resize(function() {
        $('#exampleTableEvents').bootstrapTable('resetView', {
            height: tableHeight()-80
        });
        $("#s_goods").css("height",tableHeight()-20-$(".ibox-content").height()+"px");
        $("#g_keys").css("height",tableHeight()+"px");
    })
    $("#s_goods").css("height",tableHeight()-20-$(".ibox-content").height()+"px");
    $("#g_keys").css("height",tableHeight()+"px");

    $('#exampleTableEvents').bootstrapTable({

        height:tableHeight()-80,//高度调整
        striped: true, //是否显示行间隔色
        locale: 'zh-CN',//中文支持,
    });
    setInterval(function () {
        // 判断支付框是否弹出
        // 1.未弹出：条码扫描输入框获取焦点
        // 2.弹出 ：卡号扫描输入框获取焦点
        let displayValue = $('#myModal5').css('display');
        if (displayValue === 'none'){
            $("#code_search").focus();
        }else{
            $("#cardID").focus();
        }

        $.ajax("/cashier/goods",{
            headers:{
                contentType: "application/x-www-form-urlencoded",
            },
            method: "post",
            data:{pageNum:uuid},
            success(res){
                //渲染数据
                let list = res.data.goods;
                if(list){
                    $("#cashierGoods").html("");
                    let total = 0;
                    for(let vo of list){
                        total+= vo.number*vo.price;
                        let el = "<tr code="+vo.code+" ><th data-field='code' style='color: #9c9c9c'>"+vo.code+"</th><th><img style='border-radius: 50%' width='40px' height='40px' gageaxax-height: 100%;' src="+vo.img+"></th><th data-field='name'>"+vo.name+"</th><th data-field='price' data-width='80'>&yen;"+vo.price+"</th><th data-field='number' data-width='200' data-align='center'><div class='input-group'><span class='input-group-btn'>" +
                            "<button type='button' style='margin-bottom: 0px' class='btn btn-primary'  onclick='subGoods("+vo.code+")' >-</button></span>" +
                            "<input type='text' style='width: 60px;text-align: center' class='form-control' value='"+vo.number+"' >" +
                            "<span class='input-group-btn'> <button type='button' style='margin-bottom: 0px' class='btn btn-primary' onclick='sendCode("+vo.code+")' >+" +
                            "</button></div></span></th><th data-field='total' data-width='80'>&yen;"+(vo.number*vo.price)+"</th></tr>";
                        $("#cashierGoods").append(el);
                    }

                    let toFixed = (parseInt(total*100)/100).toFixed(2);
                    $("#total").html(toFixed);
                    $("#ys").html(toFixed);
                    // 输入金额的文本框 动态赋值
                    $("#xj").val(toFixed);
                    // 弹出的待支付页面 动态赋值
                    $("#dzf").html(toFixed)

                }
                let keys = res.data.hang_one_keys;
                if (keys){
                    $("#keys").html("");

                    for (let ke of keys.sort()) {
                        let el =  "<tr "+ (ke==uuid?"style=\"background:rgb(99,85,178);color:#FFF\"":"") +" onclick=setKeys('"+ke+"') ><td>"+ke+"</td></tr>";
                        $("#keys").append(el);
                    }
                }
            }
        });
    },1000)
       //url为后台action

    $("#goods_search").click(function () {
        let key = $("#key").val();
        $.ajax("/goods/findGoods",{
            data:{key:key},
            success(res){
                //渲染数据
                let goods = res.data;
                $("#goods").html("");
                goods.forEach(item=>{
                    let el =  "<tr onclick='sendCode("+item.code+")'><td class='client-avatar'><img src="+item.img+"></td><td>"+item.name+"</td></tr>";
                    $("#goods").append(el);
                })
            }
        });
    })
    /*添加条码*/
    $("#addCode").click(function () {
        let code = $("#code_search").val();
        if (code != ""){
            sendCode(code);
        }
        $("#code_search").val("")
    })
    /*挂单*/
    $("#hang_one").click(function () {
        let date = new Date();
        uuid = "cashier_"+date.getTime();
    })
    /*创建单据*/
    $("#createOrder").click(function () {
        createOrder();
    });

    //键盘事件
    $(window).keydown(function (e) {
        switch (e.keyCode) {
            case 107:
                //加号
                let $last = $("#cashierGoods").children().last();
                if($last.length){
                    sendCode($last.attr("code"))
                }
                break;
            case 109:
                //减号
                let $las = $("#cashierGoods").children().last();
                if($las.length){
                    subGoods($las.attr("code"))
                }
                break;
            case 13:
                //结算
                //createOrder();
                let displayValue = $('#myModal5').css('display');
                let code = "";
                //支付窗口隐藏
                if (displayValue === 'none'){
                    //商品查询
                    code = $("#code_search").val();
                    sendCode(code);
                    $("#code_search").val("");
                }else{
                    //刷卡支付
                    code = $("#cardID").val();
                    cardPay(code);
                    $("#cardID").val("");
                }
                break;
            case 9:
                //挂单
                let date = new Date();
                uuid = "cashier_"+date.getTime();
                break;
            case 32:
                //结算
                $("#createOrder").trigger("click");
                break;
        }
    })
//    现金
    $("#zf input").change(function () {
        showTotalPrice();
    })
    /*订单支付*/
    $("#order-save").click(function () {
        let i=0;
        typeids = [];
        prices= [];
        if($("#xj").val()){
            typeids[i] = 1;
            // 目前两个支付方式都使用了xj支付字段。
            // 所以不能用这一行了，这一行影响输入的金额数(比如金额数有.50的情况)
            //prices[i] = parseInt($("#xj").val())-(getTotalPrice()-parseInt($("#ys").html()));
            prices[i] = $("#xj").val();
            i++;
        }
        if($("#wx").val()){
            typeids[i] = 2;
            prices[i] = $("#wx").val();
            i++;
        }
        if($("#zfb").val()){
            typeids[i] = 3;
            prices[i] = $("#zfb").val();
            i++;
        }
        if($("#sk").val()){
            typeids[i] = 4;
            prices[i] = $("#sk").val();
            i++;
        }

        $.ajax("/order/payOrder",{
            headers:{
                contentType: "application/x-www-form-urlencoded",
            },
            method: "post",
            data: {oid:oid,prices:JSON.stringify(prices),typeids:JSON.stringify(typeids),cno:uuid},
            success(res){
                $("#xj").val("");
                $("#wx").val("");
                $("#zfb").val("");
                $("#sk").val("");
                $("#ss").html("0.00");
                $("#zl").html("0.00");
                $("#myModal5").modal("hide");
                if (res.code == 200) $("#goods").html("");
                console.log(res);
            }
        });
    });

})

/*查询商品信息*/
function selectGoods(code){
    $.ajax("/goods/findGoods",{
        data:{key:code},
        success(res){
            let goods = res.data;
            console.log(goods);
            $("#goods").html("");
            goods.forEach(item=>{
                let el = "<tr><td colspan='2' align='center'><img src="+item.img+" width='300px' height='300px' ></td></tr>"
                    + "<tr><td colspan='2' align='center' height='70px'>" + item.code + "</td></tr>"
                    + "<tr><td align='right'>商品名称：</td><td align='left'>" + item.name + "</td></tr>"
                    + "<tr><td align='right'>价格：</td><td  align='left'>" + item.price + "</td></tr>"
                    + "<tr><td align='right'>规格：</td><td  align='left'>" + item.specification + "</td></tr>"
                    + "<tr><td align='right'>产地：</td><td  align='left'>" + item.manufacturer + "</td></tr>"
                $("#goods").append(el);
            })
        }
    })
}
/*显示金额*/
function showTotalPrice() {
    $("#ss").html(getTotalPrice());
    $("#zl").html(getTotalPrice()-parseInt($("#ys").html()));
}

/*记录输入的总金额*/
function getTotalPrice() {
    let xj = parseInt($("#xj").val()?$("#xj").val():0);
    let wx = parseInt($("#wx").val()?$("#wx").val():0);
    let zfb = parseInt($("#zfb").val()?$("#zfb").val():0);
    let sk = parseInt($("#sk").val()?$("#sk").val():0);
    return xj+wx+zfb+sk;
}
/*选择单*/
function setKeys(key){
    uuid = key;
}
/*创建订单*/
function createOrder(){
    $.ajax("/order/createOrder",{
        headers:{
            contentType: "application/x-www-form-urlencoded",
        },
        method: "post",
        data:{ono:uuid},
        success(res){
            oid = res.data;
            $.ajax("/order/getOrderPeyCode",{
                headers:{
                    contentType: "application/x-www-form-urlencoded",
                },
                method: "post",
                data:{oid:oid},
                success(qrCode){
                    console.log(qrCode)
                    document.getElementById("wxPeyCode").src = qrCode.msg;
                    let interval=setInterval(function (){
                        $.ajax("/order/selectOrder",{
                            headers:{
                                contentType: "application/x-www-form-urlencoded",
                            },
                            method: "get",
                            data:{oid:oid},
                            success(res){
                                console.log(res.data);
                                if (res.data.isPay ==2){
                                    clearInterval(interval);//停止循环定时
                                    $("#order-save").trigger("click");
                                    swal("支付成功", "您已成功支付，感谢您的光临！", "success");
                                    setTimeout(function () {
                                        swal.close();
                                    },3000)
                                }
                            }
                        })
                    },1000)
                }
            });
            showType();
        }
    });
}

/*创建单据*/
function showType() {

    $("#myModal5").modal("show");
}

/*发送条码信息*/
function sendCode(code) {
    let no = $("#no").val();
    $.ajax("/code/jscode",{
        data:{no:no,type:"sy",code:code},
        success(){
            selectGoods(code);
            //提示
        }
    });
}
/*刷卡支付修改会员卡余额更新订单支付状态*/
function cardPay(code){
    let price = $("#xj").val();
        console.log(code);
        console.log(price);
        $.ajax("/membershipCard/updateBalance",{
            headers:{
                contentType: "application/x-www-form-urlencoded",
            },
            method: "put",
            data:{mno:code,price:price,oid:oid}
        })
}
/*数量减1*/

function subGoods(code) {
    $.ajax("/cashier/subGoods",{
        data:{pageNum:uuid,code:code},
        success(){
            //提示
        }
    });
}

function tableHeight() {
    return $(window).height() - 150;
}

//列表行‘操作’按钮
    function AddFunctionAlty(value, row, index) {
        return '<button id="TableView" type="button" class="btn btn-default">删除</button>'

    }
//请求服务数据时所传查询参数
    function queryParams(params){
        return{
            pageSize: params.limit,
            pageNum:parseInt(params.offset/params.limit)+1,
            name:$('#searchName').val()
        }
    }
//点击新增按钮事件
    window.operateEvents = {
        "click #TableView": function (e, value, row, index) {
            window.location.href = "/getOneCadreInfo/" + row.id;//跳转新增页面
        }
    }




