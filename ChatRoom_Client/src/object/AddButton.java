package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.UIManager;

public class AddButton extends JButton{
	private static final long serialVersionUID = 1L;

	public boolean is_exit = true;
	
	public AddButton() {
		this.setPreferredSize(new Dimension(40, 40));
		this.setBackground(Color.DARK_GRAY);
		this.setText("+");
		this.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 40));
		this.setFocusPainted(false);// ���ò�Ҫ���㣨���ֵı߿�
		this.setBorder(null);
		this.setForeground(Color.WHITE);

		// ���ð��µ���ɫ
		UIManager.put("Button.select", new Color(220, 220, 220));
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(Color.DARK_GRAY);
				is_exit = true;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(new Color(192, 192, 192));
				is_exit = false;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

}
