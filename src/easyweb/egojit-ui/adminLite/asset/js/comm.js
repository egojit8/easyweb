/**
 * Created by egojit on 2017/10/26.
 */
(function () {

    var config = {
        isDeubg: true,
        serverUrl: ""
    }

    function App() {
        this.version = "0.0.1";
        this.name = "EasySiteJs";
        this.isDeubg = true;
        this.index = -1;
    }

    App.prototype = {

        serializeNotNull: function (serStr) {
            return serStr.split("&").filter(function (str) {
                return !str.endsWith("=")
            }).join("&");
        },
        tip: function (msg) {
            //提示层
            layer.msg(msg);
        },
        confirm: function (title, callBack) {
            //询问框
            layer.confirm(title, {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (callBack) {
                    callBack();
                }
            }, function () {
                app.close();
            });
        },
        loding: function () {
            return layer.load(2);
        },
        close: function (index) {
            if (index && index > -1) {
                layer.close(index);
            } else {
                layer.close(layer.index);
            }

        },
        post: function (url, parm, success) {
            var mPostIndex = -1;
            $.ajax({
                url: config.serverUrl + url,
                data: parm,
                type: 'POST',
                dataType: 'json',
                error: function (jqXHR, textStatus, errorThrown) {
                    app.tip("网络错误！");
                },
                success: function (data, textStatus, jqXHR) {
                    if (success) {
                        if (data) {
                            if (data.code) {
                                if(data.code==200){
                                    success(data.data);
                                }else{
                                    app.tip(data.data)
                                }
                            } else {
                                success(data);
                            }

                        } else {
                            app.tip("未知的错误！")
                        }
                    }
                },
                beforeSend: function () {
                    mPostIndex = app.loding();
                },
                complete: function () {
                    app.close(mPostIndex);
                }
            })
        },
        get: function (url, parm, success) {
            var mPostIndex = -1;
            $.ajax({
                url: config.serverUrl + url,
                data: parm,
                type: 'GET',
                dataType: 'json',
                error: function (jqXHR, textStatus, errorThrown) {
                    app.tip("网络错误！");
                },
                success: function (data, textStatus, jqXHR) {
                    if (success) {
                        if (data && data.code != 200) {
                            app.tip(data.data)
                        } else {
                            success(data.data);
                        }
                    }
                },
                beforeSend: function () {
                    mPostIndex = app.loding();
                },
                complete: function () {
                    app.close(mPostIndex);
                }
            })
        },
        windows: function (title, url, width, height) {
            var width = width ? width : "900px";
            var height = height ? height : "580px";
            //iframe窗
            return layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: [width, height],
                content: url
            });
        },
        getFormJson: function (form) {
            var o = {};
            var a = $(form).serializeArray();
            $.each(a, function () {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        }
        ,
        list: function (option) {
            $.jgrid.defaults.styleUI = "Bootstrap";
            var $jqGrid = $("#table_list");
            $jqGrid.jqGrid({
                url: option.url,
                datatype: "json",
                mtype: "post",
                height: 'auto',
                autowidth: true,
                shrinkToFit: true,
                rowNum: 10,
                rownumbers: true,
                rowList: [10, 20, 30],
                multiselect: true,//复选框
                colNames: option.colNames,
                colModel: option.colModel,
                pager: "#pager_list",
                multikey: option.multikey ? option.multikey : 'id',
                prmNames: {page: "pageNo", rows: "pageSize"},
                viewrecords: true,
                add: true,
                edit: true,
                addtext: "Add",
                edittext: "Edit",
                hidegrid: false
            });
            var op = {}
            var oldPostData = $.extend(true, op, $jqGrid.getGridParam().postData);
            $("#btnSearch").click(function () {
                // var postData = app.serializeNotNull($("#formSearch").serialize());
                console.log(app.getFormJson("#formSearch"));
                $.each($jqGrid.getGridParam().postData, function (k, v) {
                    delete $jqGrid.getGridParam().postData[k];
                });
                var p = $.extend(app.getFormJson("#formSearch"), oldPostData);
                if (op)
                    p = $.extend(p, op);
                $jqGrid.setGridParam({
                    postData: p
                }).trigger("reloadGrid");
            });
            $("#btnRest").click(function () {
                $("#formSearch input").val("");
                $("#formSearch select").val("");
                console.log(app.getFormJson("#formSearch"));
                $.each($jqGrid.getGridParam().postData, function (k, v) {
                    delete $jqGrid.getGridParam().postData[k];
                });
                var p = $.extend(app.getFormJson("#formSearch"), oldPostData);
                if (op)
                    p = $.extend(p, op);
                $jqGrid.setGridParam({
                    postData: p
                }).trigger("reloadGrid");
            });

//        $("#table_list_2").jqGrid("navGrid", "#pager_list_2", {
//            edit: true,
//            add: true,
//            del: true
//        }, {height: 200, reloadAfterSubmit: true});
            $("#table_list").setGridHeight($(window).height() - 230);
            $(window).bind("resize", function () {
                $(window).unbind("onresize");
                var width = $(".jqGrid_wrapper").width();
                $("#table_list").setGridWidth(width);
                $("#table_list").setGridHeight($(window).height() - 230);
                $(window).bind("onresize", this);
            });

            var list = {
                list: $jqGrid,
                refresh: function (parm) {
                    op = parm;
                    var oldPostData = $.extend(true, {}, $jqGrid.getGridParam().postData);
                    $.each($jqGrid.getGridParam().postData, function (k, v) {
                        delete $jqGrid.getGridParam().postData[k];
                    });
                    $jqGrid.setGridParam({
                        postData: $.extend(oldPostData, parm)
                    }).trigger("reloadGrid");
                }
            }
            return list;
        },
        getRequest: function () {
            var url = location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        }
        ,
        initFormView: function (id, data) {
            var $elementsTextA = $(id + " textarea");
            $.each($elementsTextA, function (index, val) {
                console.log(val);
                var name = $(val).attr("name");
                $(val).text(data[name]);

            })
            var $elements = $(id + " input");
            $.each($elements, function (index, val) {
                console.log(val);
                var name = $(val).attr("name");
                $(val).val(data[name]);

            })

            var $elementsSelect = $(id + " select");
            $.each($elementsSelect, function (index, val) {
                console.log("selcet:===="+val);
                debugger;
                var name = $(val).attr("name");
                var value=data[name];
                $(val).val(value);
            })

        }
    }
    var app = new App();
    window.app = app;


})();


