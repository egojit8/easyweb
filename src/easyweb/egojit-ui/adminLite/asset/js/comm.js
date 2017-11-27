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
        list: function (option) {
            $.jgrid.defaults.styleUI = "Bootstrap";
            var $jqGrid =$("#table_list");
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
                multikey:option.multikey?option.multikey:'id',
                prmNames: {page: "pageNo", rows: "pageSize"},
                viewrecords: true,
                add: true,
                edit: true,
                addtext: "Add",
                edittext: "Edit",
                hidegrid: false
            });
            $("#btnSearch").click(function () {
                var postData = app.serializeNotNull($("#formSearch").serialize());
                console.log(postData);
                $jqGrid.setGridParam({
                    postData: postData
                }).trigger("reloadGrid");
            });
//        $("#table_list_2").jqGrid("navGrid", "#pager_list_2", {
//            edit: true,
//            add: true,
//            del: true
//        }, {height: 200, reloadAfterSubmit: true});
            $("#table_list").setGridHeight($(window).height() - 190);
            $(window).bind("resize", function () {
                $(window).unbind("onresize");
                var width = $(".jqGrid_wrapper").width();
                $("#table_list").setGridWidth(width);
                $("#table_list").setGridHeight($(window).height() - 190);
                $(window).bind("onresize", this);
            });
            return $jqGrid;
        }
    }
    var app = new App();
    window.app = app;
})();


