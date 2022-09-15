package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		ResultSet rs =null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Nome, Email, BirthDate, BaseSalary, DepartmentId) "
					+ " VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffectd = st.executeUpdate();
			
			if(rowsAffectd > 0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Erro inesperado!!! Sem linhas afetadas");
			}	
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
    public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Nome=?, Email=?, BirthDate=?, BaseSalary=?, DepartmentId=? "
					+ "WHERE Id=?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
    public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller "
					+ "WHERE Id=?");
			st.setInt(1, id);

			st.executeUpdate();
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	
	@Override
    public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.nome as DepName " 
					+"FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = Department.Id " 
					+"WHERE seller.Id=?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			//instanciando a tabela para que se tornem objetos
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSelles(rs, dep);
				return obj;	
			}
			else {
				return null;
			}
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Seller instantiateSelles(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Nome"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		obj.setDepartment(dep); // fazendo a jun��o das tabelas
		return obj;
	}


	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		    Department dep = new Department();
			dep.setId(rs.getInt("DepartmentId"));
			dep.setNome(rs.getString("DepName"));
		return dep;
	}


	@Override
    public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.nome as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = Department.Id "
					+ "ORDER BY nome");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			//instanciando a tabela para que se tornem objetos
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				  if(dep == null) {
					  dep = instantiateDepartment(rs);
					  map.put(rs.getInt("DepartmentId"), dep);
				  }
				  
				Seller obj = instantiateSelles(rs, dep);
				list.add(obj);	
			}
			return list;
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.nome as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = Department.Id "
					+ "WHERE DepartmentId=? "
					+ "ORDER BY nome");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			//instanciando a tabela para que se tornem objetos
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				  if(dep == null) {
					  dep = instantiateDepartment(rs);
					  map.put(rs.getInt("DepartmentId"), dep);
				  }
				  
				Seller obj = instantiateSelles(rs, dep);
				list.add(obj);	
			}
			return list;
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}
