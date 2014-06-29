<?php

	require_once("tests.php");

	$step = (isset($_GET['step']) ? (int) $_GET['step'] : 0);

	$qr_text = (isset($tests[$step]['qr']) ? $tests[$step]['qr'] : $tests[$step]['qr']);

	require_once("/mnt/data/www/libs/phpqrcode/qrlib.php");
	QRcode::png($qr_text, FALSE, QR_ECLEVEL_L, 10);
