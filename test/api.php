<?php

	require_once("tests.php");

	$type = strtolower($_SERVER['REQUEST_METHOD']);

	$error = '';
	$code = '';
	$scan_type = '';
	$raw = '';
	$json = '';

	if($type == 'get')
	{
		if(isset($_GET['data']))
		{
			$code = $_GET['data'];
		}
		if(isset($_GET['type']))
		{
			$scan_type = $_GET['type'];
		}
	}
	else if($type == 'post')
	{
		$raw = file_get_contents("php://input", "r");

		if($raw)
		{
			$json = @json_decode($raw, TRUE);
		}

		$json_error = json_last_error();
		$json_error_text = json_last_error_msg();

// 		$error = trim("{$json_error}: {$json_error_text}", ' :');

		if($json !== NULL)
		{
			$type = 'json';
			if(isset($json['data']))
			{
				$code = $json['data'];
			}
			if(isset($json['type']))
			{
				$scan_type = $json['type'];
			}
		}
		else if(isset($_POST['data']))
		{
			$code = $_POST['data'];

			if(isset($_POST['type']))
			{
				$scan_type = $_POST['type'];
			}
		}
		else
		{
			$type = 'plain';
			$code = $raw;
			$raw = NULL;
		}
	}

	$matching_test_nr = -1;

	if($code)
	{
		foreach($tests as $test_nr => $current_test)
		{
			if($current_test['qr'] AND $current_test['qr'] == $code)
			{
				$matching_test_nr = $test_nr;
				break;
			}
		}
	}
	else if(isset($_GET['step']) AND $_GET['step'] == 1)
	{
		$matching_test_nr = 1;
	}

	if($matching_test_nr > 0)
	{
		$test = $tests[$matching_test_nr];
		$valid = TRUE;

		if($valid AND isset($test['validate']['type']))
		{
			if($test['validate']['type'] != $type)
			{
				$valid = FALSE;
				$error = "Wrongt type, expected '{$test['validate']['type']}', got '{$type}'";
			}
		}

		if($valid AND isset($test['validate']['get']))
		{
			if(is_array($test['validate']['get']))
			{
				foreach($test['validate']['get'] as $get_key => $get_value)
				{
					if(isset($_GET[$get_key]))
					{
						if($_GET[$get_key] != $get_value)
						{
							$valid = FALSE;
							$error = "Bad get parameter '{$get_key}', expected '{$get_value}', got '{$_GET[$get_key]}'";
							break;
						}
					}
					else
					{
						$valid = FALSE;
						$error = "Missing get parameter '{$get_key}', have: " . ($_GET ? implode(', ', array_keys($_GET)) : '(none)');
						break;
					}
				}
			}
			else
			{
				$valid = FALSE;
				$error = "Invalid test {$matching_test_nr}, validate-get isn't an array";
			}
		}


		if($valid)
		{
			echo json_encode($test['response']);
		}
		else
		{
			echo json_encode(array('text' => $error) + $test['response']);
		}
	}
	else
	{
		echo json_encode(
			array(
				'text' => 'se debug',
				'debug' => array(
					'type' => $type,
					'code' => $code,
					'scan_type' => $scan_type,
					'error' => $error,
					'raw' => $raw,
				),
			)
		);
	}
?>
