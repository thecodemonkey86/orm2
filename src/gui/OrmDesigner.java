package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class OrmDesigner extends JFrame{

	private static final long serialVersionUID = -3024407768342115480L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		new OrmDesigner();
	}
	
	DesignerPanel designerPanel;
	
	public OrmDesigner() {
		super("ORM Designer");
		setSize(1024, 768);
		
		designerPanel = new DesignerPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(designerPanel,BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton btnAdd = new JButton("Add Entity");
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EntityPanel entityPanel = new EntityPanel();
				entityPanel.setSize(100, 100);
				entityPanel.setLocation(10,10);
				designerPanel.addEntity(entityPanel);
				OrmDesigner.this.repaint();
			}
		});
		bottomPanel.add(btnAdd);
		getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		setVisible(true);
	}
}
