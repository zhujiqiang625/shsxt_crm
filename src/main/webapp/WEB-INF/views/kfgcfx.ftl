<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>amCharts examples</title>
    <link rel="stylesheet" href="style.css" type="text/css">
    <script src="${ctx}/amcharts/amcharts.js" type="text/javascript"></script>
    <script src="${ctx}/amcharts/serial.js" type="text/javascript"></script>
    <script src="${ctx}/highcharts4/jquery-1.8.3.min.js" type="text/javascript"></script>

    <script>
            var chart;
            /*
			$.get("khgcfx", {}, function() {

			});*/
			var chartData = [];
			$.ajax({
				url:"khgcfx",
				dataType: 'json',
				data:{},
				async: false, // 是否异步，默认是true， false就是同步
				success: function(data) {
					chartData = data.result;
				}
			});
			/*
            var chartData = [
                {
                    "country": "USA",
                    "visits": 4025
                },
                {
                    "country": "China",
                    "visits": 1882
                },
                {
                    "country": "Japan",
                    "visits": 1809
                }
            ];*/


            AmCharts.ready(function () {
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.dataProvider = chartData;
                chart.categoryField = "levelName";
                chart.startDuration = 1;

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.labelRotation = 0;
                categoryAxis.gridPosition = "start";

                // value
                // in case you don't want to change default settings of value axis,
                // you don't need to create it, as one value axis is created automatically.

                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.valueField = "amount";
                graph.balloonText = "类型：[[levelName]] <br>数量：[[amount]]个</b>";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 0.8;
                chart.addGraph(graph);

                // CURSOR
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorAlpha = 0;
                chartCursor.zoomable = false;
                chartCursor.categoryBalloonEnabled = false;
                chart.addChartCursor(chartCursor);

                chart.creditsPosition = "top-right";

                chart.write("chartdiv");
            });
        </script>
</head>

<body>
<div id="chartdiv" style="width: 100%; height: 400px;"></div>
</body>

</html>