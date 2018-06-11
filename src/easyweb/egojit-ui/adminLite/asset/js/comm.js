/**
 * Created by egojit on 2017/10/26.
 */
(function () {

    var config = {
        isDeubg: true,
        serverUrl: "",
        fileUrl:"http://dingdou666.com:9696/"
    }

    function App() {
        this.version = "0.0.1";
        this.name = "EasySiteJs";
        this.isDeubg = true;
        this.index = -1;
    }

    App.prototype = {
        config:config,
        serializeNotNull: function (serStr) {
            return serStr.split("&").filter(function (str) {
                return !str.endsWith("=")
            }).join("&");
        },
        tip: function (msg) {
            //提示层
            layer.msg(msg);
        },
        showImage:function (title,url) {
            var imgJson={title: title, //相册标题
                id: 123, //相册id
                start: 0, //初始显示的图片序号，默认0
                data: [   //相册包含的图片，数组格式
                    {
                        alt: title,
                        src: config.fileUrl+url, //原图地址
                        thumb: "" //缩略图地址
                    }
                ]
            }
            layer.photos({
                photos: imgJson //格式见API文档手册页
                ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机
            });
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
                layer.closeAll();

            }

        },
        closeParent: function (index) {
            if (index && index > -1) {
                parent.layer.close(index);
            } else {
                parent.layer.closeAll();
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
                                if (data.code == 200) {
                                    success(data.data);
                                } else {
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
        export: function (url, parm) {

            console.log("parm", parm);
            var fileUrl = url;
            if (parm) {
                fileUrl = fileUrl + "?" + app.getUrlParm(parm);
            }
            try {
                var elemIF = document.createElement("iframe");
                elemIF.src = fileUrl;
                elemIF.style.display = "none";
                document.body.appendChild(elemIF);
            } catch (e) {
                app.tip("下载异常！");
            }
            //定义一个form表单,通过form表单来发送请求
            // var form=$("<form>");
            //
            // //设置表单状态为不显示
            // form.attr("style","display:none");
            // //method属性设置请求类型为get
            // form.attr("method","get");
            // //action属性设置请求路径,(如有需要,可直接在路径后面跟参数)
            // //例如:htpp://127.0.0.1/test?id=123
            // form.attr("action",fileUrl);
            // //将表单放置在页面(body)中
            // $("body").append(form);
            //
            // //表单提交
            // form.submit();
        },
        getUrlParm: function (param, key) {
            var paramStr = "";
            if (param instanceof String || param instanceof Number || param instanceof Boolean) {
                paramStr += "&" + key + "=" + encodeURIComponent(param);
            } else {
                $.each(param, function (i) {
                    var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                    paramStr += '&' + app.getUrlParm(this, k);
                });
            }
            return paramStr.substr(1);
        },
        windows: function (title, url, width, height) {
            var width = width ? width : "900px";
            var height = height ? height : "580px";
            //iframe窗
            return layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: 0.3,
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
        },
        list: function (option) {
            $.jgrid.defaults.styleUI = "Bootstrap";
            var $jqGrid = $("#table_list");
            if (option.tableId) {
                $jqGrid = $(option.tableId);

            }
            var pagerId = "#pager_list";
            if (option.pageId) {
                pagerId = option.pageId;
            }
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
                pager: pagerId,
                multikey: option.multikey ? option.multikey : 'id',
                prmNames: {page: "pageNo", rows: "pageSize"},
                viewrecords: true,
                add: true,
                edit: true,
                addtext: "Add",
                edittext: "Edit",
                hidegrid: false,
                postData: option.postData,
                loadComplete: option.loadComplete
            });
            var op = {};
            var oldPostData = {};
            if ($jqGrid.getGridParam() && $jqGrid.getGridParam().postData) {
                oldPostData = $.extend(true, op, $jqGrid.getGridParam().postData);
            }
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
            var titleBox=$(".ibox-title");
            var listOprBox=$("#listOprBox");
            var heightTitle=0;
            if(titleBox&&titleBox.html()&&titleBox.html().length>0){
                heightTitle=titleBox.height()+28;
            }
            if(listOprBox&&listOprBox.children().length>0){
                heightTitle=heightTitle+listOprBox.height();
            }
            heightTitle=heightTitle+40+80;
            $("#table_list").setGridHeight($(window).height() - heightTitle);
            $(window).bind("resize", function () {
                $(window).unbind("onresize");
                var width = $(".jqGrid_wrapper").width();
                $("#table_list").setGridWidth(width);
                $("#table_list").setGridHeight($(window).height() - heightTitle);
                $(window).bind("onresize", this);
            });

            var list = {
                list: $jqGrid,
                initOpr: function (option) {
                    var html = '<span id="oprBox" class="center-block text-center">';
                    for (var i = 0; i < option.length; i++) {
                        var btnItem = $(option[i].id);
                        if (btnItem && btnItem.text() && btnItem.text().length > 0) {
                            btnItem.attr("onclick", option[i].click);
                            var btnItemHtml = $("<p>").append(btnItem.clone()).html();
                            html += btnItemHtml;
                        }
                    }
                    html = html + '</span>';
                    return html;

                },
                refresh: function (parm) {
                    op = parm;
                    var oldPostData = {};
                    if ($jqGrid.getGridParam() && $jqGrid.getGridParam().postData) {
                        oldPostData = $.extend(true, {}, $jqGrid.getGridParam().postData);
                    }
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
        },
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
                console.log("selcet:====" + val);
                var name = $(val).attr("name");
                var value = data[name];
                $(val).val(value);
            })

        },
        initDetailView: function (id, data) {
            var $elementsTextA = $(id + " textarea");
            $.each($elementsTextA, function (index, val) {
                console.log(val);
                var name = $(val).attr("name");
                $(val).text(data[name]);
                $(val).attr("disabled", "disabled");

            })
            var $elements = $(id + " input");
            $.each($elements, function (index, val) {
                console.log(val);
                var name = $(val).attr("name");
                $(val).val(data[name]);
                $(val).attr("disabled", "disabled");

            })

            var $elementsSelect = $(id + " select");
            $.each($elementsSelect, function (index, val) {
                console.log("selcet:====" + val);
                var name = $(val).attr("name");
                var value = data[name];
                $(val).val(value);
                $(val).attr("disabled", "disabled");
            })

            $("#btnSubmit").hide();
        },
        initDetailLableView: function (id, data) {
            var $elements = $(id + " label");
            console.log($elements);
            $.each($elements, function (index, val) {
                console.log(val);
                var name = $(val).attr("name");
                $(val).html(data[name]);
            })

        },
        time: {
            getLocalTime: function (time) {
                var date = new Date(time);
                var y = date.getFullYear();
                var m = date.getMonth() + 1;
                m = m < 10 ? ('0' + m) : m;
                var d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                var h = date.getHours();
                h = h < 10 ? ('0' + h) : h;
                var minute = date.getMinutes();
                var second = date.getSeconds();
                minute = minute < 10 ? ('0' + minute) : minute;
                second = second < 10 ? ('0' + second) : second;
                return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
            }
        }
    }
    var app = new App();
    window.app = app;


})();


