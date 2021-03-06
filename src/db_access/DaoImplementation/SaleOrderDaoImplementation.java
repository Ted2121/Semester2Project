package db_access.DaoImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_access.DBConnection;
import db_access.DaoFactory;
import db_access.DaoInterfaces.SaleOrderDao;
import model.*;

public class SaleOrderDaoImplementation implements SaleOrderDao {

	Connection connection = DBConnection.getInstance().getDBCon();

	private List<SaleOrder> buildObjects(ResultSet rs, boolean retrieveCustomer, boolean retrieveLineItem) throws Exception{
		List<SaleOrder> saleOrderList = new ArrayList<>();
		while(rs.next()) {
			SaleOrder retrievedSaleOrder = buildObject(rs);
			if(retrieveCustomer) {
				Customer retrievedCustomerLinkedToThisSaleOrder = DaoFactory.getCustomerDao().findCustomerById(rs.getInt("FK_Customer"), false);
				retrievedSaleOrder.setCustomer(retrievedCustomerLinkedToThisSaleOrder);
			}

			if(retrieveLineItem) {
				retrievedSaleOrder.setLineItems(new ArrayList<LineItem>());
				for(LineItem lineItem : DaoFactory.getLineItemDao().findLineItemsByOrder(retrievedSaleOrder, true)) {
					retrievedSaleOrder.getLineItems().add(lineItem);
				}
			}

			saleOrderList.add(retrievedSaleOrder);

		}

		return saleOrderList;
	}

	private SaleOrder buildObject(ResultSet rs) throws SQLException{
		SaleOrder builtObject = ModelFactory.getSaleOrderModel(rs.getInt("PK_idSaleOrder"));
		builtObject.setOrderPrice(rs.getInt("Price"));
		builtObject.setOrderDateTime(rs.getString("DateTime"));

		return builtObject;
	}

	@Override
	public SaleOrder findSaleOrderById(int saleOrderId, boolean retrieveCustomer, boolean retrieveLineItem) throws Exception {

		String query = "SELECT * FROM SaleOrder INNER JOIN [Order] ON SaleOrder.PK_idSaleOrder = [Order].PK_idOrder WHERE PK_idSaleOrder = ? ";
		PreparedStatement preparedSelectStatement = connection.prepareStatement(query);
		preparedSelectStatement.setInt(1, saleOrderId);
		ResultSet rs = preparedSelectStatement.executeQuery();
		SaleOrder retrievedSaleOrder = null;
		List<SaleOrder> retrievedList = buildObjects(rs, retrieveCustomer, retrieveLineItem);
		if(retrievedList.size()>0)
			retrievedSaleOrder = retrievedList.get(0);
		if(retrievedList.size()>1) {
			throw new Exception("More than 1 item in the retrieved list of SaleOrder");
		}

		return retrievedSaleOrder;
	}

	@Override
	public List<SaleOrder> findAllSaleOrders(boolean retrieveCustomer, boolean retrieveLineItem) throws Exception {
		String query = "SELECT * FROM SaleOrder INNER JOIN [Order] ON SaleOrder.PK_idSaleOrder = [Order].PK_idOrder";
		PreparedStatement preparedSelectStatement = connection.prepareStatement(query);

		ResultSet rs = preparedSelectStatement.executeQuery();
		ArrayList<SaleOrder> retrievedSaleOrderList = (ArrayList<SaleOrder>) buildObjects(rs, retrieveCustomer, retrieveLineItem);

		return retrievedSaleOrderList;
	}
	
	@Override
	public List<SaleOrder> findSaleOrderByCustomerId(int customerId, boolean retrieveCustomer, boolean retrieveLineItem) throws Exception {
		String query = "SELECT * FROM SaleOrder INNER JOIN [Order] ON SaleOrder.PK_idSaleOrder = [Order].PK_idOrder WHERE FK_Customer = ? ";
		PreparedStatement preparedSelectStatement = connection.prepareStatement(query);
		preparedSelectStatement.setInt(1, customerId);
		ResultSet rs = preparedSelectStatement.executeQuery();
		ArrayList<SaleOrder> retrievedSaleOrders = (ArrayList<SaleOrder>) buildObjects(rs, retrieveCustomer, retrieveLineItem);

		return retrievedSaleOrders;
	}

	@Override
	public void createSaleOrder(SaleOrder objectToInsert) throws SQLException {
		String sqlInsertOrderStatement = "INSERT INTO SaleOrder(PK_idSaleOrder, FK_Customer) VALUES (? , ?)";
		PreparedStatement preparedSqlInsertOrderStatement = connection.prepareStatement(sqlInsertOrderStatement) ;
		preparedSqlInsertOrderStatement.setInt(1, objectToInsert.getId());
		preparedSqlInsertOrderStatement.setInt(2, objectToInsert.getCustomer().getId());

		preparedSqlInsertOrderStatement.execute();
	}

	@Override
	public void updateSaleOrder(SaleOrder objectToUpdate) throws SQLException {
		String sqlUpdateSaleOrderStatement = "UPDATE SaleOrder SET FK_Customer = ? WHERE PK_idSaleOrder = ?";
		PreparedStatement preparedUpdateProductStatement = connection.prepareStatement(sqlUpdateSaleOrderStatement);
		preparedUpdateProductStatement.setInt(1, objectToUpdate.getCustomer().getId());
		preparedUpdateProductStatement.setInt(2, objectToUpdate.getId());
		preparedUpdateProductStatement.execute();

	}

	@Override
	public void deleteSaleOrder(SaleOrder objectToDelete) throws SQLException {
		String sqlDeleteSaleOrderStatement = "DELETE FROM SaleOrder WHERE PK_idSaleOrder = ?";
		PreparedStatement preparedDeleteOrderStatement = connection.prepareStatement(sqlDeleteSaleOrderStatement);
		preparedDeleteOrderStatement.setInt(1, objectToDelete.getId());
		preparedDeleteOrderStatement.execute();
	}
}
