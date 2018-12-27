import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame {
	private Stack s1 = new Stack(),s2 =new Stack();
	private static short state = 0; // TST=1 , BST=2 , Trie=3 , hash=4 , non=0
	private String myFile;
	private int counter=0;
	private TST tst;
	private BST bst;
	private Trie trie;
	private Hash hash;
	public static JTextArea textArea = new JTextArea();

	public MyFrame() {
		setSize(680, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Inverted Index");
		setLayout(null);
		setResizable(false);

		JLabel text = new JLabel("Please enter folder address or use browse button");
		text.setFont(new Font("font", text.getFont().getStyle(), 13));
		text.setLocation(20, 20);
		text.setSize(400, 25);
		getContentPane().add(text);

		JTextField addressField = new JTextField();
		addressField.setFont(new Font("font", addressField.getFont().getStyle(), 12));
		addressField.setLocation(20, 45);
		addressField.setSize(400, 27);
		getContentPane().add(addressField);

		//JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("font", addressField.getFont().getStyle(), 11));
		textArea.setLocation(10, 100);
		textArea.setSize(660, 350);
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEtchedBorder());
		textArea.setLineWrap(true);
		getContentPane().add(textArea);

		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(10, 100, 660, 350);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scroll);

		JLabel text2 = new JLabel("Please enter your command:");
		text2.setFont(new Font("font", text2.getFont().getStyle(), 13));
		text2.setLocation(20, 467);
		text2.setSize(400, 25);
		getContentPane().add(text2);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		JRadioButton TSTButton = new JRadioButton("TST");
		JRadioButton BSTButton = new JRadioButton("BST");
		JRadioButton TrieButton = new JRadioButton("Trie");
		JRadioButton HashButton = new JRadioButton("Hash");

		TSTButton.setLocation(350, 470);
		TSTButton.setSize(57, 20);

		BSTButton.setLocation(430, 470);
		BSTButton.setSize(54, 20);

		TrieButton.setLocation(510, 470);
		TrieButton.setSize(60, 20);
		
		HashButton.setLocation(590, 470);
		HashButton.setSize(65, 20);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(TSTButton);
		group.add(BSTButton);
		group.add(TrieButton);
		group.add(HashButton);

		getContentPane().add(TSTButton);
		getContentPane().add(BSTButton);
		getContentPane().add(TrieButton);
		getContentPane().add(HashButton);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		JButton browseButton = new JButton("Browse");
		browseButton.setLocation(445, 45);
		browseButton.setSize(100, 27);
		getContentPane().add(browseButton);
		browseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser browse = new JFileChooser();
				browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				browse.showOpenDialog(browseButton);
				if (browse.getSelectedFile() != null)
					addressField.setText(browse.getSelectedFile().toString());
			}
		});

		JButton buildButton = new JButton("Build");
		buildButton.setLocation(50, 530);
		buildButton.setSize(100, 27);
		getContentPane().add(buildButton);
		buildButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s;
				myFile = addressField.getText();
				if (myFile.equals("")) {
					textArea.setText(textArea.getText() + "Pls choose a Directorary\n");
				} else if (TSTButton.isSelected()) {
					tst = new TST();
					counter=0;
					s = tst.build(new File("/Users/jebeli/Desktop/StopWords.txt"), new File(myFile));
					textArea.setText(textArea.getText() + s + "\n\n");
					TSTButton.setEnabled(false);
					BSTButton.setEnabled(false);
					TrieButton.setEnabled(false);
					HashButton.setEnabled(false);
					buildButton.setEnabled(false);
					browseButton.setEnabled(false);
					addressField.setEnabled(false);
					text.setEnabled(false);
					state = 1;
				} else if (BSTButton.isSelected()) {
					bst = new BST();
					counter=0;
					s = bst.build(new File("/Users/jebeli/Desktop/StopWords.txt"), new File(myFile));
					textArea.setText(textArea.getText() + s + "\n\n");
					TSTButton.setEnabled(false);
					BSTButton.setEnabled(false);
					TrieButton.setEnabled(false);
					HashButton.setEnabled(false);
					buildButton.setEnabled(false);
					browseButton.setEnabled(false);
					addressField.setEnabled(false);
					text.setEnabled(false);
					state = 2;
				} else if (TrieButton.isSelected()) {
					trie = new Trie();
					counter =0;
					s = trie.build(new File("/Users/jebeli/Desktop/StopWords.txt"), new File(myFile));
					textArea.setText(textArea.getText() + s + "\n\n");
					TSTButton.setEnabled(false);
					BSTButton.setEnabled(false);
					TrieButton.setEnabled(false);
					HashButton.setEnabled(false);
					buildButton.setEnabled(false);
					browseButton.setEnabled(false);
					addressField.setEnabled(false);
					text.setEnabled(false);
					state = 3;
				} else if (HashButton.isSelected()) {
					hash = new Hash();
					counter =0;
					s = hash.build(new File("/Users/jebeli/Desktop/StopWords.txt"), new File(myFile));
					textArea.setText(textArea.getText() + s + "\n\n");
					TSTButton.setEnabled(false);
					BSTButton.setEnabled(false);
					TrieButton.setEnabled(false);
					HashButton.setEnabled(false);
					buildButton.setEnabled(false);
					browseButton.setEnabled(false);
					addressField.setEnabled(false);
					text.setEnabled(false);
					state = 4;
				} else {
					textArea.setText(textArea.getText() + "Pls choose a Tree\n");
				}
			}

		});

		JButton resetButton = new JButton("Reset");
		resetButton.setLocation(200, 530);
		resetButton.setSize(100, 27);
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TSTButton.setEnabled(true);
				BSTButton.setEnabled(true);
				TrieButton.setEnabled(true);
				HashButton.setEnabled(true);
				buildButton.setEnabled(true);
				browseButton.setEnabled(true);
				addressField.setEnabled(true);
				text.setEnabled(true);
				textArea.setText("");
				state = 0;
			}
		});
		getContentPane().add(resetButton);

		JButton helpButton = new JButton("Help");
		helpButton.setLocation(370, 530);
		helpButton.setSize(100, 27);
		helpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, ">> list -w\n>> list -l\n>> list-f\n>> del file name without .txt\n>> add file name without .txt\n>> searh -w \"word\"\n>> searh -s \"statement\"\n...");

			}
		});
		getContentPane().add(helpButton);

		JButton exitButton = new JButton("Exit");
		exitButton.setLocation(520, 530);
		exitButton.setSize(100, 27);
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		getContentPane().add(exitButton);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		JTextField commandField = new JTextField();
		commandField.setFont(new Font("font", commandField.getFont().getStyle(), 12));
		commandField.setLocation(20, 490);
		commandField.setSize(640, 27);
		commandField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					String s;
					String command = commandField.getText();
					Scanner scanner = new Scanner(commandField.getText());
					
					while(!s2.isEmpty()){
						s1.push(s2.pop());
					}
					s1.push(command);
					commandField.setText("");
					if (scanner.hasNext()) {
						
						switch (scanner.next()) {
						case ">>":
							if (scanner.hasNext()) {
								switch (scanner.next()) {
								case "add":
									if (scanner.hasNext()) {
										
										switch (state) {
										case 0:
											textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
											break;
										case 1:
											s = tst.add(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 2:
											s = bst.add(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 3:
											s = trie.add(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 4:
											s = hash.add(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										}
									} else
										textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
									
									
								case "del":
									if (scanner.hasNext()) {
										
										switch (state) {
										case 0:
											textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
											break;
										case 1:
											s = tst.delete(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 2:
											s = bst.delete(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 3:
											s = trie.delete(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 4:
											s = hash.delete(new File(myFile + "/" + command.substring(7) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										}
									} else
										textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
									
									
									
									
								case "update":
									if (scanner.hasNext()) {
										
										switch (state) {
										case 0:
											textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
											break;
										case 1:
											s = tst.update(new File(myFile + "/" + command.substring(10) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 2:
											s = bst.update(new File(myFile + "/" + command.substring(10) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 3:
											s = trie.update(new File(myFile + "/" + command.substring(10) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										case 4:
											s = hash.update(new File(myFile + "/" + command.substring(10) + ".txt"));
											textArea.setText(textArea.getText() + s +"\n");
											break;
										}
									} else
										textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
									
									
									
								case "list":
									if (scanner.hasNext()) {
										
										switch (scanner.next()) {
										case "-l":
											switch (state) {
											case 0:
												textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
												break;
											case 1:
												textArea.setText(textArea.getText() + tst.list_l());
												break;
												
											case 2:
												textArea.setText(textArea.getText() + bst.list_l());
												break;
											case 3:
												textArea.setText(textArea.getText() + trie.list_l());
												break;
											case 4:
												textArea.setText(textArea.getText() + hash.list_l());
												break;
											}
											break;
											
											
											
										case "-f":
											counter = 0;
											for (File file : new File(myFile).listFiles()) {
												textArea.setText(textArea.getText() +file.getName() +"\n");
												counter ++;
											}
											textArea.setText(textArea.getText() + "Number of all docs = " + counter + "\n");
											break;
											
											
										case "-w":
											switch (state) {
											case 0:
												textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
												break;
											case 1:
												counter = tst.peymayesh();
												textArea.setText(textArea.getText() +"Number of words = "+ counter + "\n");
												break;									
											case 2:
												counter = 0;
												counter = bst.peymayesh();
												textArea.setText(textArea.getText() +"Number of words = "+ counter + "\n");
												break;
											case 3:
												counter = 0;
												counter = trie.peymayesh();
												textArea.setText(textArea.getText() +"Number of words = "+ counter + "\n");
												break;
											case 4:
												counter = 0;
												counter = hash.peymayesh();
												textArea.setText(textArea.getText() +"Number of words = "+ counter + "\n");
												break;
											}
											
										break;
										
										default:
											textArea.setText(textArea.getText() + "Invalid Command.\n");
											
										}
									} else
										textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
									
									
									
								case "search":
									if(scanner.hasNext()){
										switch (scanner.next()) {
										case "-w":
											switch (state) {
											case 0:
												textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
												break;
											case 1:
												s = "";
												s = tst.search(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;									
											case 2:
												s = "";
												s = bst.search(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											case 3:
												s = "";
												s = trie.search(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											case 4:
												s = "";
												s = hash.search(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											}
											
										break;
											
										case "-s":	
											switch (state) {
											case 0:
												textArea.setText(textArea.getText() + "first choose a tree and press build.\n");
												break;
											case 1:
												s = "";
												s = tst.multipleSearch(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;									
											case 2:
												s = "";
												s = bst.multipleSearch(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											case 3:
												s = "";
												s = trie.multipleSearch(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											case 4:
												s = "";
												s = hash.multipleSearch(command.substring(14, command.length()-1));
												textArea.setText(textArea.getText() + s + "\n");
												break;
											}
											break;

										default:
											textArea.setText(textArea.getText() + "Invalid Command.\n");
											break;
										}
									}
									else
										textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
									
									
									
								default:
									textArea.setText(textArea.getText() + "Invalid Command.\n");
									break;
								}
							} else
								textArea.setText(textArea.getText() + "Invalid Command.\n");
							break;

						default:
							textArea.setText(textArea.getText() + "Invalid Command.\n");
							break;
						}

					} else
						textArea.setText(textArea.getText() + "No Command.\n");
				}
				
				

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if(!s1.isEmpty()){
						String s1Pop =s1.pop();
						commandField.setText(s1Pop);
						s2.push(s1Pop);
					}
				}
				
				
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(!s2.isEmpty()){
						String s2Pop =s2.pop();
						commandField.setText(s2Pop);
						s1.push(s2Pop);
					}
				}
			}
		});
		getContentPane().add(commandField);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		setVisible(true);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new MyFrame();
			}
		});
	}
}