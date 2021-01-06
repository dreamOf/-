package com.cs.controller;

import com.cs.model.res.BaseResultBean;
import com.cs.model.res.R;
import com.cs.model.res.TableRBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/em")
@Api(tags = "测试")
public class Example {
    @GetMapping("/m1")
    public String example(){
      return "hi boy!";
    }

    public static void main(String[] args) throws JsonProcessingException {
        TableRBean tableRBean = new TableRBean().setTotal(200).setRows(new ArrayList<>());
        BaseResultBean baseResultBean = R.tableR(tableRBean);
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(objectMapper.writeValueAsString(baseResultBean));
    }
}
