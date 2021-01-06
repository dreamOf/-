package com.cs.model.res;

public class R extends BaseResultBean{
    Object data;
    public static BaseResultBean ok(){
       return  new BaseResultBean(ResponseCode.SUCEESS.getCode(),"");
    }
    public static BaseResultBean ok(String message){
        return  new BaseResultBean(ResponseCode.SUCEESS.getCode(),message);
    }
    public static BaseResultBean fail(){
        return  new BaseResultBean(ResponseCode.FAIL.getCode(),"");
    }
    public static BaseResultBean fail(String message){
        return  new BaseResultBean(ResponseCode.FAIL.getCode(),message);
    }
    public static BaseResultBean tableR(Object data){
        R r = new R();
        r.setCode(ResponseCode.FAIL.getCode());
        r.setMessage("查询成功！");
        r.setData(data);
        return  r;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
