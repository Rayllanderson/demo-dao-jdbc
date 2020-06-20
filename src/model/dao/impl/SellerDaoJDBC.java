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
	try {
	    
	    st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
		    + "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
	  
	    this.insertSeller(st, obj);
	    
	    int rows = st.executeUpdate();
	    
	    if (rows > 0) {
		ResultSet rs = st.getGeneratedKeys();
		if (rs.next()) {
		    int id = rs.getInt(1);
		    obj.setId(id);
		    System.out.println("Done!");
		}
		DB.closeResultSet(rs);
	    }
	    else
		throw new DbException("Erro inesperado! Nenhuma linha afetada");
	   
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeStatement(st);
	}
    }

    @Override
    public void update(Seller obj) {
	PreparedStatement st = null;
	
	try {
	    st = conn.prepareStatement("UPDATE seller " + 
	    	"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + 
	    	"WHERE Id = ?");
	    
	    insertSeller(st, obj);
	    st.setInt(6, obj.getId());
	    
	    st.executeUpdate();
	    
	}catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally{
	    DB.closeStatement(st);
	}

    }

    @Override
    public void deleteById(Integer id) {
	
	PreparedStatement st = null;
	
	try {  
	    st = conn.prepareStatement("DELETE FROM seller " + 
	    	"WHERE Id = ?");
	    st.setInt(1, id);
	    
	    int row = st.executeUpdate();
	    
	    if (row == 0) {
		throw new DbException("Ops, id não existe ou aconteceu um erro inesperado");
	    }
	    
	}catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeStatement(st);
	}
    }

    @Override
    public Seller findById(Integer id) {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {

	    st = conn.prepareStatement(
		    "SELECT seller.*,department.Name as DepName" + " FROM seller INNER JOIN department"
			    + " ON seller.DepartmentId = department.Id" + " WHERE seller.Id = ?");

	    st.setInt(1, id);

	    rs = st.executeQuery();

	    if (rs.next()) {
		Department dep = instanciarDepartment(rs);

		Seller obj = instanciarSeller(rs, dep);

		return obj;
	    }
	    return null;
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	} finally {
	    DB.closeStatement(st);
	    DB.closeResultSet(rs);
	}

    }

    @Override
    public List<Seller> findAll() {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {

	    st = this.conn.prepareStatement(
		    "SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
			    + "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

	    rs = st.executeQuery();

	    List<Seller> list = new ArrayList<>();
	    Map<Integer, Department> map = new HashMap<>();

	    while (rs.next()) {
		Department dep = map.get(rs.getInt("DepartmentId"));
		if (dep == null) {
		    dep = instanciarDepartment(rs);
		    map.put(rs.getInt("DepartmentId"), dep);
		}
		Seller seller = instanciarSeller(rs, dep);
		list.add(seller);
	    }
	    return list;
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	} finally {
	    DB.closeResultSet(rs);
	    DB.closeStatement(st);
	}
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {

	    st = conn.prepareStatement(
		    "SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
			    + "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

	    st.setInt(1, department.getId());

	    rs = st.executeQuery();

	    List<Seller> list = new ArrayList<>();
	    Map<Integer, Department> map = new HashMap<>();

	    while (rs.next()) {

		Department dep = map.get(rs.getInt("DepartmentId"));

		if (dep == null) {
		    dep = instanciarDepartment(rs);
		    map.put(rs.getInt("DepartmentId"), dep);
		}

		Seller obj = instanciarSeller(rs, dep);
		list.add(obj);
	    }

	    return list;

	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	} finally {
	    DB.closeStatement(st);
	    DB.closeResultSet(rs);
	}
    }
   
    private void insertSeller(PreparedStatement st, Seller obj) throws SQLException {
	st.setString(1, obj.getName());
	st.setString(2, obj.getEmail());
	st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
	st.setDouble(4, obj.getBaseSalary());
	st.setInt(5, obj.getDepartment().getId());
    }

    private Seller instanciarSeller(ResultSet rs, Department dep) throws SQLException {
	Seller obj = new Seller();
	obj.setId(rs.getInt("Id"));
	obj.setName(rs.getString("Name"));
	obj.setEmail(rs.getString("Email"));
	obj.setBaseSalary(rs.getDouble("BaseSalary"));
	obj.setBirthDate(rs.getDate("BirthDate"));
	obj.setDepartment(dep);
	return obj;
    }

    private Department instanciarDepartment(ResultSet rs) throws SQLException {
	Department dep = new Department();
	dep.setId(rs.getInt("DepartmentId"));
	dep.setName(rs.getString("DepName"));
	return dep;
    }
}
