package model.servicies;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Livros"));
		list.add(new Department(2, "Infromatica"));
		list.add(new Department(3, "Eletrodomesticos"));
		list.add(new Department(4, "Moveis"));
		
		return list;
	}

}
