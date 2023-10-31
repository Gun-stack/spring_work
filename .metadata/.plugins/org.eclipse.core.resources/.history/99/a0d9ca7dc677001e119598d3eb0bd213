package com.kosta.di.sample2;

public class MessageBeanImpl implements MessageBean {
	private String name;
	private String greeting;
	private OutPutter outputter;
	
	public OutPutter getOutputter() {
		return outputter;
	}
	public void setOutputter(OutPutter outputter) {
		this.outputter = outputter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	@Override
	public void sayHello() {
		String message =greeting+","+name;
		System.out.println(message);
		try {
			outputter.output(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
