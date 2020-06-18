package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

    public static void main(String[] args) {

	SellerDao sellerDao = DaoFactory.createSellerDao();
	
	System.out.println("=== Teste número #1 - Seller findById ===");
	Seller sell = sellerDao.findById(12);
	
	System.out.println(sell);

    }
}
