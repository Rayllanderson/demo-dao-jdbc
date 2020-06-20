package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
	this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
	PreparedStatement st = null;

	try {
	    st = conn.prepareStatement("insert into Department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
	    st.setString(1, obj.getName());
	    int rows = st.executeUpdate();
	    
	    if(rows > 0 ) {
		ResultSet rs = st.getGeneratedKeys();
		if (rs.next()) {
		    int id = rs.getInt(1);
		    obj.setId(id);
		}
		DB.closeResultSet(rs);
	    }
	    
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeStatement(st);
	}
    }

    @Override
    public void update(Department obj) {
	PreparedStatement st = null;
	try {    
	    st = conn.prepareStatement(
			"UPDATE Department " +
			"SET Name = ? " +
			"WHERE Id = ?");
	    
	    st.setInt(2, obj.getId());
	    st.setString(1, obj.getName());
	    st.executeUpdate();
	    
	}catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeStatement(st);
	}
    }

    @Override
    public void deleteById(Integer id) {
	
	PreparedStatement st = null;
	try {
	    st = conn.prepareStatement("delete from Department where Id = " + id);
	    st.executeUpdate();
	}catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeStatement(st);
	}
    }

    @Override
    public Department findById(Integer id) {

	PreparedStatement st = null;
	ResultSet rs = null;

	try {
	    st = conn.prepareStatement("SELECT * FROM Department where Id = " + id);
	    rs = st.executeQuery();

	    Department dep = new Department();
	    if (rs.next()) {
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
	    }
	    return dep;
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	} finally {
	    DB.closeResultSet(rs);
	    DB.closeStatement(st);
	}
    }

    @Override
    public List<Department> findAll() {
	PreparedStatement st = null;
	ResultSet rs = null;

	try {
	    st = conn.prepareStatement("select * from Department");
	    rs = st.executeQuery();

	    List<Department> list = new ArrayList<>();

	    while (rs.next()) {
		list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
	    }

	    return list;

	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	} finally {
	    DB.closeResultSet(rs);
	    DB.closeStatement(st);
	}
    }
}
