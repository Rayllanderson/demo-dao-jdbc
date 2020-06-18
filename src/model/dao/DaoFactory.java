package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    
    public static SellerDao createSellerDao() {
	//isso aq é criado pra retornar um jdbc, mas caso eu queria mudar, colocar outro sem ser JDBC, eu só mudo aqui e gg
	// like new SellerDaoMongoDB. saca? ai ja muda tudo automaticamente lá, xD izi. fácil manutençãp demais
	return new SellerDaoJDBC();
    }

}
