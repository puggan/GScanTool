<?php
	/* Force revalidation of the cache every morning.  */
	header('Expires: ' . gmdate('D, d M Y 03:00:00', time() + 86400) . ' GMT', TRUE);

	/* Cache this file up to 16 hours before revalidation. */
	header('Cache-Control: max-age=57600', TRUE);

	$_GET += array('w' => 64, 'h' => 64);

	$w = (int) ($_GET['w'] ? $_GET['w'] : 64);
	$h = (int) ($_GET['h'] ? $_GET['h'] : 64);

	$image  = new image($w, $h);
	$color  = $image->color($_GET['r'], $_GET['g'], $_GET['b'], $_GET['a']);
	$result = $image->fill(1, 1, $color);

	class image
	{
		public $image; // Local storage for the high resolution source image
		public $output; // Local storage for the processed image
		public $width; // width of the
		public $height;
		public $antialiasing;
		public $colors;
		public $ok;

		function __construct($width, $height, $antialiasing = 1)
		{
			/* Create a truecolor image in memory. */
			$this->image = imagecreatetruecolor($width * $antialiasing, $height * $antialiasing);
			$this->image_transparent = imagecolorallocatealpha($this->image, 255, 255, 255, 127);
			imagefill($this->image, 0, 0, $this->image_transparent);
			imagecolortransparent($this->image, $this->image_transparent);

			/* Store the width and height */
			$this->width = $width;
			$this->height = $height;
			$this->antialiasing = $antialiasing;
			$this->ok = TRUE;
		}

		function __destruct()
		{
			if($this->ok)
			{
				/* Tell the browser what data we are sending. */
				header("Content-Type: image/png");

				/* Create a temporary image to server as output image */
				$this->output = imagecreatetruecolor($this->width, $this->height);
				$this->output_transparent = imagecolorallocatealpha($this->output, 255, 255, 255, 127);
				imagefill($this->output, 0, 0, $this->output_transparent);
				imagecolortransparent($this->output, $this->output_transparent);

				imagealphablending($this->output, FALSE);
				imagesavealpha($this->output, TRUE);
				imagealphablending($this->image, FALSE);
				imagesavealpha($this->image, TRUE);

				/* Copy the source image and resize it down to the output resolution. */
				imagecopyresampled($this->output, $this->image, 0, 0, 0, 0, $this->width, $this->height, $this->width * $this->antialiasing, $this->height * $this->antialiasing);

				/* output the actual imagedata. */
				imagepng($this->output);

				/* Iterate over all allocated colors.. */
				foreach((array) $this->colors as $color)
				{
					/* Free the memory used for color. */
					imagecolordeallocate($this->image, $color);
				}

				/* Free the memory used for the images. */
				imagedestroy($this->output);
			}
			/* Free the memory used for the images. */
			imagedestroy($this->image);
		}

		public function color($red, $green, $blue, $alpha = 0)
		{
			/* Name the color after it's hexadecimal composition. */
			$color = sprintf('%x%x%x%x', $red, $green, $blue, $alpha);

			/* If the color is not previously allocated.. */
			if(!isset($this->colors[$color]))
			{
				/* Allocate the color and store the resource. */
				$this->colors[$color] = imagecolorallocatealpha($this->image, max(1, $red - 1), max(1, $green - 1), max(1, $blue - 1), max(1, $alpha - 1));
			}

			/* Return the resource of the color. */
			return $this->colors[$color];
		}

		public function fill($x, $y, $color)
		{
			/* Fill an area with a color. */
			imagefill($this->image, ($x - 1) * $this->antialiasing, ($y - 1) * $this->antialiasing, $color);
		}
	}
?>