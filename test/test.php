<?php

	require_once("tests.php");

	$expected = expected_sent($tests[1], "");
	echo <<<HTML_BLOCK
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<title>GScanTool - Echo</title>
	</head>
	<body>
		<h1>GScanTool - Test</h1>
		<p>Tests to run, to validate that all the apps current function works on a device</p>
		<p>
			<span>To test GScanTool, start the app and scan this QR-code:</span>
			<br />
			<img src='qr.png' alt='[QR-code step 0]' />
		</p>
		<h2>Step 0 - Start app</h2>
		<p>Start the app, from the device, or from the developer tool</p>
		<h3>Expected result</h3>
		<p><img src='screenshot_step_00.png' alt='[Screenshoot of step 0]' /></p>
		<h2>Step 1 - {$tests[1]['title']}</h2>
		<p>
			<span>Press 'Change Project' and scan this code</span>
			<br />
			<img src='test.qr.png.php?step=1' alt='[QR Step 1]' />
		</p>
		<h3>Expected result</h3>
		<p>
			<span>Message with: '{$tests[1]['response']['text']}'</span>
			<br/>
			<img src='screenshot_step_01.png' alt='[Screenshoot of step 1]' />
		</p>
		<ul>
			<li>Connection type: 'get'</li>
			<li>URL: '{$tests[1]['qr']}'</li>
			<li>postdata: ''</li>
			<li>response: '{$expected['response']}'</li>
		</ul>

HTML_BLOCK;

	$next_url = $tests[1]['response']['url'];

	foreach($tests as $test_nr => $current_test)
	{
		if($test_nr < 2)
		{
			continue;
		}

		$expected = expected_sent($current_test, $next_url);

		if(isset($current_test['response']['url']))
		{
			$next_url = $current_test['response']['url'];
		}

		$expected_html_safe = array();
		foreach($expected as $expected_key => $expected_value)
		{
			$expected_html_safe[$expected_key] = htmlentities($expected_value);
		}

		echo <<<HTML_BLOCK
		<h2>Step {$test_nr} - {$current_test['title']}</h2>
		<p>
			<span>scan this code</span>
			<br />
			<img src='test.qr.png.php?step={$test_nr}' alt='[QR Step {$test_nr}]' />
		</p>
		<h3>Expected result</h3>
		<p>Message with: '{$expected_html_safe['text']}'</p>
		<ul>
			<li>Connection type: '{$expected_html_safe['type']}'</li>
			<li>URL: '{$expected_html_safe['url']}'</li>
			<li>postdata: '{$expected_html_safe['data']}'</li>
			<li>response: '{$expected_html_safe['response']}'</li>
		</ul>

HTML_BLOCK;
	}

	echo <<<HTML_BLOCK
	</body>
</html>

HTML_BLOCK;
?>
