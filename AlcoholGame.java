import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

public class AlcoholGame extends JPanel implements Runnable{
	JPanel panel;
	Thread th;
	Graphics2D g;
	
	int dx, dy;
	User u = new User();
	
	int NUM = 6;
	
	Bottle bot[] = new Bottle[NUM];
	Apple app[] = new Apple[NUM];
	Mover move[] = new Mover[NUM];
	Mover move2[] = new Mover[NUM];
	
	boolean inside = false;
	Timer time[] = new Timer[NUM];
	
	Random rand = new Random();
	JButton start;
	
	static int FWIDTH = 1000, FHEIGHT = 700;
	Color c1 = new Color(242, 152, 41), c2 = new Color(255,237,175);
	
	int in = 0;
	int speed = 5;
	int lifes=5;
	
	Image icon;
	
	public AlcoholGame() {
		icon = new ImageIcon("pochayy2.png").getImage();

		u.setMx(100);
		u.setMy(110);
		setOpaque(false);
		
		setLayout(null);
		
		start = new JButton(new ImageIcon("GameStart2.png"));
		start.setBackground(new Color(127,127,127));
        start.setSize(210, 80);
        start.setLocation(400,350);
		start.setBorderPainted(false);
//		start.setFocusPainted(false);
//		start.setContentAreaFilled(false);
		
		 Image background = Toolkit.getDefaultToolkit().createImage("pochayy2.png");
		 this.drawImage(background, 0, 0, null);
		
		add(start);
		try {
			setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, 
					new Font("Tahoma",1,24), new Color(204,0,0)));
			for(LookAndFeelInfo inf : UIManager.getInstalledLookAndFeels()) {
				if(inf.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(inf.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		for (int i = 0; i < bot.length; ++i) {
			bot[i] = new Bottle();
			bot[i].setMy(-70);
			bot[i].reset();
		}
		for (int i = 0; i < app.length; ++i) {
			app[i] = new Apple();
			app[i].setMy(-50);
			app[i].reset();
		}
		
		for(in = 0; in<bot.length;++in) {
			move[in] = new Mover(bot[in], u);
			move[in].setInitialDelay((in+1)*1000);
			move[in].move();
		}
		
		for(in = 0; in<app.length;++in) {
			move2[in] = new Mover(app[in], u);
			move2[in].setInitialDelay((in+1)*1800);
			move2[in].move();
		}
		
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				dx = (int) me.getPoint().getX();
				if (u.contains(me.getPoint())) {
					inside = true;
				}
			}

			public void mouseReleased(MouseEvent me) {
				inside = false;
			}
		};
		
		addMouseListener(ml);

		MouseMotionListener mll = new MouseAdapter() {
			public void mouseDragged(MouseEvent me) {
				if (inside == true) {
					u.setMx((int) me.getPoint().getX());
				}
			}
		};
		
		addMouseMotionListener(mll);
//		th = new Thread(this);
		
		start.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				makeThread();
				th.start();
				start.setVisible(false);
			}
		});
	}
	
	private void drawImage(Image background, int i, int j, Object object) {
		// TODO Auto-generated method stub
		
	}

	public void makeThread() {
		th = new Thread(this);
	}
	
	public void paint(Graphics g2) {
		g = (Graphics2D) g2;
		super.paintComponent(g);
		icon = new ImageIcon("pochayy2.png").getImage();
		g.drawImage(icon, 0, 0, null);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		super.paint(g);
//		GradientPaint gpp = new GradientPaint(getWidth() / 2, 0, c1, getWidth() / 2, getHeight(), c2);
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, getWidth(), getHeight());
		u.setMy(getHeight()-110);
//		Polygon poly = new Polygon();
//		poly.addPoint((getWidth() / 3) / 2, getHeight() - 40);
//		poly.addPoint(getWidth() - (getWidth() / 3) / 2, getHeight() - 40);
//		poly.addPoint(getWidth() - 10, getHeight() - 10);
//		poly.addPoint(10, getHeight() - 10);
//		GradientPaint gp = new GradientPaint(getWidth() / 2, getHeight(), Color.WHITE, getWidth() / 2, getHeight() - 40,
//				new Color(106,190,255));
//		g.setPaint(gp);
//		g.fill(poly);
		u.drawOn(g);
		for (int i = 0; i < bot.length; ++i) {
			bot[i].drawOn(g);
			app[i].drawOn(g);
		}
	}
	
	public void run() {
		try {
			while (true) {
				th.sleep(speed);
				if(u.lifes ==0) {
					for(int i=0; i<bot.length;++i) {
						bot[i].reset();
						app[i].reset();
						move[i].stop();
						move2[i].stop();
					}
					JOptionPane.showMessageDialog(null, "당신의 점수는 " + u.score+"점");
					int a = JOptionPane.showConfirmDialog(null, "재시작", "게임 오버", JOptionPane.OK_CANCEL_OPTION);
					
					if(a==JOptionPane.OK_OPTION) {
						for (int i = 0; i < bot.length; ++i) {
							move[i].setInitialDelay((i+1)*1300);
							move[i].move();
						}
						for (int i = 0; i < app.length; ++i) {
							move2[i].setInitialDelay((i+1)*1700);
							move2[i].move();
						}
						u.score = -10;
						u.lifes = 5;
						u.updateScore();
					}
					if(a == JOptionPane.CANCEL_OPTION) {
						System.exit(0);
					}
				}
				repaint();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
}
