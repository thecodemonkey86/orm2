package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DesignerPanel extends JPanel{

	private static final long serialVersionUID = 8967247944227973521L;

	private ArrayList<EntityPanel> entityPanels;
	
	public DesignerPanel() {
		super(null);
		entityPanels = new ArrayList<>();
		setDoubleBuffered(true);
	}
	
	public void addEntity(EntityPanel entityPanel) {
		add(entityPanel);
		this.entityPanels.add(entityPanel);
	}
	
@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d=(Graphics2D) g;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
	g.clearRect (0, 0, getWidth(), getHeight());
	
	 g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	if (entityPanels.size()>=2) {
		EntityPanel e0=entityPanels.get(0);
		EntityPanel e1=entityPanels.get(1);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(e0.getX()+e0.getWidth()/2, e0.getY(), e1.getX()+e1.getWidth()/2, e1.getY());
	}
}
	
}
