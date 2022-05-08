package test.DaoTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import db_access.DaoFactory;
import db_access.DaoInterfaces.*;
import model.*;
import model.Product.*;

public class TestProductInformationDaoImplementation {
	private static ProductInformationDao productInformationDao;
	private static ProductDao productDao;
	private static ProductInformation productInformationToUpdate;
	private static ProductInformation productInformationToDelete;
	private static ProductInformation productInformationToCreate;
	private static Product product1, product2, product3;
	
	@BeforeClass
	public static void setUp() throws SQLException {
		productInformationDao = DaoFactory.getProductInformationDao();
		productDao = DaoFactory.getProductDao();
		product1 = ModelFactory.getProductModel("Test1", 15d, 20d, WeightCategory.FIVE, Unit.KG);
		product2 = ModelFactory.getProductModel("Test2", 15d, 20d, WeightCategory.FIVE, Unit.KG);
		product3 = ModelFactory.getProductModel("Test3", 15d, 20d, WeightCategory.FIVE, Unit.KG);
		productDao.createProduct(product1);
		productDao.createProduct(product2);
		productDao.createProduct(product3);
		productInformationToUpdate = ModelFactory.getProductInformationModel(900, 14, product1.getId());
		productInformationToUpdate.setRelatedProduct(product1);
		productInformationToDelete = ModelFactory.getProductInformationModel(900, 14, product2.getId());
		productInformationToCreate = ModelFactory.getProductInformationModel(900, 14, product3.getId());
		productInformationDao.createProductInformation(productInformationToUpdate);
		productInformationDao.createProductInformation(productInformationToDelete);
	}
	
	@Test
	public void testCreateProductInformation() throws SQLException, Exception {
		productInformationDao.createProductInformation(productInformationToCreate);
		assertNotNull(productInformationDao.findProductInformationByProductId(product3.getId(), false));
	}
	
	@Test
	public void testUpdate() throws SQLException, Exception {
		productInformationToUpdate.setQuantity(50);
		productInformationDao.updateProductInformation(productInformationToUpdate);
		assertEquals(50, productInformationDao.findProductInformationByProduct(product1, false).getQuantity());
	}
	
	@Test
	public void testDelete() throws SQLException, Exception {
		productInformationDao.deleteProductInformation(productInformationToDelete);
		assertNull(productInformationDao.findProductInformationByProductId(product2.getId(), false));
	}
	
	@Test
	public void testFindAllWithoutAssociation() throws SQLException, Exception {
		List<ProductInformation> list =  productInformationDao.findAllProductInformation(false);
		int count = 0;
		for(ProductInformation p : list) {
			count++;
			assertTrue(p.getRelatedProduct()==null);
		}
		assertTrue(count>0);
	}
	
	@Test
	public void testFindAllWithAssocation() throws SQLException, Exception {
		List<ProductInformation> list =  productInformationDao.findAllProductInformation(true);
		int count = 0;
		for(ProductInformation p : list) {
			count++;
			assertTrue(p.getRelatedProduct().getId()==p.getId());
		}
		assertTrue(count>0);
	}
	
	@Test
	public void testFindByProductWithoutAssociation() throws SQLException, Exception {
		ProductInformation result = productInformationDao.findProductInformationByProduct(product1, false);
		assertEquals(result.getQuantity(), productInformationToUpdate.getQuantity());
		assertEquals(result.getLocationCode(), productInformationToUpdate.getLocationCode());
		assertTrue(result.getRelatedProduct()==null);
	}
	
	@Test
	public void testFindByProductWithAssociation() throws SQLException, Exception {
		ProductInformation result = productInformationDao.findProductInformationByProduct(product1, true);
		assertEquals(result.getQuantity(), productInformationToUpdate.getQuantity());
		assertEquals(result.getLocationCode(), productInformationToUpdate.getLocationCode());
		assertTrue(result.getRelatedProduct().getId()==product1.getId());
	}
	
	@Test
	public void testFindByProductNameWithoutAssociation() throws SQLException, Exception {
		List<ProductInformation> results = productInformationDao.findProductInformationByProductName(product1.getProductName(), false);
		assertNotNull("Shouldn't return a null object", results);
		for(ProductInformation p : results) {
			assertTrue(p.getRelatedProduct()==null);
		}
	}
	
	@Test
	public void testFindByProductNameWithAssociation() throws SQLException, Exception {
		List<ProductInformation> results = productInformationDao.findProductInformationByProductName(product1.getProductName(), true);
		assertNotNull("Shouldn't return a null object", results);
		for(ProductInformation p : results) {
			assertTrue(p.getRelatedProduct().getId()==p.getId());
		}
	}
	
	@Test
	public void testFindByProductIdWithoutAssociation() throws SQLException, Exception {
		ProductInformation result = productInformationDao.findProductInformationByProductId(product1.getId(), false);
		assertEquals(result.getQuantity(), productInformationToUpdate.getQuantity());
		assertEquals(result.getLocationCode(), productInformationToUpdate.getLocationCode());
		assertTrue(result.getRelatedProduct()==null);
	}
	
	@Test
	public void testFindByProductIdWithAssociation() throws SQLException, Exception {
		ProductInformation result = productInformationDao.findProductInformationByProductId(product1.getId(), true);
		assertEquals(result.getQuantity(), productInformationToUpdate.getQuantity());
		assertEquals(result.getLocationCode(), productInformationToUpdate.getLocationCode());
		assertTrue(result.getRelatedProduct().getId()==product1.getId());
	}
	
	@Test
	public void testAddOrRemoveQuantityToAProduct() throws SQLException, Exception {
		int quantityToAdd = 5;
		int quantityToRemove = 5;
		productInformationToUpdate.setQuantity(14);
		productInformationDao.updateProductInformation(productInformationToUpdate);
		productInformationDao.addQuantityToProduct(product1, quantityToAdd);
		ProductInformation updatedProductInfo = productInformationDao.findProductInformationByProduct(product1, false);
		assertTrue("Should return the value +"+quantityToAdd,updatedProductInfo.getQuantity() == (productInformationToUpdate.getQuantity() + quantityToAdd));
		productInformationToUpdate.setQuantity(14+quantityToAdd);
		productInformationDao.removeQuantityToProduct(product1, quantityToRemove);
		updatedProductInfo = productInformationDao.findProductInformationByProduct(product1, false);
		assertTrue("Should return the value -"+quantityToRemove,updatedProductInfo.getQuantity() == (productInformationToUpdate.getQuantity() - quantityToRemove));
		productInformationToUpdate.setQuantity(19-quantityToRemove);
	}
	
	@AfterClass
	public static void cleanUp() throws SQLException {
		productInformationDao.deleteProductInformation(ModelFactory.getProductInformationModel(900, 14, product3.getId()));
		productInformationDao.deleteProductInformation(productInformationToUpdate);
		productDao.deleteProduct(product1);
		productDao.deleteProduct(product2);
		productDao.deleteProduct(product3);
	}
}
