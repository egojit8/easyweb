/**
 * Created by egojit on 2017/10/26.
 */
(function() {

    var config = {
        isDeubg: true,
        serverUrl: ""
    }
    function App() {
        this.version = "0.0.1";
        this.name = "EasySiteJs"
        this.isDeubg = true,
        this.index=-1
    }
    App.prototype = {
        tip:function (msg) {
            //提示层
            layer.msg(msg);
        },
        loding:function () {
            return layer.load(2);
        },
        close:function (index) {
            if(index&&index>-1){
                layer.close(index);
            }else{
                layer.close(layer.index);
            }

        },
        post: function (url, parm, success) {
            var mPostIndex=-1;
            $.ajax({
                url: config.serverUrl+url,
                data: parm,
                type: 'POST',
                dataType: 'json',
                error:function ( jqXHR,textStatus,errorThrown) {
                    app.tip("网络错误！");
                },
                success: function (data, textStatus, jqXHR) {
                    if (success) {
                        if(data&&data.code!=200){
                            app.tip(data.data)
                        }else {
                            success(data.data);
                        }
                    }
                },
                beforeSend: function () {
                    mPostIndex=app.loding();
                },
                complete: function () {
                    app.close(mPostIndex);
                }
            })
        },
        get: function (url, parm, success) {
            var mPostIndex=-1;
            $.ajax({
                url: config.serverUrl+url,
                data: parm,
                type: 'GET',
                dataType: 'json',
                error:function ( jqXHR,textStatus,errorThrown) {
                    app.tip("网络错误！");
                },
                success: function (data, textStatus, jqXHR) {
                    if (success) {
                        if(data&&data.code!=200){
                            app.tip(data.data)
                        }else {
                            success(data.data);
                        }
                    }
                },
                beforeSend: function () {
                    mPostIndex=app.loding();
                },
                complete: function () {
                    app.close(mPostIndex);
                }
            })
        },
        windows:function (title,url,width,height) {
            var width=width?width:"900px";
            var height=height?height:"580px";
            //iframe窗
            return layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: [width,height],
                content: url
            });
        }
    }
    var app = new App();
    window.app=app;
})();


