<?php

	$tests = array();

	$test_nr = 1;
	$tests[$test_nr] = array();
	$tests[$test_nr]['title'] = "Init Test";
	$tests[$test_nr]['qr'] = "http://gscantool.puggan.se/gscantool/test/api.php?step={$test_nr}";
	$tests[$test_nr]['response'] = array();
	$tests[$test_nr]['response']['name'] = "GScannTool Test";
	$tests[$test_nr]['response']['text'] = "GScannTool Test started";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";
	$tests[$test_nr]['validate'] = array();
	$tests[$test_nr]['validate']['type'] = 'get';
	$tests[$test_nr]['validate']['get'] = array('step' => $test_nr);
	$tests[$test_nr]['validate']['code'] = FALSE;

	// nr 2
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Simple scan";

	// nr 3
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Switch protocol to get";
	$tests[$test_nr]['response']['type'] = "get";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";

	// nr 4
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "using protocol get";
	$tests[$test_nr]['validate']['type'] = 'get';

	// nr 5
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Switch protocol to plain";
	$tests[$test_nr]['response']['type'] = "plain";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";
	$tests[$test_nr]['validate']['type'] = 'get';

	// nr 6
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "using protocol plain";
	$tests[$test_nr]['validate']['type'] = 'plain';

	// nr 7
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Switch protocol to json";
	$tests[$test_nr]['response']['type'] = "json";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";
	$tests[$test_nr]['validate']['type'] = 'plain';

	// nr 8
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "using protocol json";
	$tests[$test_nr]['validate']['type'] = 'json';

	// nr 9
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Switch protocol to post";
	$tests[$test_nr]['validate']['type'] = 'json';
	$tests[$test_nr]['response']['type'] = "post";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";

	// nr 10
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "Switching url";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php?extra=test";

	// nr 11
	$tests[$test_nr] = base_test(++$test_nr);
	$tests[$test_nr]['title'] = "using new url";
	$tests[$test_nr]['response']['url'] = "http://gscantool.puggan.se/gscantool/test/api.php";
	$tests[$test_nr]['validate']['get'] = array('extra'=>'test');

	function base_test($test_nr)
	{
		return array(
			'title' => "Unnamed test {$test_nr}",
			'qr' => "Step {$test_nr}",
			'response' => array(
				'text' => "Step {$test_nr} OK",
				),
			'validate' => array(
				'type' => 'post',
				'code' => TRUE,
				),
			);
	}

	function expected_sent($test, $url)
	{
		$data = array('type' => 'QR_CODE', 'data' => $test['qr']);
		$default_result = array(
			'url' => $url,
			'type' => $test['validate']['type'],
			'data' => NULL,
			'response' => json_encode($test['response']),
			'text' => json_encode($test['response']['text']),
			);

		switch($test['validate']['type'])
		{
			case 'json':
			{
				return array('data' => json_encode($data)) + $default_result;
			}
			case 'plain':
			{
				return array('data' => $data['data']) + $default_result;
			}
			case 'get':
			{
				$separator = (strstr($url, "?") ? "?" : "&");
				$get_url = $url . $separator . http_build_query($data);
				return array('url' => $get_url) + $default_result;
			}
			case 'post':
			default:
			{
				return array('type' => 'post', 'data' => http_build_query($data)) + $default_result;
			}
		}
	}