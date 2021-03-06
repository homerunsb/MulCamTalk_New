package com.mc.mctalk.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mc.mctalk.chatserver.ChattingClient;
import com.mc.mctalk.chatserver.ChattingController;
import com.mc.mctalk.view.uiitem.CustomJScrollPane;
import com.mc.mctalk.view.uiitem.CustomTitlebar;
import com.mc.mctalk.view.uiitem.LogoManager;
import com.mc.mctalk.view.uiitem.RoundedImageMaker;
import com.mc.mctalk.vo.UserVO;

public class CreatingChattingRoomPanel extends JFrame {
	private RoundedImageMaker imageMaker = new RoundedImageMaker();
	private JPanel entirePanel = new JPanel();
	private JPanel middlePanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private CustomJScrollPane ChoiceFriendListScrollPanel ;
	private JScrollPane middleChoiceFriendListScrollPanel = new JScrollPane();
	private JLabel middleChoiceFriendListLabel = new JLabel();
	private JTextField middleSerchFriendListTextField = new JTextField(10);
	private JPanel middleSerchSpace = new JPanel();
	private JPanel middleSelectedFriendListPanel = new JPanel();
	private FriendsListPanel friendListPannel;
	private JList<UserVO> selectedList;
	private JButton closeBtn = new JButton("X"); // 나중에 이미지로 주면 이쁠것같다.
	private JButton confirmBtn = new JButton("확인");
	private JButton cancelBtn = new JButton("취소");
	private DefaultListModel<UserVO> listmodel = new DefaultListModel<>();
	private JLabel topPanelLabel = new JLabel();
	private JLabel topCountLabel = new JLabel("" + count);
	private JPanel titlepanel = new JPanel();
	private static int count = 0;
	private Color backGroundColor = new Color(255, 255, 255);
	private Color selectedColor1 = new Color(64, 224, 208); // 아름다운 푸른색 찾아보기.
	private Font grayFont = new Font("dialog", Font.BOLD, 12);
	private ChattingClient client;
	private MainFrame mainFrame;
	private	JPanel panelAdd = new JPanel();
	public CreatingChattingRoomPanel(ChattingClient client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
		friendListPannel = new FriendsListPanel(true, client);
		// frame setting
		new LogoManager().setLogoFrame(this);
		this.setBackground(backGroundColor);
		this.setSize(520, 430);
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		this.setUndecorated(true);
		CustomTitlebar titleBar = new CustomTitlebar(this, client, false);
		titleBar.setPreferredSize(new Dimension(520, 35));
		entirePanel.setBorder(BorderFactory.createLineBorder(new Color(82, 134, 198)));
		entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.Y_AXIS));
		titlepanel.setLayout(new BorderLayout());
		titlepanel.add(titleBar, BorderLayout.NORTH);
		titlepanel.setBackground(backGroundColor);
		entirePanel.add(titlepanel);
		entirePanel.add(panelAdd);
		entirePanel.add(middlePanel);
		entirePanel.add(bottomPanel);
		middlePanel.setBackground(backGroundColor);
		middlePanel.setPreferredSize(new Dimension(520, 300));
		bottomPanel.setBackground(backGroundColor);
		middleSelectedFriendListPanel.setBackground(backGroundColor);
		// topPanel setting
		panelAdd.setBackground(backGroundColor);
		this.add(entirePanel);
		
		panelAdd.add(topPanelLabel);
		panelAdd.add(topCountLabel);
		closeBtn.setBackground(backGroundColor);
		topPanelLabel.setFont(grayFont);
		topPanelLabel.setText("초대하기");
		topCountLabel.setText("" + count);

		// middlePanel setting
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		friendListPannel.setPreferredSize(new Dimension(250, 200));
		friendListPannel.getTfSearch().setPreferredSize(new Dimension(230, 15));
		friendListPannel.getpSearch().setBackground(backGroundColor);
		friendListPannel.getTfSearch().setBackground(backGroundColor);
		middlePanel.add(friendListPannel);
		// middlePanel.add(middleChoiceFriendListScrollPanel);
		middleSelectedFriendListPanel.setPreferredSize(new Dimension(220, 200));
		// 크기 설정하려면 서치패널에 있는 크기를 필수적으로 줄여주어야 한다....
		selectedList = new JList<>(listmodel);
		selectedList.setCellRenderer(new FriendsListCellRenderer());
		selectedList.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				// 오른쪽 선택된 리스트 선택시 왼쪽 리스트 선택 해제. 
//				System.out.println(selectedList.getSelectedValue().getSelectedIndex());
				friendListPannel.getJlFriendsList().getSelectionModel().removeSelectionInterval(
						selectedList.getSelectedValue().getSelectedIndex(), selectedList.getSelectedValue().getSelectedIndex());
				listmodel.remove(selectedList.getSelectedIndex());// 오른쪽 선택 값 삭제 
				friendListPannel.getSelectedFriends().remove(selectedList.getSelectedValue());// 오른쪽 선택된 친구들의 값이 지워짐에 따라 왼쪽 선택값 삭
			}
		});
		ChoiceFriendListScrollPanel = new CustomJScrollPane(selectedList, CustomJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				CustomJScrollPane.HORIZONTAL_SCROLLBAR_NEVER, true);
		ChoiceFriendListScrollPanel.setBorder(null);
		ChoiceFriendListScrollPanel.setPreferredSize(new Dimension(220, 200));
		middlePanel.add(ChoiceFriendListScrollPanel);
		friendListPannel.getpSearch().getpSearchOuter().setBackground(backGroundColor);
		friendListPannel.getJlFriendsList().addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				listmodel.removeAllElements();
				Iterator<Entry<String, UserVO>> entry = friendListPannel.getSelectedFriends().entrySet().iterator();
				for (int i = 0; i < friendListPannel.getSelectedFriends().size(); i++) {

					listmodel.addElement(entry.next().getValue());
				}
				System.out.println(listmodel.size());
				count = friendListPannel.getSelectedFriends().size();
				topCountLabel.setText(count + "");
				repaint();
			}
		});
		// bottomPanel setting
		bottomPanel.add(confirmBtn);
		bottomPanel.add(cancelBtn);
		// 버튼 액션 리스너!!!
		confirmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LinkedHashMap<String, UserVO> lastSelected = (LinkedHashMap<String, UserVO>) friendListPannel
						.getSelectedFriends();

				Thread chatCreat = new Thread(new Runnable() {
					public void run() {
						if (lastSelected.size() == 1) {
							Iterator<Entry<String, UserVO>> entry = friendListPannel.getSelectedFriends().entrySet()
									.iterator();
							ChattingController make1on1Romm = new ChattingController(client, entry.next().getValue());
						} else {
							ChattingController makeRoom = new ChattingController(client, lastSelected);// 다중

						}mainFrame.changePanel("chattingList");
						
						// 채팅컨트롤러손봐야
						// 함.
					}
				});
				chatCreat.start();
				dispose();
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.setVisible(true);
	}

	class FriendsListCellRenderer extends JPanel implements ListCellRenderer<UserVO> {
		private JLabel lbCloseBtn = new JLabel();
		private ImageIcon modelIcon = new ImageIcon("images/modelimg.png");
		private ImageIcon graundIcon = new ImageIcon("images/btnimg.png");
		private JLabel totalLabel = new JLabel();
		private JLabel lbImgIcon = new JLabel();
		private JLabel lbName = new JLabel();
		private JLabel lbStatMsg = new JLabel();
		private JPanel panelText;

		public FriendsListCellRenderer() {
			// Border border = this.getBorder();
			Border margin = new EmptyBorder(5, 20, 5, 20);

			LineBorder roundedborder = new LineBorder(backGroundColor, 7, true);

			// lbCloseBtn.setForeground(selectedColor1);
			lbCloseBtn.setPreferredSize(new Dimension(48, 45));
			lbCloseBtn.setIcon(graundIcon);
			this.setLayout(new BorderLayout()); // 간격 조정이 되버림(확인필요)

			this.setBorder(roundedborder);
			// border // 둥글게 표현할수 있는지확인하기

			lbName.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
			lbStatMsg.setFont(new Font("Malgun Gothic", Font.PLAIN, 8));
			// lbStatMsg.setBorder(new EmptyBorder(0, 6, 0, 6));

			panelText = new JPanel(new GridLayout(0, 1));
			// panelText.setBorder(new EmptyBorder(10, 6, 10, 0));
			panelText.add(lbName);
			// panelText.add(lbStatMsg);
			totalLabel.setLayout(new BorderLayout());
			add(totalLabel);

			totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 38, 0, 0));
			totalLabel.setPreferredSize(new Dimension(200, 45));
			totalLabel.add(lbImgIcon, BorderLayout.WEST);
			JLabel pluslabel = new JLabel();
			totalLabel.add(pluslabel, BorderLayout.CENTER);
			pluslabel.setLayout(new BorderLayout());
			pluslabel.add(panelText, BorderLayout.WEST);
			pluslabel.add(lbCloseBtn, BorderLayout.CENTER);
			totalLabel.setIcon(modelIcon);
			lbImgIcon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends UserVO> selectedColor, UserVO value, int index,
				boolean isSelected, boolean cellHasFocus) {
			// 받아온 JList의 값을 UserVO 객체에 담기
			UserVO vo = (UserVO) value;

			// 리턴할 객체에 둥근 프로필 이미지, 이름과, 상태 메세지 세팅
			ImageIcon profileImage = imageMaker.getRoundedImage(vo.getUserImgPath(), 35, 35);
			lbImgIcon.setIcon(profileImage);
			lbName.setText("   " + vo.getUserName());
			if (vo.getUserMsg() != null) {
				lbStatMsg.setText(vo.getUserMsg());
			}

			// 투명도 설정
			lbImgIcon.setOpaque(false);
			lbName.setOpaque(false);
			lbStatMsg.setOpaque(true);
			panelText.setOpaque(false);

			// 선택됐을때 색상 변경
			if (isSelected) {

				lbName.setBackground(selectedColor1);
				lbStatMsg.setBackground(selectedColor1);
				panelText.setBackground(selectedColor1);
				lbCloseBtn.setBackground(selectedColor1);
				setBackground(backGroundColor);
			} else {
				// lbImgIcon.setBackground(selectedColor1);
				lbName.setForeground(backGroundColor);
				lbStatMsg.setBackground(selectedColor1);
				panelText.setForeground(backGroundColor);
				lbCloseBtn.setBackground(selectedColor1);
				setBackground(backGroundColor);
			}
			return this;
		}
	}
}
