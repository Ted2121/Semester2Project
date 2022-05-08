package controller.ControllerImplementation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.ControllerInterfaces.CRUDProductInformationController;
import db_access.DaoFactory;
import db_access.DaoInterfaces.*;
import model.ModelFactory;
import model.Product.*;
import model.*;

public class CRUDProductInformationControllerImplementation extends RetrievingSubsetControllerImplementation implements CRUDProductInformationController{
	private ProductDao productDao;
	private ProductInformationDao productInformationDao;
	
	List<List<Product>> productContainingLetter;
	
	public CRUDProductInformationControllerImplementation() {
		productDao = DaoFactory.getProductDao();
		productInformationDao = DaoFactory.getProductInformationDao();
	}

	@Override
	public List<Product> searchAllProductAndProductInformation() {
		// TODO Auto-generated method stub
		List<Product> list = new ArrayList<Product>();
		try {
			list = productDao.findAllProducts(false, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void createProductInformationAndProduct(String productName, double purchasingPrice, double sellingPrice, String unit,
			int weightCategory, int locationCode, int quantity) {
		// TODO Auto-generated method stub
		Product product;
		ProductInformation productInformation;
		try {
			if(weightCategory == 1) {
				product = ModelFactory.getProductModel(productName, purchasingPrice, sellingPrice, WeightCategory.valueOf("ONE"), Unit.valueOf(unit));
			}
			else if(weightCategory == 5) {
				product = ModelFactory.getProductModel(productName, purchasingPrice, sellingPrice, WeightCategory.valueOf("FIVE"), Unit.valueOf(unit));
			}
			else if(weightCategory == 10) {
				product = ModelFactory.getProductModel(productName, purchasingPrice, sellingPrice, WeightCategory.valueOf("TEN"), Unit.valueOf(unit));
			}
			else {
				Exception weighCategoryException = new Exception();
				throw weighCategoryException;
			}
			
			try {
				productDao.createProduct(product);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			productInformation = ModelFactory.getProductInformationModel(locationCode, quantity, product.getId());
			try {
				productInformationDao.createProductInformation(productInformation);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Weight Category is incorrect");
		}
	}

	@Override
	public void updateProductInformationAndProduct(Product product, int locationCode, int quantity) {
		// TODO Auto-generated method stub
		try {
			productDao.updateProduct(product);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProductInformation productInformation = ModelFactory.getProductInformationModel(locationCode, quantity, product.getId());
		try {
			productInformationDao.updateProductInformation(productInformation);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProductInformationAndProduct(Product product) {
		// TODO Auto-generated method stub
		ProductInformation productInformation;
		try {
			productInformation = productInformationDao.findProductInformationByProduct(product, false);
			productInformationDao.deleteProductInformation(productInformation);
			productDao.deleteProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}