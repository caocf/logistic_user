package com.cn.xtouch.fodelforuser.model;

import java.io.Serializable;

public class CurrentOrder  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public CurrentOrder(String orderNo,String phoneNum,String address,String name)
	{
		this.orderNo=orderNo;
		this.phoneNum=phoneNum;
		this.address=address;
		this.name=name;
	}
	
	public String  orderNo;
	
	public String phoneNum;
	
	public String address;
	
	public String name;

}
