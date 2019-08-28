package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class EntityPanel extends JPanel {

	private static final long serialVersionUID = -2832412300102612960L;
	private boolean drag = false;
	private Point dragLocation = new Point();

	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static Cursor eResizeCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
	private static Cursor seResizeCursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
	private static Cursor sResizeCursor = new Cursor(Cursor.S_RESIZE_CURSOR);

	public EntityPanel() {
		setBackground(Color.WHITE);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				drag = true;
				dragLocation = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				drag = false;
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.getX() > getWidth() - 10 && e.getY() > getHeight() - 10) {
					setCursor(seResizeCursor);
				} else if (e.getX() > getWidth() - 10) {
					setCursor(eResizeCursor);
				} else if (e.getY() > getHeight() - 10) {
					setCursor(sResizeCursor);
				} else {
					setCursor(defaultCursor);
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (drag) {
					if (dragLocation.getX() > getWidth() - 10 // se
							&& dragLocation.getY() > getHeight() - 10) {
						setSize((int) (getWidth() + (e.getPoint().getX() - dragLocation
								.getX())), (int) (getHeight() + (e.getPoint()
								.getY() - dragLocation.getY())));
						dragLocation = e.getPoint();
					} else if (dragLocation.getX() > getWidth() - 10 // e
							&& dragLocation.getY() <= getHeight() - 10) {
						setSize((int) (getWidth() + (e.getPoint().getX() - dragLocation
								.getX())), getHeight());
						dragLocation = e.getPoint();
					} else if (dragLocation.getY() > getHeight() - 10) { // s
						setSize(getWidth(), (int) (getHeight() + (e.getPoint()
								.getY() - dragLocation.getY())));
						dragLocation = e.getPoint();
					} else {
						setLocation((int) (getX() + (e.getPoint().getX() - dragLocation
								.getX())), (int) (getY() + (e.getPoint()
								.getY() - dragLocation.getY())));
					}
					getParent().repaint();
				}
			}
		});
	}
}
