package org.flashmonkey.neat.gui;

/*import javax.swing.JTabbedPane;
 import javax.swing.ImageIcon;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JFrame;
 import java.util.Date;
 import java.text.SimpleDateFormat;
 import java.awt.*;
 import java.awt.event.*;


 import javax.swing.*;
 import javax.accessibility.*;
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.BasicConfigurator;
import org.flashmonkey.neat.core.NeatContext;
import org.flashmonkey.neat.util.log.HistoryLog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainGui extends JPanel {

	JFrame f1;

	private Parameter a_parameter;
	private ExperimentPanel a_session;
	private Generation a_generation;
	private Grafi a_grafi;

	JTabbedPane jtabbedPane1;

	public static void main(String[] args) {
		JFrame jp = null;
		MainGui pn1 = null;

		try {
			jp = new JFrame(
					"  J N E A T   Java simulator for   NeuroEvolution of Augmenting Topologies  ");
			pn1 = new MainGui(jp);

			// jp.getContentPane().add(pn1);
			jp.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

			jp.pack();
			jp.setSize(800, 600);
			jp.setVisible(true);

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public MainGui(JFrame frame) {

		BasicConfigurator.configure();
		
		f1 = frame;
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		System.out.println("Context Loaded ");
		//Neat neat = (Neat)appContext.getBean("neat");
		
		NeatContext context = new NeatContext(appContext);//(NeatContext)appContext.getBean("context");
		System.out.println("NEAT " + context.getNeat());
		
		a_parameter = new Parameter(f1, context);
		a_session = new ExperimentPanel(f1, context);
		a_generation = new Generation(f1, context);
		a_grafi = new Grafi(f1);

		logger = new HistoryLog();

		a_parameter.setLog(logger);
		a_session.setLog(logger);
		a_generation.setLog(logger);
		a_grafi.setLog(logger);

		jtabbedPane1 = new JTabbedPane();
		jtabbedPane1.addTab("jneat parameter", a_parameter.pmain);
		jtabbedPane1.addTab("session parameter", a_session.pmain);
		jtabbedPane1.addTab("start simulation", a_generation.pmain);
		jtabbedPane1.addTab("view graph", a_grafi.pmain);
		jtabbedPane1.setSelectedIndex(0);

		/*
		 * 
		 * 
		 * Container contentPane = f1.getContentPane();
		 * contentPane.setLayout(new BorderLayout());
		 * contentPane.add(jtabbedPane1,BorderLayout.CENTER);
		 * contentPane.add(logger, BorderLayout.SOUTH);
		 */

		Container contentPane = f1.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JSplitPane paneSplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				jtabbedPane1, logger);
		paneSplit1.setOneTouchExpandable(true);
		paneSplit1.setContinuousLayout(true);
		paneSplit1.setDividerSize(10);
		jtabbedPane1.setMinimumSize(new Dimension(400, 50));
		logger.setMinimumSize(new Dimension(100, 50));

		paneSplit1.setDividerLocation(410);

		paneSplit1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(2, 2, 2, 2), BorderFactory
				.createEmptyBorder(2, 2, 2, 2)));

		contentPane.add(paneSplit1, BorderLayout.CENTER);

	}

	protected HistoryLog logger;
}