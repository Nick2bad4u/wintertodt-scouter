/*
 * Copyright (c) 2021, Andrew McAdams, nucleon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.wintertodt.scouter.ui.panels;

import com.wintertodt.scouter.WintertodtBossData;
import com.wintertodt.scouter.WintertodtScouterPlugin;
import com.wintertodt.scouter.ui.WintertodtScouterPluginPanelBase;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WintertodtScouterPluginPanel extends WintertodtScouterPluginPanelBase
{
	FixedWidthPanel bossListPanel = new FixedWidthPanel();
	ArrayList<WintertodtScouterSinglePanel> bossList = new ArrayList<>();

	private final GridBagConstraints c = new GridBagConstraints();

	public WintertodtScouterPluginPanel(WintertodtScouterPlugin plugin)
	{
		super(plugin, false);

		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		// Create the container for the title and refresh task button
		JPanel topContainer = new JPanel();
		topContainer.setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel();
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titlePanel.setLayout(new BorderLayout());

		JLabel title = new JLabel();
		title.setText("Wintertodt Scouter");
		title.setForeground(Color.WHITE);
		titlePanel.add(title, BorderLayout.WEST);
		topContainer.add(titlePanel, BorderLayout.NORTH);

		add(topContainer, BorderLayout.NORTH);

		// Create the task list panel
		bossListPanel.setLayout(new GridBagLayout());
		bossListPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		c.insets = new Insets(0, 2, 2, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.anchor = GridBagConstraints.PAGE_START;

		JScrollPane scrollPane = new JScrollPane(bossListPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new EmptyBorder(1, 0, 0, 0));
		scrollPane.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		add(scrollPane, BorderLayout.CENTER);
	}

	public void addBossData(JPanel wintertodtScouterPanel, WintertodtBossData data)
	{
		WintertodtScouterSinglePanel bossDataPanel = new WintertodtScouterSinglePanel(data, plugin::hopTo);
		wintertodtScouterPanel.add(bossDataPanel, c);
		c.gridy += 1;
		bossList.add(bossDataPanel);
	}

	@Override
	public void populate(List<WintertodtBossData> globalBossData)
	{
		c.gridy = 0;
		c.weighty = 0;
		// Remove all old panels
		for (WintertodtScouterSinglePanel bossSinglePanel : bossList)
		{
			bossListPanel.remove(bossSinglePanel);
		}
		bossList.clear();

		// Add new panels. Need to keep track of the last one to give it extra weighty (to put all extra space after it)
		WintertodtBossData lastData = null;
		for (WintertodtBossData bossData : globalBossData)
		{
			if (lastData != null)
				addBossData(bossListPanel, lastData);
			lastData = bossData;
		}

		// Add the last panel with weighty 1
		c.weighty = 1;
		if (lastData != null)
			addBossData(bossListPanel, lastData);

		repaint();
		revalidate();
	}

	@Override
	public void updateList()
	{
		for (WintertodtScouterSinglePanel bossSinglePanel : bossList)
		{
			bossSinglePanel.updateLabels();
		}
	}

}
