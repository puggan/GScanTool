 <html>
	<head>
		<title>GScanTool</title>
		<script src="js/onload_manager.js"></script>
		<script src="js/misc.js"></script>
		<script src="js/filter_table.js"></script>
		<script src="js/sort_table2.js"></script>
		<script src="js/set_checkboxes_in_form.js"></script>
		<link href="css/tables.css" type="text/css" rel="stylesheet" />
	</head>
	<body>
		<h1>GScanTool</h1>

		<h2>App</h2>
		<ul>
			<li>
				<span>Android paket:</span>
				<a href='GScanTool_1_03.apk'>GScanTool_1_03.apk</a>
				<span>(v1.03 relesed 2014-06-29)</span>
			</li>
			<li>
				<span>Google Play link:</span>
				<a href='https://play.google.com/store/apps/details?id=se.puggan.gscantool'>https://play.google.com/store/apps/details?id=se.puggan.gscantool</a>
			</li>
			<li>
				<span>Github link</span>
				<a href='https://github.com/puggan/GScanTool'>https://github.com/puggan/GScanTool</a>
			</li>
		</ul>

		<h2>Use</h2>
		<ul>
			<li>Visit a site that uses the GScanTool-protocol</li>
			<li>Scan the QR code in the webbrowser to "login"</li>
			<li>Scan other bar-codes or QR-codes that the website asks you for.</li>
		</ul>

		<h2>Projects using GScanTool protocol</h2>
		<p>Here is a list of projects where you can use this app</p>
		<ul>
			<li>(Add your project here, be emailing <a href='mailto:gscantool@puggan.se?subject=_____+is+using+GScanTool'>gscantool@puggan.se</a>)</li>
			<li><a href='echo.php'>Echo - test</a></li>
			<li><a href='test/test.php'>GScanTool unit-test</a>, Runed by app-developer before pulicing a new version</li>
		</ul>

		<h2>For developers</h2>
		<p>Here comes some extra information for website developers</p>

		<h3>Exemple of implementation areas</h3>
		<ol>
			<li>local store using webshop, let the phone be a barcode scanner, that adds scanned product to the webshop-cart.</li>
			<li>ticket validation, add a qr code on all sold tickets, and use this app to check-in and validate that its a valid ticket, and that its only being used once.</li>
			<li>shopping planer/history, scan the prodcts you buy, and use that shoping history to calculate when you going to run out of milk next time.</li>
			<li>Medialibrary, let the users add the movies/cds/books they have to a library, by scanning the barcode.</li>
		</ol>

		<h3>Protocol</h3>
		<ol>
			<li>The app scans a QR encoded URL</li>
			<li>The app download the content at that URL</li>
			<li>
				<span>The app json decode the answer and look for parameters:</span><br />
				<ul>
					<li>
						<b>name</b>
						<span>, the title of the project</span>
					</li>
					<li>
						<b>url</b>
						<span>, where to send next </span>
					</li>
					<li>
						<b>type</b>
						<span>, prefered data format: get, post, json, plain. default: post </span>
					</li>
					<li>
						<b>text</b>
						<span>textmessage to show the user</span>
					</li>
				</ul>
			</li>
			<li>The app scans for any QR- or bar-code</li>
			<li>The app sends the QR to the previus given URL</li>
			<li>The app scans for next QR- or bar-code</li>
		</ol>

		<h2>Version History</h2>
		<ul>
			<li>v1.00, 2014-06-21, first release</li>
			<li>v1.01, 2014-06-21, moved webrequest to background thread, fix for bug on android 3.0+</li>
			<li>v1.02, 2014-06-29, remember project between uses</li>
			<li>v1.03, 2014-06-29, Now supports diffrent comunication protocols: git, json, plain and post</li>
		</ul>

		<h3>Test units</h3>
		<table class='sort_yes filter_yes' style='margin-bottom: 50px;'>
			<thead>
				<tr>
					<th>Version tested</th>
					<th>Test date</th>
					<th>Status</th>
					<th>Android version</th>
					<th>API level</th>
					<th>Android/API Name</th>
					<th>Model number</th>
					<th>Unit Name</th>
				</tr>
			</thead>
			<tbody>
				<tr class='valid'>
					<td>v1.03</td>
					<td>2014-06-29<span style='display: none;'>00:00:00 D</span></td>
					<td>Working</td>
					<td>4.4.2</td>
					<td>19</td>
					<td>KitKat</td>
					<td>SM-T230</td>
					<td>Samsung Galaxy Tab 4</td>
				</tr>
				<tr class='valid'>
					<td>v1.03</td>
					<td>2014-06-29<span style='display: none;'>00:00:00 C</span></td>
					<td>Working</td>
					<td>2.3.7</td>
					<td>10</td>
					<td>Gingerbread</td>
					<td>ST27i</td>
					<td>Sony Xperia Go</td>
				</tr>
				<tr class='valid'>
					<td>v1.02</td>
					<td>2014-06-29<span style='display: none;'>00:00:00 B</span></td>
					<td>Working</td>
					<td>4.4.2</td>
					<td>19</td>
					<td>KitKat</td>
					<td>SM-T230</td>
					<td>Samsung Galaxy Tab 4</td>
				</tr>
				<tr class='valid'>
					<td>v1.02</td>
					<td>2014-06-29<span style='display: none;'>00:00:00 A</span></td>
					<td>Working</td>
					<td>2.3.7</td>
					<td>10</td>
					<td>Gingerbread</td>
					<td>ST27i</td>
					<td>Sony Xperia Go</td>
				</tr>
				<tr class='invalid'>
					<td>v1.01</td>
					<td>2014-06-27</td>
					<td title='Resets the app betwenn scans'>Misbehave</td>
					<td>4.4</td>
					<td>19</td>
					<td>KitKat</td>
					<td>C6603(?)</td>
					<td>Sony Xperia Z(?)</td>
				</tr>
				<tr class='valid'>
					<td>v1.01</td>
					<td>2014-06-21<span style='display: none;'>23:59:59 A</span></td>
					<td>Working</td>
					<td>2.3.7</td>
					<td>10</td>
					<td>Gingerbread</td>
					<td>ST27i</td>
					<td>Sony Xperia Go</td>
				</tr>
				<tr class='invalid'>
					<td>v1.00</td>
					<td>2014-06-21 18:47</td>
					<td>Crashes</td>
					<td>4.4</td>
					<td>19</td>
					<td>KitKat</td>
					<td>C6603</td>
					<td>Sony Xperia Z</td>
				</tr>
				<tr class='valid'>
					<td>v1.00</td>
					<td>2014-06-21</td>
					<td>Working</td>
					<td>2.3.7</td>
					<td>10</td>
					<td>Gingerbread</td>
					<td>ST27i</td>
					<td>Sony Xperia Go</td>
				</tr>
			</tbody>
	</body>
</html>