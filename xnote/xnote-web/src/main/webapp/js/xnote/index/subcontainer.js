function newxnote(group, type) {
    if (type == 'mkdown') {

        $('#container').html('<iframe src="/xnote/mkedit?groupId=' + group + '" class="xnote-frame" onload="iframeLoad(this)"></iframe>');
    } else {
        $('#container').html('<iframe src="/xnote/ueditor?groupId=' + group + '" class="xnote-frame" onload="iframeLoad(this)"></iframe>');
    }
}

function showContent(id, type) {
    var id = id.split('_')[1];
    if (true) {
        $('#container').html('<iframe id="if_' + id + '" src="/xnote/mkedit/' + id + '" class="xnote-frame" onload="iframeLoad(this)"></iframe>');
    }

}

function iframeLoad(){}
