package com.smartContactManager.controller;

import com.smartContactManager.helper.ZXingHelper;
import com.smartContactManager.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap){
        modelMap.put("contacts", contactService.findAll());
        return "contact/index";
    }

    @RequestMapping(value = "/barcode/{id}" , method = RequestMethod.GET)
    public void barcode(ModelMap modelMap, @PathVariable("id") String id, HttpServletResponse response) throws Exception{
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getBarCodeImage(id, 200, 200));
        outputStream.flush();
        outputStream.close();
    }

}
