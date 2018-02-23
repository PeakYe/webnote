var renderer = new marked.Renderer();
renderer.link = function (href,title,text) {
    return '<a href="'+href+'" target="_blank">'+text+'</a>';
};
marked.setOptions({
    renderer : renderer,
    gfm : true,
    tables : true,
    breaks : false,
    pedantic : false,
    sanitize : true,
    smartLists : true,
    smartypants : false,
    highlight : function(code) {
        return hljs.highlightAuto(code).value;
    }
});


$('#btnedit').click(function() {
    //window.open('mkedit.html?xnoteId=' + currentBlog.id);
    editxnote(currentBlog.id);
})
$('#btnview').click(function() {
    window.open('/xnote/view/' + currentBlog.id);
})
$('#btndel').click(function() {
    layer.confirm('确认删除？', {
        btn : [ '确定', '点错了' ]
    }, function() {
        layer.closeAll();
        c.ajax({
            url : '/service/xnote/delete.req',
            data : {
                id : currentBlog.id
            },
            success : function() {
                $('#container').addClass('hidden');
                menu.deleteMenu(currentBlog.id,function(del){
                    toastr.info('已删除笔记');
                });
            },
            complete : function() {
                layer.closeAll();
            }
        })
    })
})