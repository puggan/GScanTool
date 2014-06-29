<?php
	if($_POST)
	{
		$text = "Recived post data:\n" . print_r($_POST, TRUE);
	}
	else if($_GET)
	{
		$text = "Recived get data:\n" . print_r($_GET, TRUE);
	}
	else
	{
		$text = "No data Recived";
	}

	echo json_encode(
		array(
			'text' => $text,
		)
	);
?>
