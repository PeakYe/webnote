var fileId = c.getUrlParamter('xnoteId');
var groupId=c.getUrlParamter('group');
var acen_edit;

$(function(){
	(function(){
		marked.setOptions({
			renderer: new marked.Renderer(),
			gfm: true,
			tables: true,
			breaks: false,
			pedantic: false,
			wrap: 'free',
			sanitize: true,
			smartLists: true,
			smartypants: false,
			// animatedScroll: true,
			highlight: function(code) {
				return hljs.highlightAuto(code).value;
			}
		});
		acen_edit = ace.edit('mdeditor');
		acen_edit.setTheme('ace/theme/xcode');
		acen_edit.getSession().setMode('ace/mode/markdown');
		// 自动换行
		acen_edit.getSession().setOption('wrap', 'free');
		acen_edit.renderer.setShowPrintMargin(false);;
		console.log(acen_edit);
		$("#mdeditor").keyup(function() {
			$("#preview").html(marked(acen_edit.getValue()));
		});
		
	})();
	
	(function(){
		var input = document.getElementById('mdeditor');
		input.addEventListener('paste', function(event) {
			// 添加到事件对象中的访问系统剪贴板的接口
			var clipboardData = event.clipboardData,
			i = 0,
			items, item, types;
			if (clipboardData) {
				items = clipboardData.items;
				if (!items) {
					return;
				}
				item = items[0];
				// 保存在剪贴板中的数据类型
				types = clipboardData.types || [];
				for (; i < types.length; i++) {
					if (types[i] === 'Files') {
						item = items[i];
						break;
					}
				}
				// 判断是否为图片数据
				if (item && item.kind === 'file' && item.type.match(/^image\//i)) {
					// 读取该图片
					imgReader(item);
				}
			}
		});
		var imgReader = function(item) {
			var file = item.getAsFile(),
			reader = new FileReader();
			// 读取文件后将其显示在网页中
			reader.onload = function(e) {
				// var img = new Image();
				// img.src = e.target.result;
				// document.body.appendChild( img );
				// acen_edit.insert('![简短的图片]('+e.target.result+') ')
				c.ajax({
					url: '/service/xnote/uploadImg',
					data: {
						imgData: e.target.result
					},
					success: function(r) {
						acen_edit.insert('![](/service/xnote/' + r.data + ')  ');
					}
				})
			};
			// 读取文件
			reader.readAsDataURL(file);
		};
		
		$('.save').click(function() {
			var title = $('#title').val();
			if (title == null || title == '') {
				$('#title').focus();
				return;
			}
			var url;
			if (fileId == null) {
				// new
				url = '/service/xnote/create';
				//toastr.info('正在保存...');
				c.ajax({
					dataType: 'json',
					url: url,
					data: {
						title: title,
						content: acen_edit.getValue(),
						group:groupId
					},
					success: function(r) {
						var data=r.data;
						fileId = data;
//						layer.msg("已保存", {
//							skin: 'layui-layer-lan',
//							offset: ['80%', '5%']
//						});
						toastr.info('保存成功');
					},failure:function(r){
						toastr.info('创建失败：'+r);
					}
				})
			} else {
				// edit
				toastr.info('正在保存...');
				url = '/service/xnote/update';
				c.ajax({
					dataType: 'json',
					url: url,
					data: {
						id: fileId,
						title: title,
						content: acen_edit.getValue(),
						group:groupId
					},
					success: function(r) {
						var data=r.data;
						toastr.info('创建成功');
					},failure:function(r){
						toastr.info('创建失败：'+r);
					}
				})
			}
		});
	})();
	
	{
		if (fileId != null) {
			var iload = layer.load(0, {
				shade: 0.3
			});
			c.ajax({
				url: '/service/xnote/get',
				data: {
					id: fileId
				},
				success: function(r) {
					var data=r.data;
					acen_edit.setValue(data.content);
					$('#title').val(data.title);
					layer.close(iload);
				}
			})
		}
	}
	{
		var r = $("#preview-column");
		acen_edit.on('change',function(e){
			$("#preview").html(marked(acen_edit.getValue()));
			var scrollHeight = $(".ace_scrollbar-v").prop('scrollHeight');
	        var scrollTop = acen_edit.session.getScrollTop();
	        var height = $(".ace_scrollbar-v").height();
	        if(scrollHeight==0){
	        	r.stop(true).animate({
	            	scrollTop: (r.prop('scrollHeight'))
	            }, 500)
	        }else{
	        	r.stop(true).animate({
	        		scrollTop: (r.prop('scrollHeight') - r.height()) * ((scrollTop) / (scrollHeight - height))
	        	}, 300);
	        }
		});
		
		acen_edit.on('paste',function(e){
			var pattern=/^(http:|https:|ftp:)+/;
			if(pattern.test(e.text)){
				e.text=('[]('+e.text+')');
			}
		})
	}
	 
	
	
	(function() {
	    var l = $("#mdeditor");
	    var r = $("#preview-column");
	    
	    //init
	    
	    var scrollRight = false;
	    l.mouseover(function() {
	        scrollRight = true;
	        acen_edit.session.on('changeScrollTop', function() {
	            if (scrollRight) {
	                var scrollHeight = $(".ace_scrollbar-v").prop('scrollHeight');
	                var scrollTop = acen_edit.session.getScrollTop();
	                var height = $(".ace_scrollbar-v").height();
	                r.stop(true).animate({
	                    scrollTop: (r.prop('scrollHeight') - r.height()) * ((scrollTop) / (scrollHeight - height))
	                }, 300);
	            }
	        })
	    }).mouseleave(function() {
	        scrollRight = false;
	    })
	    r.mouseover(function() {
	    	r.unbind('scroll');
	        r.scroll(function() {
	            var scrollHeight = r.prop('scrollHeight');
	            var scrollTop = r.prop('scrollTop')
	            var height = r.height();
	            var rscrollHeight = $(".ace_scrollbar-v").prop('scrollHeight');
	            var rheight = $(".ace_scrollbar-v").height()
	            acen_edit.session.setScrollTop((rscrollHeight - height) * ((scrollTop) / (scrollHeight - height)));
	        })
	    }).mouseleave(function() {
	        r.unbind('scroll');
	    })
	})();


	(function(){
		$('#closeIframe').click(function(){
			var index = parent.layer.getFrameIndex(window.name);
			var isCreate = c.getUrlParamter('create')
			if(isCreate!=null){
			   parent.newxnoteCallback(fileId);
			}else{
			   parent.editxnoteCallback(fileId);
			}
			parent.layer.close(index);
		    
		});
	})()
})