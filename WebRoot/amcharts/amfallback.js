AmCharts.AmFallback = AmCharts.Class(
{
	write:function(holder)
	{		
		var chart = this.chart;
		if(chart)
		{
			chart.clear();
			this.chart = null;
		}
		
		document.getElementById(holder).innerHTML = "";
		
		this.loadParseEverything();
		
		// crate chart
		var type = this.type;
		if(type == "pie")
		{
			this.chart = new AmCharts.AmPieChart();
			this.processPieChart();
		}
		
		if(type == "line" || type == "column"){
			this.chart = new AmCharts.AmSerialChart();
			this.processSerialChart();													
		}
		
		if(type == "radar"){
			this.chart = new AmCharts.AmRadarChart();
			this.processRadarChart();													
		}		
		
		if(type == "xy"){
			this.chart = new AmCharts.AmXYChart();
			this.processXYChart();													
		}			
		this.chart.autoMargins = false;
		this.addLabels();
					
		this.chart.write(holder);			
	},
	
	loadParseEverything:function()
	{
		this.settings = {};
		this.settingsRaw = [];
		this.data = [];
		this.dataRaw = [];		

		this.loadAndParseSettings();		
		this.loadAndParseData();		
	},
	
	loadAndParseData:function()
	{
		// load and parse data
		var dataFile = this.dataFile;
		var chartData = this.chartData;
		var data = this.data;
		var dataRaw = this.dataRaw;
		var dataType = this.dataType;
				
		if (dataFile != undefined && dataFile != '')
		{
			if (dataType == 'csv')
			{
				this.loadCsv(dataFile, data);
			}
			else 
			{
				this.loadXml(dataFile, dataRaw);
			}
		}
		// parse inline data		
		else if (chartData != undefined && chartData != '')
		{
			if (dataType == 'csv')
			{
				this.parseCSV(chartData, data);
			}
			else
			{
				this.parseXML(chartData, dataRaw);
			}
		}		
		if(dataRaw)
		{
			this.parseXMLDataObject(dataRaw, data);
		}		
	},
	
	loadAndParseSettings:function()
	{
		// load and parse settings
		var settingsFile = this.settingsFile;
		var settingsRaw = this.settingsRaw;
		var chartSettings = this.chartSettings;
		
	    if (settingsFile != '' && settingsFile != undefined)
	    {
			this.loadXml(settingsFile, settingsRaw);			
		}
		else if(chartSettings != undefined)
		{
			this.parseXML(chartSettings, settingsRaw);
		}
				
		if(settingsRaw[0])
		{
			var root = settingsRaw[0].root;
			if(root)
			{
				var settings = root.settings;
				if(settings)
				{
					this.settings = settings;
				}
			}
		}	

		if(settings.data_type)
		{
    		if (this.trim(settings.data_type))
    		{
    			this.dataType = settings.data_type.toLowerCase();
    		}
    	}		
	},
	
	processPieChart:function()
	{
		var chart = this.chart;
		var settings = this.settings;
		
		chart.dataProvider = this.data;
		chart.titleField = "title";
		chart.valueField = "value";
		chart.pulledField = "pulled";
		chart.colorField = "color";
		chart.descriptionField = "description";
		chart.urlField = "url";
		chart.labelText = "";
		chart.alphaField = "alpha";
		
		this.setSettings(chart, settings, this.commonSettingsMap);
		
		this.createLegend();
		
		var bs = settings.balloon; 
		if (bs)
		{
			this.setSettings(chart.balloon, bs, this.balloonMap);
		}
		
		this.setSettings(chart, settings, this.pieSettingsMap);			
	},	
	
	processSerialChart:function()
	{
		var chart = this.chart;
		var settings = this.settings;
	
		chart.pathToImages = this.pathToImages;	
		chart.dataProvider = this.data;
	
		var type = settings.type;
		if(type == "bar")
		{
			chart.rotate = true;
			chart.dataProvider.reverse();
		}				
		
		chart.categoryField = "series";
		
		this.setSettings(chart, settings, this.commonSettingsMap);

		var bs = settings.balloon; 
		if (bs)
		{
			this.setSettings(chart.balloon, bs, this.balloonMap);
		}
		
		this.setSettings(chart, settings, this.serialSettingsMap);				
		
		chart.categoryAxis.autoGridCount = false;
		
		if(this.type == "line")
		{
			chart.categoryAxis.startOnAxis = true;
		}
		else
		{
			chart.categoryAxis.gridPosition = "start";
			chart.categoryAxis.gridCount = 1000;
		}
		
		var categoryAxisMap = this.categoryAxisMap;
		this.setSettings(chart.categoryAxis, settings, categoryAxisMap, "x");
		this.setSettings(chart.categoryAxis, settings, categoryAxisMap, "category");
		
		var zs = settings.zoom_out_button;
		if (zs)
		{
			this.setSettings(chart.zoomOutButton, zs, this.zoomOutButtonMap);
		}
		
		var column = settings.column;
		if (column)
		{
			this.setSettings(chart, column, this.columnSettingsMap);
		}
										
		this.createLegend();
		this.createValueAxes();						
		this.createGraphs();
		
        if(type == "bar")
        {
            if(this.chart.graphs)
            {
                this.chart.graphs.reverse();
            }
        }		
		
		var guides = settings.guides;
		if (guides)
		{
			var guide = guides.guide;
			if (guide)
			{
				this.createGuides(guide);
			}
		}

		if(this.dataType == "xml")
		{
			this.createGraphsFromData(this.dataRaw);
		}	

		guides = this.data.guides;
		if (guides)
		{
			guide = guides.guide;
			if (guide)
			{
				this.createGuides(guide);
			}
		}
									
		this.createCursor();
		this.createScrollbar();
		this.setStackType(this.settings.column);
		chart.path = this.path;	
	},
		
	processXYChart:function()
	{
		var chart = this.chart;
		var settings = this.settings;
		
		chart.pathToImages = this.pathToImages;	
		chart.dataProvider = this.data;
		
		this.setSettings(chart, settings, this.commonSettingsMap);
		
		var bs = settings.balloon; 
		if (bs)
		{
			this.setSettings(chart.balloon, bs, this.balloonMap);
		}
		
		this.setSettings(chart, settings, this.serialSettingsMap);
		
		var zs = settings.zoom_out_button;
		if (zs)
		{
			this.setSettings(chart.zoomOutButton, zs, this.zoomOutButtonMap);
		}
										
		this.createLegend();
		this.createXYAxes();						
		this.createGraphs();

		if(this.dataType == "xml")
		{
			this.createGraphsFromData(this.dataRaw);
		}
									
		this.createCursor();
		this.createScrollbar();
		chart.path = this.path;	
	},	
	
	
	processRadarChart:function()
	{
		var chart = this.chart;
		var settings = this.settings;
	
		chart.dataProvider = this.data;
		chart.categoryField = "series";
		
		this.setSettings(chart, settings, this.commonSettingsMap);
		this.setSettings(chart, settings, this.radarSettingsMap);
		
		var bs = settings.balloon; 
		if (bs)
		{
			this.setSettings(chart.balloon, bs, this.balloonMap);
		}
										
		this.createLegend();
		
		var valueAxis = new AmCharts.ValueAxis();
		valueAxis.axisAlpha = 0.15;
		this.setSettings(valueAxis, settings, this.valueAxisMap, "-");
		this.setSettings(valueAxis, settings, this.radarAxisMap);
		chart.addValueAxis(valueAxis);
								
		this.createGraphs();
		
		var fills = settings.fills;
		var fill;
		if (fills)
		{
			fill = fills.fill;
			if (fill)
			{
				this.createGuides(fill);
			}
		}

		if(this.dataType == "xml")
		{
			this.createGraphsFromData(this.dataRaw);
		}	
		
		fills = this.data.fills;
		if (fills)
		{
			fill = fills.fill;
			if (fill)
			{
				this.createGuides(fill);
			}
		}
		this.setStackType(settings);
	},	
	
	addLabels:function()
	{
		var labels = this.settings.labels;
		var label;
		if (labels)
		{
			label = labels.label;
			if (label)
			{
				this.createLabels(label);
			}
		}
		labels = this.data.labels;
		if (labels)
		{
			label = labels.label;
			if (label)
			{
				this.createLabels(label);
			}
		}		
	},
	
	parseXML: function (str, holder)
	{
		var parser;
		var xmlDoc;
		if (window.DOMParser)
		{
			parser = new DOMParser();
			xmlDoc = parser.parseFromString(str, 'text/xml');
		}
		else
		{
			xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
			xmlDoc.async = 'false';
			xmlDoc.loadXML(str);
		}
		this.parseXMLObject(xmlDoc, holder);
	},
	
	parseXMLObject: function (xml, holder)
	{
		var data = {
			root:{}
		};
		this.parseXMLNode(data, 'root', xml);
		holder.push(data);
	},
	
	parseXMLNode: function (dataNode, dataNodeName, xmlNode, level)
	{
		if (level == undefined)
		{
			level = '';
		}
		if (xmlNode)
		{
			var cnt = xmlNode.childNodes.length;
			for (var i = 0; i < cnt; i++)
			{
				var node = xmlNode.childNodes[i];
				var name = node.nodeName;
				var value = node.nodeValue ? this.trim(node.nodeValue) : '';
				
				var foundAttr = false;
				if (node.attributes)
				{
					if(node.attributes.length > 0)
					{
						foundAttr = true;
					}
				}
			
				if (node.childNodes.length == 0 && value == '' && foundAttr == false)
				{
				
				}
				else
				{
					if (node.nodeType == 3 || node.nodeType == 4)
					{					
						if (value != '')
						{
							var n = 0;
							for (var prop in dataNode[dataNodeName])
							{
								n++;
							}
							if (n)
							{
								dataNode[dataNodeName]['#text'] = value;
							}
							else
							{
								dataNode[dataNodeName] = value;
							}
						}
					}
					else if (node.nodeType == 1)
					{
						var newNode;
						if (dataNode[dataNodeName][name] != undefined)
						{
							if (dataNode[dataNodeName][name].length == undefined)
							{
								var tmp = dataNode[dataNodeName][name];
								dataNode[dataNodeName][name] = [];
								dataNode[dataNodeName][name].push(tmp);
								dataNode[dataNodeName][name].push({});
								newNode = dataNode[dataNodeName][name][1];
							}
							else
							{
								if (typeof(dataNode[dataNodeName][name]) == 'object') {
									dataNode[dataNodeName][name].push({});
									newNode = dataNode[dataNodeName][name][dataNode[dataNodeName][name].length - 1];
								}
							}
						}
						else 
						{
							dataNode[dataNodeName][name] = {};
							newNode = dataNode[dataNodeName][name];
						}
							
						if (node.attributes)
						{
							if (node.attributes.length)
							{
								for (var c = 0; c < node.attributes.length; c++) 
								{
									newNode[node.attributes[c].name] = node.attributes[c].value;
								}
							}
						}
							
						if (dataNode[dataNodeName][name].length != undefined) 
						{
							this.parseXMLNode(dataNode[dataNodeName][name], dataNode[dataNodeName][name].length - 1, node, level + '  ');
						}
						else 
						{
							this.parseXMLNode(dataNode[dataNodeName], name, node, level + '  ');
						}
					}
				}
			}
			
			var n = 0;
			var text = '';
			for (var prop in dataNode[dataNodeName])
			{
				if (prop == '#text')
				{
					text = dataNode[dataNodeName][prop];
				}
				else
				{
					n++;
				}
			}
			if ((n == 0) && (dataNode[dataNodeName].length == undefined))
			{
				dataNode[dataNodeName] = text;
			}
		}
	},	
	
	trim: function (str)
	{
		if (str)
		{
			var whitespace = ' \n\r\t\f\x0b\xa0\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u200b\u2028\u2029\u3000';
			for (var i = 0; i < str.length; i++)
			{
				if (whitespace.indexOf(str.charAt(i)) === -1)
				{
					str = str.substring(i);
					break;
				}
			}
			for (i = str.length - 1; i >= 0; i--)
			{
				if (whitespace.indexOf(str.charAt(i)) === -1)
				{
					str = str.substring(0, i + 1);
					break;
				}
			}
			return whitespace.indexOf(str.charAt(0)) === -1 ? str : '';
		}
		else
		{
			return;
		}
	},	
	
	
	loadXml: function (file, holder)
	{
		var request
		if (window.XMLHttpRequest)
		{
			// IE7+, Firefox, Chrome, Opera, Safari
			 request = new XMLHttpRequest();
		}
		else
		{
			// code for IE6, IE5
			request = new ActiveXObject('Microsoft.XMLHTTP');
		}
		
		if (request.overrideMimeType)
		{
			request.overrideMimeType('text/xml')
		}
		
		// load
		request.open('GET', file, false);
		request.send();
		this.parseXMLObject(request.responseXML, holder);
	},
	
	loadCsv: function (file, holder)
	{
		if (window.XMLHttpRequest)
		{
			// IE7+, Firefox, Chrome, Opera, Safari
			var request = new XMLHttpRequest();
		}
		else
		{
			// code for IE6, IE5
			var request = new ActiveXObject('Microsoft.XMLHTTP');
		}
		// load
		request.open('GET', file, false);
		request.send();
		this.parseCSV(request.responseText, holder);
	},
	
	
	parseCSV:function(data, holder)
	{
		var separator = this.settings.csv_separator;
		if(separator == undefined)
		{
			separator = ";"
		}
		var skip = Number(this.settings.skip_rows);
		if(isNaN(skip))
		{
			skip = 0;
		}
		
		//replace UNIX new lines
		data = data.replace (/\r\n/g, "\n");
		//replace MAC new lines
		data = data.replace (/\r/g, "\n");
		//split into rows
		var rows = data.split("\n");
		// delete skiped rows
		if (!isNaN(skip))
		{
			rows.splice(0,skip);
		}

		var type = this.type;
		if(type == "pie")
		{
			for (var i = 0; i < rows.length; i++)
			{
				if (rows[i])
				{
					var tArray = rows[i].split(separator);
					
					var dItem = new Object();
					
					var a = tArray[0];
					if (AmCharts.isDefined(a))
					{
						dItem.title = String(a.replace(/&lessthen/g, "&lt;"));
					}
					
					a = tArray[1];
					if (AmCharts.isDefined(a))
					{
						dItem.value = AmCharts.toNumber(a);
					}
					
					a = tArray[2];
					if (AmCharts.isDefined(a) && a != "")
					{
						dItem.pulled = AmCharts.toBoolean(a);
					}
					
					a = tArray[3];
					if (AmCharts.isDefined(a) && a != "")
					{
						dItem.color = AmCharts.toColor(a);
					}
					
					a = tArray[4];
					if (AmCharts.isDefined(a) && a != "")
					{
						dItem.url = a;
					}
					
					a = tArray[5];
					if (AmCharts.isDefined(a) && a != "")
					{
						dItem.description = a.replace(/&lessthen/g, "&lt;");
					}
					
					a = tArray[6];
					if (AmCharts.isDefined(a))
					{
						dItem.alpha = AmCharts.toNumber(a) / 100;
					}
					holder.push(dItem);
				}
			}
		}
		if(type == "column" || type == "line" || type == "radar")
		{
			for (var i = 0; i < rows.length; i++)
			{			
				if(rows[i])
				{					
					var tArray = rows[i].split(separator);
					
					var dItem = new Object();
					dItem.series = tArray[0];
					for (var j = 1; j < tArray.length; j++)
					{
						var g = j - 1;
						if (tArray[j] != "" && tArray[j] != undefined)
						{
							dItem['graph' + g + "_value"] = AmCharts.toNumber(tArray[j]);
						}
					}
					holder.push(dItem);
				}
			}			
		}		
        if(type == "xy")
        {
            for (var i = 0; i < rows.length; i++)
            {           
                if(rows[i])
                {                   
                    var tArray = rows[i].split(separator);
                    
                    var dItem = new Object();
                    dItem['graph0_x'] = AmCharts.toNumber(tArray[0]);
                    dItem['graph0_y'] = AmCharts.toNumber(tArray[1]);
                    dItem['graph0_value'] = AmCharts.toNumber(tArray[2]);
                    holder.push(dItem);
                }
            }           
        }       		
	},
	
	
	parseXMLDataObject:function(data, holder)
	{		
		var type = this.type;
		if(data[0])
		{
			if (data[0].root)
			{
				if (type == "pie")
				{
					if (data[0].root.pie)
					{						
						if(data[0].root.pie.guides)
						{
							holder.guides = data[0].root.pie.guides; 
						}
						
						if (data[0].root.pie.slice)
						{
							var d = data[0].root.pie.slice;
							
							d = this.objectToArray(d);
							if (d)
							{
								for (var i = 0; i < d.length; i++)
								{
									var dItem = d[i];
									
									var a = dItem.title;
									if (AmCharts.isDefined(a))
									{
										dItem.title = a.replace(/&lessthen/g, "&lt;");
									}
									
									var a = dItem['#text'];
									if (AmCharts.isDefined(a))
									{
										dItem.value = AmCharts.toNumber(a);
									}
									
									a = dItem.pull_out;
									
									if (AmCharts.isDefined(a))
									{
										dItem.pulled = AmCharts.toBoolean(a, false);
									}
									
									var a = dItem.color;
									if (AmCharts.isDefined(a))
									{
										dItem.color = AmCharts.toColor(a);
									}
									
									var a = dItem.url;
									if (AmCharts.isDefined(a))
									{
										dItem.url = a;
									}
									
									a = dItem.description;
									if (AmCharts.isDefined(a))
									{
										dItem.description = a.replace(/&lessthen/g, "&lt;");
									}
									
									var a = dItem.alpha;
									if (AmCharts.isDefined(a))
									{
										dItem.alpha = AmCharts.toNumber(a) / 100;
									}
									holder.push(dItem);
								}
							}
						}
					}
				}
				
				if(type == "xy")
				{
					var o = data[0].root.chart;
					if (o)
					{
						if (o)
						{
							if (o.graphs)
							{
								if (o.graphs.graph)
								{
									var graphs = o.graphs.graph;
									graphs = this.objectToArray(graphs);	
									
									if (graphs)
									{
										for (var j = 0; j < graphs.length; j++)
										{
											var graph = graphs[j];
											var points = graph.point;
											if(points)
											{
												points = this.objectToArray(points);
												
												for(var p = 0; p < points.length; p++)
												{
													var point = points[p];
													
													var dItem = {};													
													dItem['graph' + j + "_value"] = Number(point.value);
													dItem['graph' + j + "_x"] = Number(point.x);
													dItem['graph' + j + "_y"] = Number(point.y);
													this.setDataAttributes(dItem, point, j);
													holder.push(dItem);
												}
											}
										}
									}									
								}
							}
						}
					}					
				}				
				
				if (type == "column" || type == "line" || type == "radar")
				{
					var o = data[0].root.chart;
					if (o)
					{
						if (o.guides)
						{
							holder.guides = o.guides;
						}
						
						if (o.graphs)
						{
							if (o.graphs.graph)
							{
								var graphs = o.graphs.graph;
								
								graphs = this.objectToArray(graphs);
								
								if (this.type == "radar") {
									var sv = o.axes.axis;
								}
								else {
									var sv = o.series.value;
								}
								
								sv = this.objectToArray(sv);
								if (sv)
								{
									for (var i = 0; i < sv.length; i++)
									{
										var xid = sv[i].xid;
										var series = sv[i]['#text'];
										
										var dItem = {
											series: series
										};
										
										// get graph values and other info
										if (graphs) 
										{
											for (var j = 0; j < graphs.length; j++)
											{
												var value = null;
												var gv = graphs[j].value;
												
												gv = this.objectToArray(gv);
												if (gv)
												{
													// try the same first
													var found = false;
													if (i < gv.length)
													{
														var v = gv[i];
														if (v.xid == xid)
														{
															found = true;
															value = v;
														}
													}
													if (!found)
													{
														for (var k = 0; k < gv.length; k++)
														{
															var v = gv[k];
															if (v.xid == xid)
															{
																found = true;
																value = v;
															}
														}
													}
													if (found)
													{
														dItem['graph' + j + "_value"] = Number(value['#text']);															
														this.setDataAttributes(dItem, value, j);
													}
												}
											}
										}
										holder.push(dItem);
									}
								}
							}
						}
					}
				}
			}		
		}	
	},
	
	setDataAttributes:function(dItem, value, j)
	{
		var dataAttr = this.dataAttr;
		for (var a = 0; a < dataAttr.length; a++)
		{
			var name = dataAttr[a].n;
			var type = dataAttr[a].t;
			
			if (AmCharts.isDefined(value[name]))
			{
				var aval = value[name];
				
				if(name == "bullet_color")
				{
					name = "color";
				}				
				dItem['graph' + j + "_" + name] = this.parseValue(aval, type);
			}
		}		
	},
	
	
	objectToArray:function(obj)
	{
		if (obj)
		{
			if (obj.length == undefined)
			{
				var o = obj;
				obj = [o];
			}
		}

		return obj;
	},	
	
	getValue:function(a, o)
	{
		var f = o[a[0]];
		if(f != undefined)
		{					
			if(a.length > 1)
			{	
				a.shift();		
				return this.getValue(a, f);
			}
			else
			{
				return f;						
			}					
		}
		else{
			return;
		}
	},
	
	
	setChartValue:function(a, o, val)
	{
		var f = o[a[0]];

		if (f != undefined)
		{
			if(a.length == 2)
			{
				f[a[1]] = val;
			}
			else
			{
				a.shift();
				setChartValue(a, f, val);
			}
		}				
	},
	
	addLabel:function(label)
	{
		var text = label.text;
		
		if (text)
		{
			text = text.replace(/<(.|\n)*?>/g, '');
			
			var rotation = 0;
			if (AmCharts.toBoolean(label.rotate))
			{
				rotation = 270;
			}
			this.chart.addLabel(label.x, label.y, text, label.align, Number(label.text_size), AmCharts.toColor(label.text_color), rotation, 1);
		}			
	},
	
	setSettings:function(co, o, map, repl)
	{				
		if (o)
		{
			// process map
			for (var i = 0; i < map.length; i++)
			{
				var obj = map[i];
				var cs = obj.c;
				var type = obj.t;
				var s = obj.s;

				if(repl == "-")
				{
					s = s.replace("{a}.", "");
				}			
				
				if(repl)
				{
					s = s.replace("{a}", repl);
				}
				
				if (s != undefined)
				{
					var val;
					
					if (s.indexOf('.') != -1)
					{
						var a = s.split('.');
						val = this.getValue(a, o);
					}
					else 
					{
						val = o[s];
					}
					
					if (AmCharts.isDefined(val)) 
					{
						
						val = this.parseValue(val, type);
							
						if (val != undefined) 
						{
							if (cs.indexOf('.') == -1) 
							{
								co[cs] = val;
							}
							else 
							{
								this.setChartValue(cs.split('.'), co, val);
							}
						}
					}
				}
			}
		}		
	},	
	
	parseValue:function(val, type)
	{
		switch (type) 
		{
			case '!b':
				val = !AmCharts.toBoolean(val);
				break;
			case 'n':
				val = AmCharts.toNumber(val);
				break;
			case 'c':
				val = AmCharts.toColor(val);
				break;
				
			case 'b':
				val = AmCharts.toBoolean(val);
				break;
				
			case 'e':
				val = val.toLowerCase();
				if (val == "strong" || val == "regular")
				{
					val = ">";
				}
				break;
			case 'a':
				if(val.indexOf(",") != -1)
				{
					val = val.split(",")[0];
				}
				val = AmCharts.toNumber(val);
				if(isNaN(val))
				{
					val = 0;
				}
				val = val / 100;
				break;
			case 'arr':
				val = val.split(',');
				break;
				
			case '{s}':
				if (typeof(val) == 'object') 
				{
					if (val['#cdata-section'])
					{
						val = val['#cdata-section'];
					}
					else if (val['#text'])
					{
						val = val['#text'];
					}
					else 
					{
						val = "";
					}
				}
				val = val.replace(/{series}/g, '[[category]]');
				val = val.replace(/{start}/g, '[[open]]');
				val = val.replace(/{/g, '[[');
				val = val.replace(/}/g, ']]');
				val = val.replace(/<br>/g, '\n');
				val = val.replace(/<(.|)*?>/g, ''); // html tags

				if (val.replace(/ /g, '') == "")
				{
					val = undefined;
				}
				break;
		}		
		return val;		
	},
		
		
	createLabels:function(labels)
	{
		if (labels.length > 0) 
		{
			for (var i = 0; i < labels.length; i++) 
			{
				var label = labels[i];
				this.addLabel(label);
			}
		}
		else 
		{
			this.addLabel(labels)
		}
	},
	
	createLegend:function()
	{
		var lsettings = this.settings.legend;
		var enabled = true;
		var valuesEnabled = true;
		if(lsettings)
		{
		    enabled = AmCharts.toBoolean(lsettings.enabled, true)
		    if(lsettings.values)
		    {
		        valuesEnabled = AmCharts.toBoolean(lsettings.values.enabled, true);
		    }
		}

		if(enabled)
		{
			var legend = new AmCharts.AmLegend();
			this.chart.addLegend(legend);
			
			legend.color = this.chart.color;
			legend.fontSize = this.chart.fontSize;
			
			if(this.type == "pie" || this.type == "radar")
			{
				legend.marginLeft = 10;
				legend.marginRight = 10;
				legend.marginTop = 10;
				legend.marginBottom = 10;
			}
            else
            {
				legend.marginLeft = this.chart.marginLeft;
				legend.marginRight = this.chart.marginRight;
				legend.marginTop = 10;
				legend.marginBottom = 10;            
            }
			this.setSettings(legend, lsettings, this.legendMap);
			
			if(!valuesEnabled)
			{
			    legend.valueText = undefined;
			}
		}
	},	
	
	createGraphs:function()
	{
		if(this.settings.graphs)
		{		
			if(this.settings.graphs.graph)
			{
				var graphs = this.settings.graphs.graph;
				if (graphs.length > 0)
				{
					for (var i = 0; i < graphs.length; i++)
					{
						var settings = graphs[i];								
						this.addGraph(settings);
					}
				}
				else
				{					
					this.addGraph(graphs);
				}
			}
		}				
	},
		
	addGraph:function(s){
		var i = this.chart.graphs.length;
		var graph = new AmCharts.AmGraph();

		graph.valueField = "graph" + i + "_value";
		graph.xField = "graph" + i + "_x";
		graph.yField = "graph" + i + "_y";
		
		if(s.gid != undefined){
			graph.id = s.gid;
		}
		else
		{
			graph.id = "graph" + i;
		}
		
		for(var a = 0; a < this.dataAttr.length; a++)
		{
			var field = this.dataAttr[a].f;
			var name = this.dataAttr[a].n;
			graph[field] = "graph" + i + "_" + name;
		}
		
		this.setGraphSettings(graph, s);
		this.chart.addGraph(graph);				
	},
	
	setGraphSettings:function(graph, s)
	{
		this.setSettings(graph, this.settings, this.graphMapC);
		
		if(this.type == "column")
		{
			if (s.type != "line")
			{
				graph.type = "column";
			}
			if (graph.type == "line")
			{
				graph.lineThickness = 2;
			}
		}
		
		if(graph.type == "column")
		{
			graph.fillAlphas = 1;
			graph.lineAlpha = 0;			
			this.setSettings(graph, this.settings.column, this.graphMapColumn);		
		}
		if(graph.type == "line")
		{			
			this.setSettings(graph, this.settings.line, this.graphMapLine);		
		}		

		this.setSettings(graph, s, this.graphMap);
		
		if(s.axis == "right")
		{
			graph.valueAxis = this.chart.valueAxes[1];
		}				
		if(AmCharts.toBoolean(s.vertical_lines))
		{
			graph.type = "column";
			graph.fillAlphas = 1;
		}										
	},
	
	createGraphsFromData:function(data)
	{
		if(data[0].root)
		{
			if(data[0].root.chart)
			{
				var o = data[0].root.chart;
		
				if (o.graphs)
				{
					if (o.graphs.graph)
					{
						var graphs = o.graphs.graph;
						
						if (graphs.length == undefined)
						{
							this.setDataGraphsSettings(graphs);
						}
						else
						{						
							for (var i = 0; i < graphs.length; i++)
							{
								var gs = graphs[i];
								this.setDataGraphsSettings(gs);	
							}
						}
					}
				}
			}
		}
	},			

	setDataGraphsSettings:function(gs)
	{
		var graph;
		if (gs.gid != undefined)
		{
			graph = this.chart.getGraphById(gs.gid);
		}	

		if (!graph)
		{			
			this.addGraph(gs);
		}
		else
		{
			this.setGraphSettings(graph, gs);			
		}		
	},

	createGuides:function(guides)
	{
		if (guides.length > 0)
		{
			for (var i = 0; i < guides.length; i++)
			{
				var settings = guides[i];								
				this.addGuide(settings, i);
			}
		}
		else
		{
			this.addGuide(guides);
		}
	},			
		
	addGuide:function(s){
		var guide = {};

		this.setSettings(guide, s, this.guideMap);
		if (this.type == "radar")
		{
			this.setSettings(guide, s, this.radarGuideMap);
		}
		if(s.axis == "right")
		{
			this.chart.valueAxes[1].addGuide(guide);	
		}
		else
		{
			this.chart.valueAxes[0].addGuide(guide);
		}								
	},
	
	createValueAxes:function()
	{
		var settings = this.settings;
		var valueAxisMap = this.valueAxisMap;
		
		var leftAxis = new AmCharts.ValueAxis();
		leftAxis.autoGridCount = false;
		leftAxis.position = "left";
		this.chart.addValueAxis(leftAxis);
		this.setSettings(leftAxis, settings, valueAxisMap, "y_left");
		this.setSettings(leftAxis, settings, valueAxisMap, "value");
		
		var rightAxis = new AmCharts.ValueAxis();
		rightAxis.position = "right";
		rightAxis.autoGridCount = false;
		this.chart.addValueAxis(rightAxis);
		this.setSettings(rightAxis, settings, valueAxisMap, "y_left");
		rightAxis.minimum = NaN;
		rightAxis.maximum = NaN;
		this.setSettings(rightAxis, settings, valueAxisMap, "y_right");
		this.setSettings(rightAxis, settings, valueAxisMap, "value");								
	},			
	
	createXYAxes:function()
	{
		var xAxis = new AmCharts.ValueAxis();
		xAxis.position = "bottom";
		xAxis.autoGridCount = false;
		this.chart.addValueAxis(xAxis);
		this.setSettings(xAxis, this.settings, this.valueAxisMap, "x");
		
		var yAxis = new AmCharts.ValueAxis();
		yAxis.position = "left";
		yAxis.autoGridCount = false;
		this.chart.addValueAxis(yAxis);
		this.setSettings(yAxis, this.settings, this.valueAxisMap, "y");								
	},				
	
	createCursor:function()
	{
		var ss;
		var create = false;
		var type = this.type;
		if (type == "line")
		{
			ss = this.settings.indicator;
			create = true;
		}
		if(type == "xy")
		{
			create = true;
			ss = this.settings.zoom;
		}
		
		if(ss)
		{
			create = AmCharts.toBoolean(ss.enabled, true); 
		}
					
		if (create)
		{				
			var cursor = new AmCharts.ChartCursor();
			if (ss)
			{
				this.setSettings(cursor, ss, this.cursorMap);
			}
			if(this.settings.balloon)
			{
                cursor.oneBalloonOnly = this.settings.balloon.only_one; 
			} 
			
			this.chart.addChartCursor(cursor);				
		}
	},
	
	createScrollbar:function()
	{
		var ss = this.settings.scroller;
		var create = false;
		var type = this.type;
		if (type == "line" || type == "xy")
		{
			create = true;
		}
		if(ss)
		{
			create = AmCharts.toBoolean(ss.enabled, true); 
		}
					
		if (create)
		{	
			var sb = new AmCharts.ChartScrollbar();
			if (ss)
			{
				this.setSettings(sb, ss, this.scrollbarMap);
				
				if (ss.graph)
				{
					sb.graph = this.chart.graphs[ss.graph];
				}
			}				
			this.chart.addChartScrollbar(sb);
		}
	},
	
	setStackType:function(cs)
	{
		if(cs != undefined)
		{
			var st = cs.type;
			var newType;
			if(st == "stacked")
			{
				newType = "regular";	
			}
			if(st == "100% stacked")
			{
				newType = "100%";	
			}
			if(st == "3d column")
			{
				newType = "3d";	
			}								
			if (newType)
			{
				this.chart.valueAxes[0].stackType = newType;
			}
		}
	},				
	
		
	construct: function()
	{	
		this.dataType = "xml";
		this.pathToImages = "js/images/"
	
		// SETTING MAPS	
		this.commonSettingsMap = [
		
			// c - property name in JS version
			// s - setting in settings xml file
			
			// t - type of property: n - number; b - boolean; c - color; a - alpha; s - string; 
			// arr = array; {s} - balloon strings, where tags are used; e - effect
			
			{c:'backgroundColor',					s:'background.color',				t:'c'},
			{c:'backgroundAlpha',					s:'background.alpha',				t:'a'},
			{c:'borderColor',						s:'background.border_color',		t:'c'},
			{c:'borderAlpha',						s:'background.border_alpha',		t:'a'},
			{c:'backgroundImage',					s:'background.file',				t:'s'},						
			{c:'color',								s:'text_color',						t:'c'},
			{c:'fontFamily',						s:'font',							t:'s'},
			{c:'fontSize',							s:'text_size',						t:'n'},			
			{c:'percentFormatter.decimalSeparator',		s:'decimals_separator',			t:'s'},
			{c:'percentFormatter.thousandsSeparator',	s:'thousands_separator',		t:'s'},
			{c:'percentFormatter.precision',			s:'precision',					t:'n'},
			
			{c:'numberFormatter.decimalSeparator',		s:'decimals_separator',			t:'s'},
			{c:'numberFormatter.thousandsSeparator',	s:'thousands_separator',		t:'s'},
			{c:'numberFormatter.precision',				s:'digits_after_decimal',		t:'n'},
			{c:'colors',								s:'colors',						t:'arr'}
			],	
			
		this.balloonMap = [
			{c:'enabled',							s:'enabled',						t:'b'},
			{c:'fillColor',							s:'color',							t:'c'},
			{c:'fillAlpha',							s:'alpha',							t:'a'},			
			{c:'borderThickness',					s:'border_width',					t:'n'},
			{c:'borderColor',						s:'border_color',					t:'c'},
			{c:'borderAlpha',						s:'border_alpha',					t:'a'},
			{c:'cornerRadius',						s:'corner_radius',					t:'n'},			
			{c:'color',								s:'text_color',						t:'c'},
			{c:'fontSize',							s:'text_size',						t:'n'},
			{c:'horizontalPadding',					s:'horizontal_padding',				t:'n'},
			{c:'verticalPadding',					s:'vertical_padding',				t:'n'},
			{c:'pointerWidth',						s:'pointer_width',					t:'n'},
			{c:'pointerOrientation',				s:'pointer_orientation',			t:'s'},
			{c:'textShadowColor',					s:'text_shadow_color',				t:'c'},
			{c:'adjustBorderColor',					s:'adjust_border_color',			t:'b'}	
		];
		
		this.legendMap = [
			{c:'position',					s:'position',				t:'s'},
			{c:'align',						s:'align',					t:'s'},
			{c:'color',						s:'text_color',				t:'c'},
			{c:'fontSize',					s:'text_size',				t:'n'},
			{c:'backgroundColor',			s:'color',					t:'c'},
			{c:'backgroundAlpha',			s:'alpha',					t:'a'},
			{c:'borderColor',				s:'border_color',			t:'c'},
			{c:'borderAlpha',				s:'border_alpha',			t:'a'},			
			{c:'markerLabelGap',			s:'marker_label_gap',		t:'n'},
			{c:'spacing',					s:'spacing',				t:'n'},
			{c:'verticalGap',				s:'margins',				t:'n'},
			{c:'horizontalGap',				s:'margins',				t:'n'},
			{c:'markerDisabledColor',		s:'marker_disabled_color',	t:'c'},
			{c:'markerType',				s:'key.type',				t:'s'},
			{c:'markerSize',				s:'key.size',				t:'n'},
			{c:'markerBorderColor',			s:'key.border_color',		t:'c'},
			{c:'markerBorderAlpha',			s:'key.border_alpha',		t:'a'},
			{c:'markerBorderThickness',		s:'key.border_thickness',	t:'n'},
			{c:'maxColumns',				s:'max_columns',			t:'n'},
			{c:'marginTop',					s:'margin_top',				t:'n'},
			{c:'marginBottom',				s:'margin_bottom',			t:'n'},
			{c:'marginRight',				s:'margin_right',			t:'n'},
			{c:'marginLeft',				s:'margin_left',			t:'n'},
			{c:'valueWidth',				s:'values.width',			t:'n'},
			{c:'switchable',				s:'graph_on_off',			t:'b'},
			{c:'switchColor',				s:'switch_color',			t:'c'},
			{c:'switchType',				s:'switch_type',			t:'c'},
			{c:'rollOverColor',				s:'roll_over_color',		t:'c'},
			{c:'reversedOrder',				s:'reverse_order',			t:'b'},
			{c:'labelText',					s:'label_text',				t:'s'},
			{c:'valueText',					s:'values.text',			t:'{s}'}
		];
		
		this.pieSettingsMap = [
			{c:'pieAlpha',							s:'pie.alpha',						t:'a'},
			{c:'colors',							s:'pie.colors',						t:'arr'},
			{c:'pieBaseColor',						s:'pie.base_color',					t:'c'},
			{c:'pieBrightnessStep',					s:'pie.brightness_step',			t:'n'},
			{c:'marginLeft',						s:'pie.margin_left',				t:'s'},
			{c:'marginTop',							s:'pie.margin_top',					t:'s'},
			{c:'marginBottom',						s:'pie.margin_bottom',				t:'s'},
			{c:'marginRight',						s:'pie.margin_right',				t:'s'},
			{c:'radius',							s:'pie.radius',						t:'s'},
			{c:'minRadius',							s:'pie.min_radius',					t:'n'},
			{c:'hoverAlpha',						s:'pie.hover_alpha',				t:'a'},
			{c:'depth3D',							s:'pie.height',						t:'n'},
			{c:'pieBrightnessStep',					s:'pie.brightness_step',			t:'n'},			
			{c:'angle',								s:'pie.angle',						t:'n'},
			{c:'innerRadius',						s:'pie.inner_radius',				t:'s'},
			{c:'outlineColor',						s:'pie.outline_color',				t:'c'},
			{c:'outlineAlpha',						s:'pie.outline_alpha',				t:'a'},
			{c:'outlineThickness',					s:'pie.outline_thickness',			t:'n'},
			{c:'outlineColor',						s:'pie.outline_color',				t:'c'},
			//{c:'gradient',							s:'pie.gradient',					t:'s'},
			{c:'urlTarget',							s:'pie.link_target',				t:'{s}'},
			{c:'startRadius',						s:'animation.start_radius',			t:'s'},
			{c:'startAlpha',						s:'animation.start_alpha',			t:'a'},
			{c:'startDuration',						s:'animation.start_time',			t:'n'},
			{c:'startEffect',						s:'animation.start_effect',			t:'e'},
			{c:'sequencedAnimation',				s:'animation.sequenced',			t:'b'},
			{c:'pullOutRadius',						s:'animation.pull_out_radius',		t:'s'},
			{c:'pullOutDuration',					s:'animation.pull_out_time',		t:'n'},
			{c:'pullOutEffect',						s:'animation.pull_out_effect',		t:'e'},
			{c:'pullOutOnlyOne',					s:'animation.pull_out_only_one',	t:'b'},
			{c:'pullOnHover',						s:'animation.pull_out_on_hover',	t:'s'},			
			{c:'labelsEnabled',						s:'data_labels.enabled',			t:'b'},
			{c:'labelRadius',						s:'data_labels.radius',				t:'s'},
			{c:'labelTickColor',					s:'data_labels.line_color',			t:'c'},
			{c:'labelTickAlpha',					s:'data_labels.line_alpha',			t:'a'},
			{c:'labelText',							s:'data_labels.show',				t:'{s}'},
			{c:'hideLabelsPercent',					s:'data_labels.hide_labels_percent',t:'n'},
			{c:'balloonText',						s:'balloon.show',					t:'{s}'},
			{c:'groupPercent',						s:'group.percent',					t:'n'},
			{c:'groupedTitle',						s:'group.title',					t:'s'},
			{c:'groupedColor',						s:'group.color',					t:'c'},
			{c:'groupedPulled',						s:'group.pull_out',					t:'b'},
			{c:'groupedAlpha',						s:'group.alpha',					t:'a'},
			{c:'groupedDescription',				s:'group.description',				t:'s'},
			{c:'groupedColor',						s:'group.color',					t:'c'}];		
			
		this.radarSettingsMap = [
			{c:'radius',							s:'radar.radius',					t:'s'},
			{c:'startDuration',						s:'radar.grow_time',				t:'n'},
			{c:'startEffect',						s:'radar.grow_effect',				t:'e'},
			{c:'sequencedAnimation',				s:'radar.sequenced_grow',			t:'b'}			
		];
		
		this.radarAxisMap = [
			{c:'radarCategoriesEnabled',			s:'axes.labels.enabled',			t:'b'},
			{c:'axisTitleOffset',					s:'axes.labels.distance',			t:'n'}
						
		];		
		
		this.serialSettingsMap = [
			{c:'zoomOutText',						s:'zoom_out_button.text',			t:'s'},
			{c:'marginLeft',						s:'plot_area.margins.left',			t:'n'},
			{c:'marginTop',							s:'plot_area.margins.top',			t:'n'},
			{c:'marginBottom',						s:'plot_area.margins.bottom',		t:'n'},
			{c:'marginRight',						s:'plot_area.margins.right',		t:'n'},
			{c:'columnWidth', 						s:'vertical_lines.width',			t:'a'},
			{c:'depth3D',							s:'depth',							t:'n'},
			{c:'angle',								s:'angle',							t:'n'},
			{c:'rotate',							s:'rotate',							t:'b'},
			{c:'legend.marginRight',				s:'plot_area.margins.right',		t:'n'},
			{c:'legend.marginLeft',					s:'plot_area.margins.left',			t:'n'},	
			{c:'plotAreaFillColors',				s:'plot_area.color',				t:'c'},
			{c:'plotAreaFillAlphas',				s:'plot_area.alpha',				t:'a'},
			{c:'plotAreaBorderColor',				s:'plot_area.border_color',			t:'c'},
			{c:'plotAreaBorderAlpha',				s:'plot_area.border_alpha',			t:'a'}						
		];
			
		this.valueAxisMap = [
			{c:'gridColor',				s:'grid.{a}.color',					t:'c'},
			{c:'gridAlpha',				s:'grid.{a}.alpha',					t:'a'},
			{c:'dashLength',			s:'grid.{a}.dash_length',			t:'n'},			
			{c:'gridCount',				s:'grid.{a}.approx_count',			t:'n'},
			{c:'fillColor',				s:'grid.{a}.fill_color',			t:'c'},
			{c:'fillAlpha',				s:'grid.{a}.fill_alpha',			t:'a'},
			{c:'gridType',				s:'grid.{a}.type',					t:'s'},		
			{c:'stackType',				s:'values.{a}.stack_type',			t:'s'},			
			{c:'color',					s:'values.{a}.color',				t:'c'},
			{c:'fontSize',				s:'values.{a}.text_size',			t:'n'},
			{c:'labelsEnabled',			s:'values.{a}.enabled',				t:'b'},
			{c:'inside',				s:'values.{a}.inside',				t:'b'},
			{c:'reversed',				s:'values.{a}.reverse',				t:'b'},
			{c:'labelRotation',			s:'values.{a}.rotate',				t:'n'},
			{c:'labelFrequency',		s:'values.{a}.frequency',			t:'n'},
			{c:'showFirstLabel',		s:'values.{a}.skip_first',			t:'!b'},
			{c:'showLastLabel',			s:'values.{a}.skip_last',			t:'!b'},
			{c:'minimum',				s:'values.{a}.min',					t:'n'},
			{c:'maximum',				s:'values.{a}.max',					t:'n'},
			{c:'unit',					s:'values.{a}.unit',				t:'s'},
			{c:'unitPosition',			s:'values.{a}.unit_position',		t:'s'},
			{c:'integersOnly',			s:'values.{a}.integers_only',		t:'b'},
			{c:'duration',				s:'values.{a}.duration',			t:'s'},
			{c:'type',                  s:'values.{a}.type',                t:'s'},
			{c:'axisColor',				s:'axes.{a}.color',					t:'c'},
			{c:'axisAlpha',				s:'axes.{a}.alpha',					t:'a'},
			{c:'axisThickness',			s:'axes.{a}.width',					t:'n'},
			{c:'logarithmic',			s:'axes.{a}.logarithmic',			t:'b'},
			{c:'tickLength',			s:'axes.{a}.tick_length',			t:'n'},
			{c:'stackType',				s:'axes.{a}.type',					t:'s'}									
		];
		
		this.categoryAxisMap = [
			{c:'startOnAxis',			s:'start_on_axis', 					t:'b'},
			{c:'gridColor',				s:'grid.{a}.color',					t:'c'},
			{c:'gridAlpha',				s:'grid.{a}.alpha',					t:'a'},
			{c:'dashLength',			s:'grid.{a}.dash_length',			t:'n'},			
			{c:'gridCount',				s:'grid.{a}.approx_count',			t:'n'},
			{c:'color',					s:'values.{a}.color',				t:'c'},
			{c:'fontSize',				s:'values.{a}.text_size',			t:'n'},
			{c:'labelsEnabled',			s:'values.{a}.enabled',				t:'b'},
			{c:'inside',				s:'values.{a}.inside',				t:'b'},
			{c:'labelRotation',			s:'values.{a}.rotate',				t:'n'},
			{c:'labelFrequency',		s:'values.{a}.frequency',			t:'n'},
			{c:'showFirstLabel',		s:'values.{a}.skip_first',			t:'!b'},
			{c:'showLastLabel',			s:'values.{a}.skip_last',			t:'!b'},
			{c:'axisColor',				s:'axes.{a}.color',					t:'c'},
			{c:'axisAlpha',				s:'axes.{a}.alpha',					t:'a'},
			{c:'axisThickness',			s:'axes.{a}.width',					t:'n'},
			{c:'tickLength',			s:'axes.{a}.tick_length',			t:'n'}
		];
		
		this.graphMap = [
			{c:'type',								s:'type',							t:'s'},
			{c:'title',								s:'title',							t:'s'},
			{c:'stackable',							s:'stackable',						t:'b'},
			{c:'lineColor',							s:'color',							t:'c'},
			{c:'fillColors',						s:'color',							t:'c'},
			{c:'fillColors',						s:'gradient_fill_colors',			t:'c'},
			{c:'lineAlpha',							s:'line_alpha',						t:'a'},
			{c:'lineAlpha',                         s:'alpha',                          t:'a'},
			{c:'lineThickness',						s:'line_width',						t:'n'},
			{c:'fillAlphas',						s:'fill_alpha',						t:'a'},
			{c:'fillColors',						s:'fill_color',						t:'c'},
			{c:'bullet',							s:'bullet',							t:'s'},
			{c:'balloonColor',						s:'balloon_color',					t:'c'},
			{c:'balloonAlpha',						s:'balloon_alpha',					t:'a'},
			{c:'balloonTextColor',					s:'balloon_text_color',				t:'c'},
			{c:'balloonText',						s:'balloon_text',					t:'{s}'},
			{c:'labelText',							s:'data_labels',					t:'{s}'},
			{c:'color',								s:'data_labels_text_color',			t:'c'},
			{c:'fontSize',							s:'data_labels_text_size',			t:'n'},
			{c:'labelPosition',						s:'data_labels_position',			t:'s'},			
			{c:'bulletSize',						s:'bullet_size',					t:'n'},
			{c:'cornerRadiusTop',					s:'corner_radius_top',				t:'n'},
			{c:'cornerRadiusBottom',				s:'corner_radius_bottom',			t:'n'},
			{c:'bulletColor',						s:'bullet_color',					t:'c'},
			{c:'bulletAlpha',						s:'bullet_alpha',					t:'a'},
			{c:'hidden',							s:'hidden',							t:'b'},
			{c:'visibleInLegend',					s:'visible_in_legend',				t:'b'},			
			{c:'showBalloon',						s:'selected',						t:'b'},
			{c:'includeInMinMax',                   s:'min_max',                        t:'b'}		
		];
		
		this.graphMapC = [
			{c:'connect',							s:'connect',						t:'b'},
			{c:'hideBulletsCount',					s:'hide_bullets_count',				t:'n'},
			{c:'urlTarget',							s:'link_target',					t:'s'},
			{c:'balloonColor',						s:'balloon.color',					t:'c'}
		];	
		
		this.graphMapColumn = [
			{c:'fillAlphas',						s:'alpha',							t:'a'},
			{c:'lineAlpha',							s:'border_alpha',					t:'a'},
			{c:'labelText',							s:'data_labels',					t:'{s}'},
			{c:'balloonText',						s:'balloon_text',					t:'{s}'},
			{c:'urlTarget',							s:'link_target',					t:'s'},
			{c:'color',								s:'data_labels_text_color',			t:'c'},
			{c:'fontSize',							s:'data_labels_text_size',			t:'n'},
			{c:'labelPosition',						s:'data_labels_position',			t:'s'},
			{c:'cornerRadiusTop',					s:'corner_radius_top',				t:'s'},
			{c:'cornerRadiusBottom',				s:'corner_radius_bottom',			t:'s'},
			{c:'gradientOrientation',				s:'gradient',						t:'s'}		
		];	
		
		this.graphMapLine = [
			{c:'connect',							s:'connect',						t:'b'},
			{c:'lineThickness',						s:'width',							t:'n'},
			{c:'lineAlpha',							s:'alpha',							t:'a'},
			{c:'fillAlpha',							s:'fill_alpha',						t:'a'},
			{c:'bullet',							s:'bullet',							t:'s'},
			{c:'bulletSize',						s:'bullet_size',					t:'n'},
			{c:'labelText',							s:'data_labels',					t:'{s}'},
			{c:'balloonText',						s:'balloon_text',					t:'{s}'},
			{c:'urlTarget',							s:'link_target',					t:'s'},
			{c:'color',								s:'data_labels_text_color',			t:'c'},
			{c:'fontSize',							s:'data_labels_text_size',			t:'n'}
		];						
		
	
		this.columnSettingsMap = [
			{c:'columnWidth',						s:'width',							t:'a'},
			{c:'columnSpacing',						s:'spacing',						t:'n'},
			{c:'startDuration',						s:'grow_time',						t:'n'},
			{c:'startEffect',						s:'grow_effect',					t:'e'},
			{c:'sequencedAnimation',				s:'sequenced_grow',					t:'b'}
		];
		
		this.cursorMap = [
			{c:'zoomable',							s:'zoomable',						t:'b'},
			{c:'cursorColor',						s:'color',							t:'c'},
			{c:'cursorAlpha',						s:'line_alpha',						t:'a'},
			{c:'selectionColor',					s:'selection_color',				t:'c'},
			{c:'categoryBalloonEnabled',			s:'x_balloon_enabled',				t:'b'},
			{c:'color',								s:'x_balloon_text_color',			t:'c'}
		];
		
		this.xyCursorMap = [
			{c:'enabled',							s:'enabled',						t:'b'},
			{c:'cursorColor',						s:'border_color',					t:'c'},
			{c:'cursorAlpha',						s:'border_alpha',					t:'a'},
			{c:'selectionColor',					s:'bg_color',						t:'c'}
		];		
		
		this.scrollbarMap = [
			{c:'selectedBackgroundColor',			s:'color',							t:'c'},
			{c:'selectedBackgroundAlpha',			s:'alpha',							t:'a'},
			{c:'backgroundColor',					s:'bg_color',						t:'c'},
			{c:'backgroundAlpha',					s:'bg_alpha',						t:'a'},
			{c:'scrollbarHeight',					s:'height',							t:'n'}			
		];
		
		this.zoomOutButtonMap = [
			{c:'backgroundColor',			s:'color',		t:'c'},
			{c:'backgroundAlpha',			s:'alpha',		t:'a'},
			{c:'color',						s:'text_color',	t:'c'},
			{c:'fontSize',					s:'text_size',	t:'n'}
		];
		
		this.guideMap = [
			{c:'value',						s:'start_value',	t:'n'},
			{c:'toValue',					s:'end_value',		t:'n'},
			{c:'label',						s:'title',			t:'s'},
			{c:'lineColor',					s:'color',			t:'c'},
			{c:'lineAlpha',					s:'alpha',			t:'a'},
			{c:'inside',					s:'inside',			t:'b'},
			{c:'lineThickness',				s:'width',			t:'n'},
			{c:'fillAlpha',					s:'fill_alpha',		t:'a'},
			{c:'fillColor',					s:'fill_color',		t:'c'},
			{c:'dashLength',				s:'dash_length',	t:'n'}			
		];
		
		this.radarGuideMap = [
			{c:'fillAlpha',					s:'alpha',		t:'a'},
			{c:'fillColor',					s:'color',		t:'c'}
		];		
		
		this.dataAttr = [{n:'url', t:'s', f:'urlField'},
						{n:'bullet', t:'s', f:'bulletField'},
						{n:'bullet_color', t:'c', f:'colorField'},
						{n:'color', t:'c', f:'colorField'},
						{n:'bullet_size', t:'n', f:'bulletSizeField'},
						{n:'description', t:'s',f:'descriptionField'},
						{n:'start', t:'n',f:'openField'}
		];		
	}				
});