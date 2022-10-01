package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class ScrollBarUI extends BasicScrollBarUI {
	@Override
	protected void configureScrollBarColors() {
		// ����
		trackColor = Color.darkGray;
	}
	
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		super.paintTrack(g, c, trackBounds);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		// ���һ��Ҫ���ϰ�����Ȼ�϶���ʧЧ��
		g.translate(thumbBounds.x, thumbBounds.y); 
		g.setColor(new Color( 0x303030 ));// ���ñ߿���ɫ
		g.drawRoundRect(5, 0, 6, thumbBounds.height-1, 5, 5); // ��һ��Բ�Ǿ���
		// �������
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.addRenderingHints(rh);
		// ��͸��
		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		// ���������ɫ�����������˽��䣬��������
		g2.setPaint(new GradientPaint(c.getWidth() / 2, 1, new Color( 0x303030 ), c.getWidth() / 2, c.getHeight(), new Color( 0x303030 )));
		// ���Բ�Ǿ���
		g2.fillRoundRect(5, 0, 6, thumbBounds.height-1, 5, 5);
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(0, 0));
		return button;
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(0, 0));
		return button;
	}
	
}
