package com.markjmind.jwtools.ui;


public class JwMenu extends JwGroup{
	
	public JwMenu(){
		super();
		setDefalutHistory(true);
	}
	
	public JwMenu(JwOnGroupSelect onGroupSelect){
		super(onGroupSelect);
		setDefalutHistory(true);
	}
}
