package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuSelectorPanel extends JPanel{
	private boolean opened;
	
	
	public MenuSelectorPanel() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setPreferredSize(new Dimension(800,50));
		setBackground(ProgramFrame.getBgcolor());
		JButton hideShowButton = new JButton();
		hideShowButton.setPreferredSize(new Dimension(40,40));
		try {
		    Image img = ImageIO.read(getClass().getResource("/ui/assets/menuButton.png"));
		    Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		    hideShowButton.setIcon(new ImageIcon(newimg));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		Dimension buttonSize = new Dimension(100,40);
		JButton productInfoButton = new JButton("Products");
		productInfoButton.setPreferredSize(buttonSize);
		JButton customersButton = new JButton("Customers");
		customersButton.setPreferredSize(buttonSize);
		JButton providersButton = new JButton("Providers");
		providersButton.setPreferredSize(buttonSize);
		JButton salesButton = new JButton("Sales");
		salesButton.setPreferredSize(buttonSize);
		JButton purchasesButton = new JButton("Purchases");
		purchasesButton.setPreferredSize(buttonSize);
		JButton btn = new JButton("test");
		add(productInfoButton);
		add(customersButton);
		add(providersButton);
		add(salesButton);
		add(purchasesButton);
		add(hideShowButton);
		JLabel connectionIndicator = new DatabaseConnectionIndicator();
		add(connectionIndicator);
		
		opened = false;
		productInfoButton.setVisible(opened);
		customersButton.setVisible(opened);
		providersButton.setVisible(opened);
		salesButton.setVisible(opened);
		purchasesButton.setVisible(opened);
		
		hideShowButton.addActionListener(e -> {
			if(opened) opened = false;
			else opened = true;
			productInfoButton.setVisible(opened);
			customersButton.setVisible(opened);
			providersButton.setVisible(opened);
			salesButton.setVisible(opened);
			purchasesButton.setVisible(opened);
		});
		
		productInfoButton.addActionListener(f -> ProgramFrame.changePanel(new ProductInfoPanel()));
		customersButton.addActionListener(g -> ProgramFrame.changePanel(new CustomersPanel()));
		providersButton.addActionListener(h -> ProgramFrame.changePanel(new ProvidersPanel()));
		salesButton.addActionListener(i -> ProgramFrame.changePanel(new SalesPanel()));
		purchasesButton.addActionListener(j -> ProgramFrame.changePanel(new PurchasesPanel()));
	}
}
