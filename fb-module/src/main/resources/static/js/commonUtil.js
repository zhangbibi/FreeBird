var util = (function($) {
	var util={};
	var commonPart1='<div id="dialog" class="threeguys_dialog_confirm" id="dialog1">'+
    				'<div class="threeguys_mask"></div>'+
    				'<div class="threeguys_dialog">'+
    				'<div class="threeguys_dialog_hd"><span id="title" class="threeguys_dialog_title">';
	var commonPart2='</span></div><div id="content" class="threeguys_dialog_bd">';
	var commonPart3='</div><div class="threeguys_dialog_ft">';
	var confimHTML='<div href="javascript:;" class="threeguys_btn_dialog threeguys_btn_default" id="cancel">取消</div><div href="javascript:;" class="threeguys_btn_dialog primary" id="sure">确定</div></div></div></div>';
	var alertHTML='<div href="javascript:;" class="threeguys_btn_dialog primary" id="sure">确定</div></div></div></div>';
	
	
	var toastHTML1='<div id="toast"><div style="position: fixed;padding: 0px 5px;z-index: 13;top: 80%;left: 50%;-webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);background-color: rgba(84, 83, 83, 0.72);text-align: center;border-radius: 10px;overflow: hidden;">';
	var toastHTML3='<span style="display: block;font-size: 16px;color: #FFFFFF;padding: 6px;">';
	var toastHTML4='</span></div></div>';
     
	util.toast = function(option){
		var toastHTML=toastHTML1;
		toastHTML+=(toastHTML3+option.tips+toastHTML4);
		$("body").append(toastHTML);
		var time=option.time?option.time:2000;
		setTimeout("$('#toast').remove();", time )
	}
	util.showconfim = function(option){
		var showhtml = commonPart1+option.title+commonPart2+option.content+commonPart3+confimHTML;
		$("body").append(showhtml);
		$("#sure").on("click",function(){
			$("#dialog").remove();
			if(option.surefunc)
				option.surefunc();			
		});
		$("#cancel").on("click",function(){
			$("#dialog").remove();
			if(option.cancelfunc)
				option.cancelfunc();			
		});
	}
	util.showalert = function(option){
		var showhtml = commonPart1+option.title+commonPart2+option.content+commonPart3+alertHTML;
		$("body").append(showhtml);
		$("#sure").on("click",function(){
			$("#dialog").remove();
			if(option.surefunc)
				option.surefunc();			
		});
	}
	util.GetArgsFromHref = function(sHref, sArgName){
       var args = sHref.split("?");
       var retval = "";
       if(args[0] == sHref) /*参数为空*/
       {
            return retval; /*无需做任何处理*/
       }  
       var str = args[1];
       args = str.split("&");
       for(var i = 0; i < args.length; i ++)
       {
           str = args[i];
           var arg = str.split("=");
           if(arg.length <= 1) continue;
           if(arg[0] == sArgName) retval = arg[1]; 
       }
       return retval;
	}
	return util;
})(jQuery);
$.fn.extend({
	_opt: {
		placeholader: '<p style="color: #7b7979;">请输入正文,不可少于10个字</p><p style="color: #7b7979;">收费内容的前30字对读者可见</p>',
	},
	artEditor: function(e) {
		var t = this,
			a = {
				"-webkit-user-select": "text",
				"user-select": "text",
				"overflow-y": "auto",
				"word-break": "break-all",
				outline: "none"
			};
		$(this).css(a).attr("contenteditable", true), t._opt = $.extend(t._opt, e);
		//t.placeholderHandler()
	},
	placeholderHandler: function() {
		var e = this;
		$(this).on("focus", function(ee) {
//			$(this).trigger("focus");
 			$.trim($(this).html()) === e._opt.placeholader && $(this).html("");
		}).on("blur", function() {
 			$(this).html() || $(this).html(e._opt.placeholader)
		});
		$.trim($(this).html()) || $(this).html(e._opt.placeholader)
	}
	
});
