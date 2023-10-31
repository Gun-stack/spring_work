package com.kosta.di.sample3;

public class EmployeeImpl implements Employee {
	private Integer id;
	private String name;
	private Department department;
	
	public EmployeeImpl(Integer id, String name) {
		this.id =id;
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}


	@Override
	public void info() {
		System.out.println(String.format("사원번호 : %d,  이름 : %s,  부서 : %S, 부서위치: %s, 부서번호 : %d",id,name,department.getName(),department.getLocation(),department.getId()));
	}
	
	
}