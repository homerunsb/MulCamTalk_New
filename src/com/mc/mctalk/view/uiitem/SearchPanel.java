package com.mc.mctalk.view.uiitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.prompt.PromptSupport;

public class SearchPanel extends JPanel{
	private JPanel pSearchInner, pSearchOuter;
	private JTextField tfSearch;
	private JLabel jlSearch;
	
	public SearchPanel(String str) {
		this.setLayout(new BorderLayout());
		pSearchInner = new JPanel();
		pSearchOuter = new JPanel();
		pSearchInner.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		pSearchInner.setBackground(Color.white);
		pSearchInner.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		tfSearch = new JTextField();
		tfSearch.setPreferredSize(new Dimension(290, 15));
		tfSearch.setBorder(BorderFactory.createEmptyBorder());
		PromptSupport.setPrompt(str, tfSearch);
		
		jlSearch = new JLabel(new ImageIcon("images/icon_search.png"));
		pSearchInner.add(jlSearch);
		pSearchInner.add(tfSearch);
		pSearchOuter.add(pSearchInner);
		this.add(pSearchOuter);
	}
	
	public JTextField getTfSearch() {
		return tfSearch;
	}
	public void setTfSearch(JTextField tfSearch) {
		this.tfSearch = tfSearch;
	}
	public JPanel getpSearchOuter() {
		return pSearchOuter;
	}
	public void setpSearchOuter(JPanel pSearchOuter) {
		this.pSearchOuter = pSearchOuter;
	}

	public JPanel getpSearchInner() {
		return pSearchInner;
	}
}
