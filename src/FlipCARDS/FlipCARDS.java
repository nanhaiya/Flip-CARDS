package FlipCARDS;



import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class FlipCARDS extends JFrame {

	private JPanel contentPane;
	JButton btn_exit;//�˳�
	JButton btn_restart;//���¿�ʼ
	JButton btn_start;//��ʼ��Ϸ
	int rows=20;  //��
	int cols=20;  //��
	JButton jb[]=new JButton[rows*cols];  //�ƿ�
	Color[] cl;//��ɫ��
	Random rd; //�����
	int nextPai[]=new int[rows*cols];   //����˳��
	
	Thread tr;  
	fanPai fp;  //��Ϸ�߳�
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlipCARDS frame = new FlipCARDS();
					frame.setLocationRelativeTo(frame);
					frame.setTitle("����");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public FlipCARDS() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				btn_exit.doClick();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 636, 709);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel myPanel = new JPanel();
		myPanel.setBounds(0, 0, 618, 617);
		contentPane.add(myPanel);
		
		//��ʼ��Ϸ
		btn_start = new JButton("\u5F00\u59CB\u6E38\u620F");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playGame();
				
				//�߳̿�ʼ
				fp=new fanPai();
				tr=new Thread(fp);
				tr.start();
			}
		});
		btn_start.setBounds(67, 623, 123, 34);
		contentPane.add(btn_start);
		
		//���¿�ʼ
		btn_restart = new JButton("\u91CD\u65B0\u5F00\u59CB");
		btn_restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<nextPai.length;i++) {
					nextPai[i]=0;
				}
				creatGamePanel();
				
			}
		});
		btn_restart.setBounds(257, 623, 113, 34);
		contentPane.add(btn_restart);
		
		//�˳�
		btn_exit = new JButton("\u9000\u51FA\u6E38\u620F");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res=JOptionPane.showConfirmDialog(null, "�Ƿ��˳���","ȷ��������",JOptionPane.YES_NO_OPTION);
			if(res==JOptionPane.YES_OPTION) {System.exit(0);}
			if(res==JOptionPane.NO_OPTION) {btn_restart.doClick();}
			}
		});
		btn_exit.setBounds(437, 623, 113, 34);
		contentPane.add(btn_exit);
		
		GridLayout gl=new GridLayout(rows, cols);
		myPanel.setLayout(gl);
		

		
		mosListener msl=new mosListener() ;
		
		//��ʼ�����
		for(int i=0;i<jb.length;i++) {
				jb[i]=new JButton();
				jb[i].setOpaque(true);
				jb[i].setBackground(Color.WHITE);
				jb[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				jb[i].addActionListener(msl);
				jb[i].setActionCommand(i+"");
				myPanel.add(jb[i]);
		}
		
	
		
		creatGamePanel();
		
	}
	
	
	//��ԭ��ɫ���ɰ�ɫ��������������ͬ��������ɫ����
	public void creatGamePanel() {
		cl=new Color[rows*cols];
		rd=new Random();
		
		for(int i=0;i<cl.length;i++) {
			cl[i]=new Color(255,255,255);
		}

	    //����������ͬ����ɫ����
		int zong=rows*cols;
		for(int i=1;i<cl.length+1;i++) {
			if(i>1&&i%2==0) {
			cl[i-1]=cl[i-2]=new Color(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255));
			}
		}
         
		//����˳��
		for(int index=cl.length-1;index>=0;index--) {
			int i=rd.nextInt(index+1);
			Color temp=cl[i];
			cl[i]=cl[index];
			cl[index]=temp;
		}

		
		//��ӡ
		for(int i=0;i<jb.length;i++) {
				jb[i].setBackground(cl[i]);
		}
		

		
		
	}
	
	public void playGame() {
		//��ť��ɫ�ĳɺ�ɫ
		for(int i=0;i<jb.length;i++) {
			jb[i].setBackground(Color.DARK_GRAY);
		}
		
		
		
	}
	
	public class mosListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
           System.out.println(e.getActionCommand());
           //��ñ�ǩ
			int index=Integer.parseInt(e.getActionCommand());
			//�ı䰴ť��ɫ
			jb[index].setBackground(cl[index]);
			
			//�����Ƿ񷭳���ͬ��ɫ��
			SamePai(index);
						
			//�߳��Ƿ���ţ������½��߳�
			if(tr.isAlive()==false) {
				tr=new Thread(fp);
				tr.start();
			}
			
			if(winGame()==true) {winChoose();}
           
//			if(index==rows*cols-1) {
//				for(int i=0;i<nextPai.length;i++) {
//					System.out.println(nextPai[i]);
//				}
//			}
			
			
		}
	
	}
	
	//��Ϸ�߳�ʵ��
	public class fanPai implements Runnable{

		@Override
		public void run() {
			
           while(cunzai()) {
        	   try {
				Thread.sleep(500);          //������ʱ
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	   //����
        	   jb[ nextPai[0]].setBackground(Color.DARK_GRAY);
        	   
        	   //��˳��������ȡ����������ǰ��
        	   for(int i=0;i<rows*cols-1;i++) {
        		   nextPai[i]=nextPai[i+1];
        	   }
               nextPai[rows*cols-1]=0;
        	  
           }
			
		}
		
	}
	
	//�ж��Ƿ��д�����δ���ص�
	public boolean cunzai() {
		boolean cunZaiTure=false;
//		boolean ting=true;
//		int i=0;
//		while(ting) {
//			if(jb[i].getBackground()!=Color.DARK_GRAY) {
//				ting=false;
//				cunZaiTure=true;
//			}
//			++i;
//		}
		if(nextPai[0]!=0) {cunZaiTure=true;}
		
		return cunZaiTure;
	}
	
	//����˳���������
	public void cunRu(int next) {
		int index=0;
		while(nextPai[index]!=0||index>=rows*cols-1) {
			index++;
		}
       nextPai[index]=next;		
	}
	
	//������ƴ�����ɫ��ͬ�����˳��������ȡ��
	public void SamePai(int index) {
		for(int i=0;i<nextPai.length;i++) {
//			if(i<nextPai.length) {
//			if(cl[index].getBlue()==cl[nextPai[i]].getBlue()&&cl[index].getGreen()==cl[nextPai[i]].getGreen()&&cl[index].getRed()==cl[nextPai[i]].getRed()) {
//				nextPai[i]=0;
//				for(int j=i;j<rows*cols-1;j++) {
//					nextPai[j]=nextPai[j+1];
//				}
//				
//				for(int k=0;k<nextPai.length;k++) {
//					if(nextPai[k]==index) {nextPai[k]=0;break;}
//				}
//				break;	
//			}
//			}if(i==nextPai.length) {			
//				//����˳������
//				cunRu(index);}
			if(i>0) {
			if(nextPai[i]==0) {
				if(cl[index].getBlue()==cl[nextPai[i-1]].getBlue()&&cl[index].getGreen()==cl[nextPai[i-1]].getGreen()&&cl[index].getRed()==cl[nextPai[i-1]].getRed()) {
					nextPai[i-1]=0;break;
				}else {
					cunRu(index);break;
				}
			}}
			
		}
		
	}
	
	
	//�ж���Ϸ����
	public boolean winGame() {
		boolean gameWin=false;
		int colorPai=0;
		for(int i=0;i<jb.length;i++) {
			if(jb[i].getBackground()!=Color.DARK_GRAY) {++colorPai;}
		}
		if(colorPai==rows*cols) {
			gameWin=true;
		}
		
		return gameWin;
	}
	
	public void winChoose() {
		int res=JOptionPane.showConfirmDialog(null, "��ʤ���ˣ�","�Ƿ����¿�ʼ��",JOptionPane.YES_NO_OPTION);
	    if(res==JOptionPane.YES_OPTION) {btn_restart.doClick();}
	    if(res==JOptionPane.NO_OPTION) {btn_exit.doClick();}
	}
	
	
	
	}
	
