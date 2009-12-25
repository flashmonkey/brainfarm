package org.flashmonkey.neat.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.core.Genome;
import org.flashmonkey.neat.core.NNode;
import org.flashmonkey.neat.core.Neat;
import org.flashmonkey.neat.core.NeatContext;
import org.flashmonkey.neat.core.Network;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.core.Population;
import org.flashmonkey.neat.core.Species;
import org.flashmonkey.neat.experiments.api.DataSource;
import org.flashmonkey.neat.experiments.api.IExperimentFitness;
import org.flashmonkey.neat.experiments.api.IExperimentInput;
import org.flashmonkey.neat.experiments.api.IExperimentOutput;
import org.flashmonkey.neat.experiments.api.INeatContext;
import org.flashmonkey.neat.experiments.api.StartFrom;
import org.flashmonkey.neat.gui.graph.Edge;
import org.flashmonkey.neat.gui.graph.Structure;
import org.flashmonkey.neat.gui.graph.Vertex;
import org.flashmonkey.neat.gui.graph.ChartXY;
import org.flashmonkey.neat.gui.graph.code;
import org.flashmonkey.neat.util.log.HistoryLog;

import util.CodeConstant;
import util.EnvConstant;
import util.EnvRoutine;
import util.IOseq;

public class Generation extends JPanel implements ActionListener, ItemListener {

	private static Logger logger = Logger.getLogger(Generation.class);
	
	private JFrame f1;
	
	private INeatContext context;

	Container contentPane;
	//protected HistoryLog logger;

	JPanel pmain;

	JPanel p2; // pannello comandi
	JPanel p3; // pannello grafico

	JPanel p3_text;
	JPanel p3_graph;
	JPanel p3_curve;

	Vector v1_fitness;
	Vector v1_fitness_win;
	Vector v1_species;

	JButton b1;
	JButton b2;
	JButton b3;

	// JButton b5;

	JButton b9;

	ButtonGroup ck_group;

	JRadioButton ck1;
	JRadioButton ck2;
	JRadioButton ck3;

	JTextPane textPane2;
	JScrollPane paneScroll2;

	ChartXY mappa_graph;
	ChartXY mappa_graph_curr;

	ChartXY mappa_curve;

	JSplitPane paneSplit2;

	// dynamic definition for fitness
	private IExperimentFitness fitnessClass;
	/*Class Class_fit;
	Object ObjClass_fit;
	Method Method_fit;
	Object ObjRet_fit;*/

	// dynamic definition for input class
	private IExperimentInput inputClass;
	/*Class Class_inp;
	Object ObjClass_inp;
	Method Method_inp;
	Object ObjRet_inp;*/

	// dynamic definition for target class
	private IExperimentOutput outputClass;
	/*Class Class_tgt;
	Object ObjClass_tgt;
	Method Method_tgt;
	Object ObjRet_tgt;*/

	private volatile Thread lookupThread;

	final static String[] My_styles = { "normal", "italic", "bold",
			"bold-italic" };

	/**
	 * pan1 constructor comment.
	 */
	public Generation(JFrame frame, NeatContext context) {

		this.context = context;
		
		GridBagLayout gbl;
		GridBagConstraints limiti;

		//logger = new HistoryLog();

		f1 = frame;

		mappa_graph = new ChartXY();
		mappa_graph_curr = new ChartXY();
		mappa_curve = new ChartXY();

		p2 = new JPanel();

		p3 = new JPanel();

		p3_curve = new JPanel();
		p3_graph = new JPanel();
		p3_text = new JPanel();

		p3.setLayout(new BorderLayout());

		b1 = new JButton(" start ");
		b1.addActionListener(this);

		b2 = new JButton(" start from last");
		b2.addActionListener(this);

		b3 = new JButton(" stop ");
		b3.addActionListener(this);

		// b5 = new JButton(" clear ");
		// b5.addActionListener(this);

		b9 = new JButton(" E X I T ");
		b9.addActionListener(this);

		Font fc = new Font("Dialog", Font.BOLD, 12);
		b1.setFont(fc);
		b2.setFont(fc);
		b3.setFont(fc);

		// b5.setFont(fc);
		b9.setFont(fc);

		ck_group = new ButtonGroup();
		ck1 = new JRadioButton("text output", true);
		ck1.setActionCommand("text output");
		ck1.addItemListener(this);

		ck2 = new JRadioButton("graph champion", false);
		ck2.setActionCommand("graph champion");
		ck2.addItemListener(this);

		ck3 = new JRadioButton("plot Error", false);
		ck3.setActionCommand("plot Error");
		ck3.addItemListener(this);

		ck_group.add(ck1);
		ck_group.add(ck2);
		ck_group.add(ck3);

		p2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Command options"), BorderFactory
				.createEmptyBorder(10, 2, 2, 2)));

		gbl = new GridBagLayout();
		limiti = new GridBagConstraints();
		p2.setLayout(gbl);

		buildConstraints(limiti, 0, 1, 1, 2, 100, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(b1, limiti);
		p2.add(b1);

		buildConstraints(limiti, 0, 3, 1, 2, 0, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(b2, limiti);
		p2.add(b2);

		buildConstraints(limiti, 0, 5, 1, 2, 0, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(b3, limiti);
		p2.add(b3);

		buildConstraints(limiti, 0, 7, 1, 2, 0, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(ck1, limiti);
		p2.add(ck1);

		buildConstraints(limiti, 0, 9, 1, 2, 0, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(ck2, limiti);
		p2.add(ck2);

		buildConstraints(limiti, 0, 11, 1, 2, 0, 5);
		limiti.anchor = GridBagConstraints.NORTH;
		limiti.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(ck3, limiti);
		p2.add(ck3);

		buildConstraints(limiti, 0, 13, 1, 2, 0, 70);
		limiti.anchor = GridBagConstraints.SOUTH;
		limiti.fill = GridBagConstraints.HORIZONTAL;
		limiti.ipady = 20;
		gbl.setConstraints(b9, limiti);
		p2.add(b9);

		textPane2 = new JTextPane();
		textPane2.setEditable(true);
		textPane2.setBackground(new Color(255, 252, 242));
		textPane2.setText("");

		paneScroll2 = new JScrollPane(textPane2);
		paneScroll2
				.setVerticalScrollBarPolicy(paneScroll2.VERTICAL_SCROLLBAR_AS_NEEDED);
		paneScroll2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(2, 2, 2, 2), BorderFactory
				.createEtchedBorder()));

		setStyleNew(textPane2);

		p3.setMinimumSize(new Dimension(100, 100)); // 100,100
		p3.setPreferredSize(new Dimension(400, 400)); // 300,200

		p3_text.setLayout(new BorderLayout());
		p3_text.add(paneScroll2, BorderLayout.CENTER);

		p3_graph.setLayout(new GridLayout(1, 2));
		p3_graph.add(mappa_graph_curr);
		p3_graph.add(mappa_graph);

		p3_curve.setLayout(new BorderLayout());
		p3_curve.add(mappa_curve, BorderLayout.CENTER);

		p3.validate();
		p3.repaint();

		pmain = new JPanel();
		gbl = new GridBagLayout();
		pmain.setLayout(gbl);

		limiti = new GridBagConstraints();
		buildConstraints(limiti, 0, 0, 1, 5, 0, 100);
		limiti.anchor = GridBagConstraints.WEST;
		limiti.fill = GridBagConstraints.VERTICAL;
		pmain.add(p2);
		gbl.setConstraints(p2, limiti);

		limiti = new GridBagConstraints();
		buildConstraints(limiti, 1, 1, 2, 5, 100, 0);
		limiti.anchor = GridBagConstraints.WEST;
		limiti.fill = GridBagConstraints.BOTH;
		pmain.add(p3);
		gbl.setConstraints(p3, limiti);

		// interface to main method of this class
		Container contentPane = f1.getContentPane();
		BorderLayout bl = new BorderLayout();
		contentPane.setLayout(bl);
		contentPane.add(pmain, BorderLayout.CENTER);
		//contentPane.add(logger, BorderLayout.SOUTH);

		//EnvConstant.OP_SYSTEM = System.getProperty("os.name");
		//EnvConstant.OS_VERSION = System.getProperty("os.version");
		//EnvConstant.JNEAT_DIR = System.getProperty("user.dir");
		//EnvConstant.OS_FILE_SEP = System.getProperty("file.separator");

		initAllMap();

	}

	/**
	 * Starts the application.
	 * 
	 * @param args
	 *            an array of command-line arguments
	 *//*
	public static void main(java.lang.String[] args) {

		JFrame jp = null;
		Generation pn1 = null;

		try {
			jp = new JFrame("  Generation of   n. e. a. t.  ");
			//pn1 = new Generation(jp);

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

		// Insert code to start the application here.
	}*/

	public void buildConstraints(GridBagConstraints gbc, int gx, int gy,
			int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	}

	public void itemStateChanged(ItemEvent e) {

		JRadioButton cb = (JRadioButton) e.getItem();
		String ckx = cb.getActionCommand();

		/*if (!(EnvConstant.RUNNING)) {

			if (ckx.equalsIgnoreCase("text output")) {
				p3.removeAll();
				p3.add(p3_text, BorderLayout.CENTER);
				p3_text.repaint();

			}

			if (ckx.equalsIgnoreCase("graph champion")) {

				p3.removeAll();
				p3.add(p3_graph, BorderLayout.CENTER);

				mappa_graph.repaint();
				mappa_graph_curr.repaint();
				p3_graph.repaint();

			}

			if (ckx.equalsIgnoreCase("plot Error")) {
				p3.removeAll();
				p3.add(p3_curve, BorderLayout.CENTER);
				mappa_curve.repaint();
				p3_curve.repaint();

			}

			p3.validate();
			p3.repaint();

		}*/

	}

	public void startProcessAsync() {
		/*Runnable lookupRun = new Runnable() {
			public void run() {
				logger.info("context -- " + context);
				startProcess(context);

			}
		};
		lookupThread = new Thread(lookupRun, " looktest");
		lookupThread.start();*/
		context.getExperimentRun().run();
	}

	public void actionPerformed(ActionEvent e) {

		//EnvConstant.FORCE_RESTART = false;
		//EnvConstant.STOP_EPOCH = false;
		String name = null;
		String tmp1 = null;
		String tmp2 = null;

		String ckx = ck_group.getSelection().getActionCommand();
		// " update screen "

		JButton Pulsante = (JButton) e.getSource();

		if (e.getActionCommand().equals(" E X I T ")) {
			System.exit(0);
		}

		if (e.getActionCommand().equals(" stop ")) {
			logger.info(" generation: wait...");
			//EnvConstant.STOP_EPOCH = true;
			logger.debug(" generation: request of *interrupt* .....");
			logger.info("READY");
		}

		else if (e.getActionCommand().equals(" start ")) {

			logger.info(" generation: wait...");
			initAllMap();
			//EnvConstant.FORCE_RESTART = false;
			startProcessAsync();
			logger.info("READY");
		}

		else if (e.getActionCommand().equals(" start from last")) {

			logger.info(" generation: wait...");
			initAllMap();
			//EnvConstant.FORCE_RESTART = true;
			logger
					.info(" generation: emergency restart ( from last generation)");
			startProcessAsync();
			logger.info("READY");

		}

	}

	public void setLog(HistoryLog _log) {
		//logger = _log;
	}

	

	

	public void setStyleNew(JTextPane textPane1) {

		StyleContext stylecontext = StyleContext.getDefaultStyleContext();
		Style defstyle = stylecontext.getStyle(StyleContext.DEFAULT_STYLE);

		Style style = textPane1.addStyle("normal", defstyle);
		StyleConstants.setFontFamily(style, "Verdana ");
		StyleConstants.setFontSize(style, 12);

		style = textPane1.addStyle("italic", defstyle);
		// StyleConstants.setForeground(style, new Color(24, 35, 87));
		StyleConstants.setItalic(style, true);
		StyleConstants.setFontSize(style, 11);

		style = textPane1.addStyle("bold", defstyle);
		// StyleConstants.setForeground(style, new Color(24, 35, 87));
		StyleConstants.setBold(style, true);
		StyleConstants.setFontSize(style, 13);

		style = textPane1.addStyle("bold-italic", defstyle);
		StyleConstants.setItalic(style, false);
		StyleConstants.setBold(style, false);
		StyleConstants.setFontSize(style, 12);

	}

	public int getNumberUnitForFile(String _file) {
		String nomef;
		StringTokenizer riga;
		String xline;
		int rc = 0;
		IOseq xFile;
		xFile = new IOseq(_file);
		boolean ret = xFile.IOseqOpenR();

		if (ret) {

			xline = xFile.IOseqRead();
			boolean done = false;
			while ((xline != "EOF") && (!done)) {

				if (!(xline.startsWith("//", 0))) {
					done = true;
					riga = new StringTokenizer(xline);
					rc = riga.countTokens();
				}
				xline = xFile.IOseqRead();

			}
		}

		else {
			System.out.print("\n error in open ->" + _file);
			System.out.print("\n correct and re-run! \n\t Bye");
			System.exit(8);

		}
		return rc;

	}

	/*public void drawCurve(String _riga, double _fitness, int _generation,
			int _nrspecies, int _popsize) {

		// private Vector vCurve;
		double _xc1;
		double _yc1;
		double _xc2;
		double _yc2;

		code xcodew = null;
		Iterator _itr = null;

		mappa_curve.setScale(p3.getWidth(), p3.getHeight());
		mappa_curve.setAxis(" time of epoch's",
				" Level fitness / Level winner fitness / #species ");
		mappa_curve.setAxis(true);
		mappa_curve.initAzioni();
		mappa_curve.setLabelValueY2(_popsize);
		mappa_curve.setLabelValueY(EnvConstant.MAX_FITNESS);
		mappa_curve.setLabelValueX(EnvConstant.NUMBER_OF_EPOCH);

		mappa_curve.azioni.add(new code(10, p3.getHeight() + 8, " Fitness  (0-"
				+ EnvConstant.MAX_FITNESS + ")", 1,
				CodeConstant.DESCRIPTOR_CURVE));
		mappa_curve.azioni.add(new code(210, p3.getHeight() + 8, " Species (1-"
				+ _popsize + ")", -1, CodeConstant.DESCRIPTOR_CURVE));
		mappa_curve.azioni.add(new code(410, p3.getHeight() + 8,
				" Fitness Winner", 2, CodeConstant.DESCRIPTOR_CURVE));

		mappa_curve.setGrid(true);

		//
		// draw current fitness
		//

		Double nx = null;
		Double ny = null;
		double _x1 = 0.0;
		double _y1 = 0.0;
		double _x2 = 0.0;
		double _y2 = 0.0;

		Iterator itr_fit = v1_fitness.iterator();
		boolean first_time = true;
		while (itr_fit.hasNext()) {

			if ((first_time)) {
				_x1 = 0.0;
				_y1 = 0.0;
				first_time = false;
			}

			else {
				_x1 = _x2;
				_y1 = _y2;
			}

			nx = (Double) itr_fit.next();
			ny = (Double) itr_fit.next();
			_x2 = nx.doubleValue();
			_y2 = ny.doubleValue();

			_yc1 = (_y1 * p3.getHeight()) / EnvConstant.MAX_FITNESS;
			_xc1 = (_x1 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;
			_yc2 = (_y2 * p3.getHeight()) / EnvConstant.MAX_FITNESS;
			_xc2 = (_x2 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;

			// current fitness
			xcodew = new code((int) _xc1, (int) _yc1, (int) _xc2, (int) _yc2,
					CodeConstant.LINE_TYPE_1, +1);
			mappa_curve.addLineToGrafo(xcodew);

		}

		//
		// draw fitness of winner
		//

		nx = null;
		ny = null;
		_x1 = 0.0;
		_y1 = 0.0;
		_x2 = 0.0;
		_y2 = 0.0;

		itr_fit = v1_fitness_win.iterator();
		first_time = true;
		while (itr_fit.hasNext()) {
			if ((first_time)) {
				_x1 = 0.0;
				_y1 = 0.0;
				first_time = false;
			} else {
				_x1 = _x2;
				_y1 = _y2;
			}

			nx = (Double) itr_fit.next();
			ny = (Double) itr_fit.next();
			_x2 = nx.doubleValue();
			_y2 = ny.doubleValue();

			_yc1 = (_y1 * p3.getHeight()) / EnvConstant.MAX_FITNESS;
			_xc1 = (_x1 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;
			_yc2 = (_y2 * p3.getHeight()) / EnvConstant.MAX_FITNESS;
			_xc2 = (_x2 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;

			// current fitness of winner
			xcodew = new code((int) _xc1, (int) _yc1, (int) _xc2, (int) _yc2,
					CodeConstant.LINE_TYPE_1, +2);
			mappa_curve.addLineToGrafo(xcodew);

		}

		//
		// draw number of species
		//

		nx = null;
		ny = null;
		_x1 = 0.0;
		_y1 = 0.0;
		_x2 = 0.0;
		_y2 = 0.0;

		_itr = v1_species.iterator();
		first_time = true;
		while (_itr.hasNext()) {

			if ((first_time)) {
				_x1 = 0.0;
				_y1 = 0.0;
				first_time = false;
			} else {
				_x1 = _x2;
				_y1 = _y2;
			}

			nx = (Double) _itr.next();
			ny = (Double) _itr.next();
			_x2 = nx.doubleValue();
			_y2 = ny.doubleValue();

			_yc1 = (_y1 * p3.getHeight()) / _popsize;
			_xc1 = (_x1 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;
			_yc2 = (_y2 * p3.getHeight()) / _popsize;
			_xc2 = (_x2 * p3.getWidth()) / EnvConstant.NUMBER_OF_EPOCH;

			// current fitness of winner
			xcodew = new code((int) _xc1, (int) _yc1, (int) _xc2, (int) _yc2,
					CodeConstant.LINE_TYPE_1, -1);
			mappa_curve.addLineToGrafo(xcodew);

		}

		mappa_curve.setAxis(true);
		mappa_curve.setGrid(true);
		// mappa_curve.repaint();

	}*/

	public void setGraphView() {
		/*
		 * String mask6d = "  0.00000"; DecimalFormat fmt6d = new
		 * DecimalFormat(mask6d); mappa.setTitle(" First Winner genome ->"+
		 * fmt6d.format(vf1));
		 */

		p3.removeAll();
		p3.add(p3_graph, BorderLayout.CENTER);

		p3_graph.validate();
		p3_graph.repaint();

	}

	public void drawGraph(Organism _o1, String _riga, ChartXY _mappa) {

		String mask6d = "  0.00000";
		DecimalFormat fmt6d = new DecimalFormat(mask6d);

		Genome _g1 = _o1.genome;

		Vector v1 = new Vector(1, 0);
		Structure sx = new Structure();

		_mappa.initAzioni();

		sx.LoadGenome(_g1);
		sx.generate_Grafo();

		sx.compute_Coordinate(p3.getWidth() / 2, p3.getHeight());

		String riga_r1 = " Fitness ->" + fmt6d.format(_o1.getOrig_fitness())
				+ "  , error->" + fmt6d.format(_o1.getError());

		v1.add(new code(10, p3.getHeight() + 10, riga_r1, 0,
				CodeConstant.DESCRIPTOR));

		Iterator itr_point = sx.vVertex.iterator();
		while (itr_point.hasNext()) {
			Vertex _point = ((Vertex) itr_point.next());

			if ((_point.x) != 0 && (_point.y != 0) && (_point.is_real()))
				v1.add(new code(_point, CodeConstant.NODO_N));
			if ((_point.x) != 0 && (_point.y != 0) && (_point.is_recurrent()))
				v1.add(new code(_point, CodeConstant.NODO_R));

		}

		// store edge for interpreter
		Iterator itr_edge = sx.vEdge.iterator();
		while (itr_edge.hasNext()) {
			Edge _edge = ((Edge) itr_edge.next());
			Vertex _inode = _edge.in_node;
			Vertex _onode = _edge.out_node;
			int type = _edge.type;

			double weight_edge = _edge.weight;
			int sign_edge;
			if (weight_edge >= 0)
				sign_edge = +1;
			else
				sign_edge = -1;

			if ((_inode.x) != 0 && (_inode.y != 0) && (_onode.x) != 0
					&& (_onode.y != 0) && (_edge.active)) {
				int x1 = _inode.x;
				int y1 = _inode.y;
				int x2 = _onode.x;
				int y2 = _onode.y;
				v1.add(new code(_inode, _onode, type, sign_edge));
			}
		}

		_mappa.setScale((p3.getWidth()) / 2, p3.getHeight());
		_mappa.setAxis(false);
		_mappa.setGrid(false);
		_mappa.setGrafo(v1);
		_mappa.repaint();

	}

	public void initAllMap() {
		textPane2.setText("");
		p3_text.repaint();

		p3.removeAll();
		p3.add(p3_text, BorderLayout.CENTER);

		p3.removeAll();
		p3.add(p3_graph, BorderLayout.CENTER);
		mappa_graph.setAxis(false);
		mappa_graph.setGrid(false);
		mappa_graph.setBackground(Color.lightGray);
		mappa_graph.setTitle(" First Winner genome ");
		mappa_graph_curr.setTitle("Current genome (best fitness) ");
		mappa_graph.setScale((p3.getWidth()) / 2, p3.getHeight());
		mappa_graph_curr.setScale((p3.getWidth()) / 2, p3.getHeight());
		mappa_graph.initAzioni();
		mappa_graph_curr.initAzioni();
		mappa_graph.repaint();
		mappa_graph_curr.repaint();
		p3_graph.validate();
		p3_graph.repaint();

		p3.removeAll();
		p3.add(p3_curve, BorderLayout.CENTER);
		mappa_curve.setScale((p3.getWidth()), p3.getHeight());
		mappa_curve.repaint();
		p3_curve.validate();
		p3_curve.repaint();

		p3.removeAll();
		v1_fitness_win = new Vector(1, 0);
		v1_fitness = new Vector(1, 0);
		v1_species = new Vector(1, 0);

	}

	public void ViewGeneric() {
		String ckx = ck_group.getSelection().getActionCommand();

		if (ckx.equalsIgnoreCase("text output")) {
			p3.removeAll();
			p3.add(p3_text, BorderLayout.CENTER);
			p3_text.repaint();

		}

		if (ckx.equalsIgnoreCase("graph champion")) {

			p3.removeAll();
			p3.add(p3_graph, BorderLayout.CENTER);

			mappa_graph.repaint();
			mappa_graph_curr.repaint();
			p3_graph.repaint();

		}

		if (ckx.equalsIgnoreCase("plot Error")) {
			p3.removeAll();
			p3.add(p3_curve, BorderLayout.CENTER);
			mappa_curve.repaint();
			p3_curve.repaint();

		}

		// System.out.print("\n init 4a");
		p3.validate();
		p3.repaint();

	}
}