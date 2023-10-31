package com.kosta.di.sample1;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Di2Application {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beaans1.xml");
		MessageBean bean = context.getBean("messageBean",MessageBean.class);
		bean.sayHello("Spring");
	}

}
