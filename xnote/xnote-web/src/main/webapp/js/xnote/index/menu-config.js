var menu;
function createNoteMenu(id,groupId,title){
    menu.createMenu({
        id:'f_'+id,
        text:title,
        parent:groupId,
        isfolder: false,
        prefix : '<i class="glyphicon glyphicon-file" ></i>',
        click:function(row){
            showContent(this.id,title);
        }
    });
    //toastr.info('已创建文件夹 “'+title+'”')
}
// console.log('v3');

// 初始化加载
c.ajax({
    url : '/service/xnote/group/detaillist.json',
    success : function(r) {
        var firstMenu = {};
        firstMenu['setup']={
            id: 'setup',
            text: '设置'
            ,click:function(){}
        };

        var secondMenu = {};
        //文件夹
        for ( var i in r.data) {
            var d = r.data[i];

            if (!d.parentId) {
                //一级菜单
                secondMenu[d.id] = {
                    id : d.id,
                    text : d.name
                }
            } else {
                //二级菜单
                secondMenu[d.id] = {
                    id : d.id,
                    text : d.name,
                    parent : d.parentId
                }
            }

            //笔记
            for ( var j in d.notes) {
                var note = d.notes[j];
                var nnode = {
                    text : c.escapeHtml(note.title),
                    prefix : '<i class="glyphicon glyphicon-file" ></i>',
                    parent : note.groupId,
                    attrs : {
                        'note-id' : note.id
                    },
                    click:function(row){
                        showContent(this.id);
                    },
                    isfolder: false
                }
                secondMenu['f_'+note.id] = nnode;
            }
        }

        // console.log(firstMenu);
        // console.log(secondMenu);

        menu = new FileView({
            contain : '#tree',
            mainHeadHtml : '<img class="head-img" src="/imgs/head.jpg" />',
            mainMenu : firstMenu
        })

        $(".dropdown-toggle").dropdown('toggle');
        menu.addSubMenu(secondMenu);

        if(!menu.showDefault()){
            //
        }

        menu.sortMenu();
    }
})

// saveXnoteCallback
function changeXnoteCallback(id,groupId,title) {
    var fid='f_'+id;
    var f=menu.findMenu(fid);
    if(f!=null){
        menu.modifyMenu(fid,{
            id:fid,
            parent:groupId,
            text:title,
            isfolder: false
        })
    }else{
        createNoteMenu(id,groupId,title);
    }

}

function newxnoteCallback(id) {
    if(id==null){
        return;
    }
    c.ajax({
        url : '/service/xnote/detail',
        data : {
            id : id
        },
        success : function(r) {
            var data = r.data;
            createNoteMenu(data.id,data.groupId,data.title);
            //showContent('f_'+data.id);
        }
    })
}


// 右击菜单
var menu = new BootstrapMenu('.sub-menu-ul li,.sub-menu-ul', {
    fetchElementData : function(row) {
        return {
            isfolder: row.attr('isfolder')=='true',
            folderId : row.attr('sub-menu-id'),
            deleteable: !row.hasClass('sub-menu-ul'),
            currentFolder: !(row.hasClass('sub-menu-ul'))?row.parent().attr('sub-menu-id'):row.attr('sub-menu-id')
        }
    },

    actions : [ {
        name : '创建富文本笔记',
        onClick : function(row) {
            if(row.folderId==null){
                layer.alert('需要先选择文件夹')
                return ;
            }
            newxnote(row.currentFolder);
        }
    }, {
        name : '创建MARKDOWN笔记',
        onClick : function(row) {
            if(row.folderId==null){
                layer.alert('需要先选择文件夹')
                return ;
            }
            newxnote(row.currentFolder,'mkdown');
        }
    }, {
        name : '创建文件夹',
        onClick : function(row) {
            layer.prompt({title: '文件夹名称', formType: 0, maxlength:6 }, function(text, index){
                layer.close(index);
                c.ajax({
                    url : '/service/xnote/group/create.json',
                    data : {
                        parentId :row.currentFolder==null?null:row.currentFolder,
                        name : text
                    },
                    success : function(r) {
                        var d=r.data;
                        menu.createMenu({
                            id:d.id,
                            text: d.name,
                            parent: d.parentId
                        })
                    }
                })
            });
        }
    } ,{
        name : '删除',
        onClick : function(row) {
            if(row.isfolder){
                layer.confirm('确认删除？',{
                    btn:['是','点错了']
                },function(index){
                    layer.close(index);
                    c.ajax({
                        url : '/service/xnote/group/delete.json',
                        data : {
                            id : row.folderId
                        },
                        success : function(r) {
                            var d=r.data;
                            menu.deleteMenu(row.folderId,function(del){
                                toastr.info('已删除文件夹 “'+del.text+'”')
                            });
                        }
                    });
                });
            }else{
                layer.confirm('确认删除？',{
                    btn:['是','点错了']
                },function(index){
                    layer.close(index);
                    c.ajax({
                        url : '/service/xnote/delete.json',
                        data : {
                            id : row.folderId.split('_')[1]
                        },
                        success : function(r) {
                            var d=r.data;
                            menu.deleteMenu(row.folderId,function(del){
                                toastr.info('已删除笔记 “'+del.text+'”')
                            });
                        }
                    });
                });
            }

        },
        isEnabled: function(row) {
            return row.deleteable;
        }
    },{
        name : '转移',
        onClick : function(row) {
            menu.moveModel(function(menuData,to,success,error){
                var noteId=menuData.attrs['note-id'];
                var endret=false;
                c.ajax({
                    url:'/service/xnote/update/move.json',
                    data:{
                        id: noteId,
                        to: to
                    },
                    method:'post',
                    async:false,
                    success:function(r){
                        endret=true;
                        if(success){
                            success();
                        }
                    },failure:function(r){
                        toastr.error('转移失败：'+r.message);
                        if(error){
                            error();
                        }
                    }
                });
                return endret;
            });
        }
    }]
});