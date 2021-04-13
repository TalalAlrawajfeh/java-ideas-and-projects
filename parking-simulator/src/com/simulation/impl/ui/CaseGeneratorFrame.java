package com.simulation.impl.ui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.simulation.Simulation;
import com.simulation.impl.StorehouseSimulation;

@SuppressWarnings("serial")
public class CaseGeneratorFrame extends JFrame {
	public CaseGeneratorFrame() {
		super("Case Generator");
		Simulation simulation = new StorehouseSimulation();
		SwingCaseGenerator caseGenerator = new SwingCaseGenerator(simulation);

		setLayout(new FlowLayout());

		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(caseGenerator);
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(() -> {
			CaseGeneratorFrame frame = new CaseGeneratorFrame();

			frame.pack();
			frame.setResizable(false);
			frame.setVisible(true);
		});
	}
}
