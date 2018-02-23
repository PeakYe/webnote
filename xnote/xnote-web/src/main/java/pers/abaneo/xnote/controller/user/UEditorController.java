package pers.abaneo.xnote.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

@Controller
@RequestMapping("ueditor")
public class UEditorController {

    @ResponseBody
    @RequestMapping("config")
    public String config() throws IOException {
        InputStreamReader isr = new InputStreamReader(UEditorController.class.getResourceAsStream("/ueditor-config.json"), "UTF-8");
        //将file文件内容转成字符串
        BufferedReader bf = new BufferedReader(isr);
        String content = "";
        StringBuilder sb = new StringBuilder();
        while (content != null) {
            content = bf.readLine();
            if (content == null) {
                break;
            }
            sb.append(content.trim());
        }
        bf.close();


        String fileStr = sb.toString();
        return sb.toString();
    }

    @RequestMapping("index")
    public String index(){
        return "xnote/ueditor";
    }
}
