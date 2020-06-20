package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

    public static void main(String[] args) throws ParseException {
	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd");

	SellerDao sellerDao = DaoFactory.createSellerDao();
	
	System.out.println("=== Teste número #1 - Seller findById ===");
	Seller sell = sellerDao.findById(12);
	
	System.out.println(sell);
	
	System.out.println("\n=== Teste número #2 - Seller findByDepartment ===");
	Department department = new Department (3, null);
	List<Seller> list = sellerDao.findByDepartment(department);
	list.forEach(System.out::println);
	
	list.clear();
	System.out.println("\n=== Teste número #3 - Seller findAll ===");
	list = sellerDao.findAll();
	list.forEach(System.out::println);
	
	/*System.out.println("\n=== Teste número #4 - Seller Insert ===");
	Seller junin = new Seller(null, "Costa Neto", "fedora@gmail.com", new Date(), 3999.8, department);
	sellerDao.insert(junin);
	System.out.println("New id: " + junin.getId());
	list = sellerDao.findAll();
	list.forEach(System.out::println);
	
	/*System.out.println("\n=== Teste número #5 - Seller update ===");
	sell = sellerDao.findById(20);
	sell.setName("Fernando Maranhão");
	sell.setEmail("beiraMar321@gmail.com");
	sell.setDepartment(new Department(1, null));
	sell.setBaseSalary(2499.77);
	sellerDao.update(sell);
	list.forEach(System.out::println);*/
	
	System.out.println("\n=== Teste número #6 - Seller delet ===");
	sellerDao.deleteById(55);
    }
}
