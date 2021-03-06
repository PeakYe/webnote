UE.registerUI('save', function(editor, uiName) {
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName, {
        execCommand: function() {
            // alert('execCommand:' + uiName);

            // layer.promot()
            console.log(editor)
            $.ajax({
                url:saveUrl,
                data:{
                    id: $('#id').val(),
                    group:$('#groupId').val(),
                    title:$('#title').val(),
                    content:editor.getContent(),
                    type:'ueditor'
                },
                method:'post',
                success:function(data){
                    // console.log(data)
                    if(data.success){
                        toastr.success("保存成功");
                        parent.changeXnoteCallback(data.data,$('#groupId').val(),$('#title').val());
                    }else{
                        toastr.err(data.data)
                    }

                },
                error:function(){
                    toastr.info("error");
                }
            })
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name: uiName,
        //提示
        title: "保存文档",
        //添加额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -480px -20px;',
        //点击时执行的命令
        onclick: function() {
            //这里可以不用执行命令,做你自己的操作也可
            editor.execCommand(uiName);
        }
    });
    //当点到编辑内容上时，按钮要做的状态反射
    editor.addListener('selectionchange', function() {
        var state = editor.queryCommandState(uiName);
        if (state == -1) {
            btn.setDisabled(true);
            btn.setChecked(false);
        } else {
            btn.setDisabled(false);
            btn.setChecked(state);
        }
    });
    //因为你是添加button,所以需要返回这个button
    return btn;
});