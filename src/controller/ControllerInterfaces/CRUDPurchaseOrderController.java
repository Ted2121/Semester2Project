package controller.ControllerInterfaces;

import java.util.HashMap;
import java.util.List;


import model.LineItem;
import model.Product;
import model.PurchaseOrder;

public interface CRUDPurchaseOrderController extends RetrievingSubsetController{
	void updatePurchaseOrder(PurchaseOrder purchaseOrder);
	void deletePurchaseOrder(PurchaseOrder purchaseOrder);
	
	void deleteLineItemFromPurchaseOrder(LineItem lineItem);
	void addProductToPurchaseOrder(Product selectedProduct, int quantity);
	void deleteProductInProductToAdd(Product product);
	
	boolean isProductAlreadyInThePurchaseOrder(Product selectedProduct);
	
	String[][] retrieveTableData();
	HashMap<Integer, PurchaseOrder> retrieveIdRelatedToPurchaseOrderHashMap();
	
	List<LineItem> finAllLineItemRelatedToThisPurchaseOrder(PurchaseOrder purchaseOrder);
	List<PurchaseOrder> findAllPurchaseOrder();
	
	
	
}
