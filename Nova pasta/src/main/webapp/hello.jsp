<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		
		<style type="text/css">
		
			.djs-container .highlight .djs-outline {
   				stroke-width: 2px !important;
   				stroke: #08c !important;
   				fill: #bce2f5 !important;
			}
			
			.djs-container .highlight .djs-visual>:nth-child(1) {
   				fill: #bce2f5 !important;
			}
			
		</style>
	</head>
	<body>
		${message}	
		
		<div>
			<button onclick="goToBpmn()">BPMN</button>
			<button onclick="goToDmn()">DMN</button>
		</div>
		
		<div id="canvas" style="height:3000px;"></div>
		
 		<!-- <script src="bpmn-js/bpmn-viewer.js"></script>
		<script src="bpmn-js/bpmn-modeler.js"></script> -->
 		<script src="bpmn-js/bpmn-navigated-viewer.js"></script>	
 		<script src="dmn-js/dmn-viewer.js"></script>	
<!--  		<script src="jquery-3.2.1.min.js"></script> -->
		
		<link rel="stylesheet" href="bpmn-js/assets/diagram-js.css" />
		<link rel="stylesheet" href="bpmn-js/assets/bpmn-font/css/bpmn-embedded.css" />
		<link rel="stylesheet" href="dmn-js/css/dmn-js.css" />
		<!-- <link rel="stylesheet" href="dmn-js/assets/diagram-js.css" /> -->
		
		<script>	

			var events = [
				'element.hover',
				'element.out',
				'element.click',
				'element.dbclick',
				'element.mousedown',
				'element.mouseup'
			];

			function goToBpmn() {
				document.getElementById('canvas').innerHTML = '';
				downloadFileAndDoAction('diagrams/teste.bpmn', initBPMNViewer);
			}

			function goToDmn() {
				document.getElementById('canvas').innerHTML = '';
				downloadFileAndDoAction('tables/table_2.dmn', initDMNViewer);
			}	
			

			function downloadFileAndDoAction(filePath, action) {
			  	
				var xhr = new XMLHttpRequest();

			  	xhr.onreadystatechange = function() {
			    	if (xhr.readyState === 4) {
			    		action(xhr.response);			      		
			    	}
			  	};

			  	xhr.open('GET', filePath, true);
			  	xhr.send(null);
			  	
			}
		  	
			function initBPMNViewer(bpmnXML) {

				var viewer = new BpmnJS({ 
					container: '#canvas'
				});
				
				viewer.importXML(bpmnXML, function(err) {
					if (err) {
						console.log("Erro: " + err);
				  	} else {			
				    	var canvas = viewer.get('canvas');
				    	canvas.zoom('fit-viewport');
				    	canvas.alignmentBaseline;
				    	canvas.addMarker("Task_09acqcl", 'highlight');

				    	loadCallbacksForViewer(viewer);				
					}
				});
			}

 		  	function initDMNViewer(dmnXML) {

		  		var viewer = new DmnJS({
					container:'#canvas'
		  		})

		  		viewer.importXML(dmnXML, function(err) {
					if (err) {
						console.log("Erro: " + err);
					} else {
						loadCallbacksForViewer(viewer);
					}
			  	})
			}

			function loadCallbacksForViewer(viewer) {
				var eventBus = viewer.get('eventBus');
				for (var i = 0; i < events.length; i++) {
					eventBus.on(events[i], function(e) {
						console.log("Event: " + event.type  + " no item " + e.element.id);
					});
				}
			}

			
			
		</script>
	</body>
</html>